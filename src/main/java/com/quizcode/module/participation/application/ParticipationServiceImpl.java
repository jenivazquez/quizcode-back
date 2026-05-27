package com.quizcode.module.participation.application;

import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.error.exception.NotFoundExceptionCustom;
import com.quizcode.module.participation.application.validation.ParticipationValidator;
import com.quizcode.module.participation.domain.ParticipationRepository;
import com.quizcode.module.participation.domain.ParticipationService;
import com.quizcode.module.participation.domain.ParticipationToQuestionPort;
import com.quizcode.module.participation.domain.ParticipationToRoomPort;
import com.quizcode.module.participation.domain.entity.answer.ReviewedAnswer;
import com.quizcode.module.participation.domain.entity.question.QuestionSummary;
import com.quizcode.module.participation.domain.entity.answer.Answer;
import com.quizcode.module.participation.domain.entity.participation.Participation;
import com.quizcode.module.participation.domain.entity.status.ReviewStatus;
import com.quizcode.shared.Util;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationRepository partRepository;
    private final ParticipationValidator partValidator;
    private final ParticipationToRoomPort roomPort;
    private final ParticipationToQuestionPort questionPort;

    public ParticipationServiceImpl(ParticipationRepository partRepository, ParticipationValidator partValidator, ParticipationToRoomPort roomPort, ParticipationToQuestionPort questionPort) {
        this.partRepository = partRepository;
        this.partValidator = partValidator;
        this.roomPort = roomPort;
        this.questionPort = questionPort;
    }

    @Override
    public String create(Participation participation) {
        partValidator.validateToCreate(participation);
        return partRepository.create(participation);
    }

    @Override
    public String login(String roomId, String username, String password) {
        if (Util.isNull(username) || Util.isNull(password)) throw new InvalidDataExceptionCustom("El nombre de usuario y la contraseña son obligatorios");
        Participation savedPart = partRepository.findByRoomIdAndUsername(roomId, username).orElseThrow(() -> new NotFoundExceptionCustom("El usuario no existe en esta sala"));
        partValidator.validateToLogin(savedPart, password);
        return savedPart.getId();
    }

    @Override
    public Participation findById(String roomId, String participationId) {
        Participation savedPart = partRepository.findById(participationId).orElseThrow(() -> new NotFoundExceptionCustom("La participación no existe"));
        partValidator.validateToFindById(savedPart, roomId);
        return savedPart;
    }

    @Override
    public void submitAnswers(Participation participation) {
        Participation savedPart = partRepository.findById(participation.getId()).orElseThrow(() -> new NotFoundExceptionCustom("La participación no existe"));
        List<QuestionSummary> questions = questionPort.findByQuizId(roomPort.getQuizIdByRoom(participation.getRoomId()));
        partValidator.validateToSubmitAnswers(savedPart, participation, questions);
        participation.calculateTotalTime(savedPart.getStartedAt());
        partRepository.update(participation);
        autoReviewAnswers(participation.getId(), participation.getAnswers(), questions);
    }

    private void autoReviewAnswers(String participationId, List<Answer> answers, List<QuestionSummary> questions) {
        List<Answer> reviewedAnswers = getReviewedAnswers(answers, questions);
        int totalScore = reviewedAnswers.stream().mapToInt(a -> a.getScore() != null ? a.getScore() : 0).sum();
        partRepository.updateReviewAnswers(participationId, reviewedAnswers, totalScore, ReviewStatus.IA_REVIEWED);
    }

    private List<Answer> getReviewedAnswers(List<Answer> submittedAnswers, List<QuestionSummary> questions) {
        Map<String, QuestionSummary> questionMap = questions.stream().collect(Collectors.toMap(QuestionSummary::getId, q -> q));
        return submittedAnswers.stream()
                .map(answer -> getReviewedAnswer(answer, questionMap.get(answer.getQuestionId())))
                .toList();
    }

    private Answer getReviewedAnswer(Answer answer, QuestionSummary question) {
        return "EDIT_CODE".equals(question.getType()) ? reviewCodeAnswer(answer, question) : reviewOptionAnswer(answer, question);
    }

    private Answer reviewOptionAnswer(Answer answer, QuestionSummary question) {
        boolean isCorrect = new HashSet<>(question.getValidOptionCodes()).equals(new HashSet<>(answer.getCodeOptions()));
        int score = isCorrect ? question.getScore() : 0;
        return new ReviewedAnswer(answer.getQuestionId(), isCorrect, score, null).getAnswer();
    }

    //TODO: Eliminar esto y llamar en su lugar a la IA
    private Answer reviewCodeAnswer(Answer answer, QuestionSummary question) {
        boolean isCorrect = new Random().nextBoolean();
        int score = new Random().nextInt(0, question.getScore());
        return new ReviewedAnswer(answer.getQuestionId(), isCorrect, score, "Este el el feedback").getAnswer();
    }

    @Override
    public List<Participation> findByRoomIdToRanking(String roomId) {
        partValidator.validateToFindRanking(roomId);
        List<Participation> parts = roomPort.isRoomReviewed(roomId) ? partRepository.findFinishedByRoomId(roomId) : partRepository.findReviewedByRoomId(roomId);
        return parts.stream()
                .sorted(Comparator.comparingInt(Participation::getTotalScore).reversed()
                        .thenComparingLong(Participation::getTotalTime))
                .toList();
    }

    @Override
    public List<Participation> findByRoomIdAsRoomOwner(String ownerId, String quizId, String roomId) {
        partValidator.validateToFindPartsAsRoomOwner(ownerId, quizId, roomId);
        return partRepository.findByRoomId(roomId);
    }

    @Override
    public Participation findByIdAsRoomOwner(String ownerId, String quizId, String roomId, String participationId) {
        Participation savedPart = partRepository.findById(participationId).orElseThrow(() -> new NotFoundExceptionCustom("La participación no existe"));
        partValidator.validateToFindPartAsRoomOwner(savedPart, ownerId, quizId, roomId);
        return savedPart;
    }

    @Override
    public void deleteAsRoomOwner(String ownerId, String quizId, String roomId, String participationId) {
        Participation savedPart = partRepository.findById(participationId).orElseThrow(() -> new NotFoundExceptionCustom("La participación no existe"));
        partValidator.validateToDeleteAsRoomOwner(savedPart, ownerId, quizId, roomId);
        partRepository.delete(participationId);
    }

    @Override
    public void updateReviewAsRoomOwner(String ownerId, String quizId, String roomId, String participationId, List<Answer> answers) {
        Participation savedPart = partRepository.findById(participationId).orElseThrow(() -> new NotFoundExceptionCustom("La participación no existe"));
        partValidator.validateToUpdateReviewAsRoomOwner(savedPart, ownerId, quizId, roomId);
        int totalScore = answers.stream().mapToInt(a -> a.getScore() != null ? a.getScore() : 0).sum();
        partRepository.updateReviewAnswers(participationId, answers, totalScore, ReviewStatus.OWNER_REVIEWED);
    }
}