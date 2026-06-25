package com.quizcode.module.participation.application;

import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.error.exception.NotFoundExceptionCustom;
import com.quizcode.module.participation.application.validation.ParticipationValidator;
import com.quizcode.module.participation.domain.ParticipationRepository;
import com.quizcode.module.participation.domain.ParticipationService;
import com.quizcode.module.participation.domain.ParticipationToQuestionPort;
import com.quizcode.module.participation.domain.ParticipationToQuizPort;
import com.quizcode.module.participation.domain.ParticipationToRoomPort;
import com.quizcode.module.participation.domain.entity.answer.Answer;
import com.quizcode.module.participation.domain.entity.question.QuestionSummary;
import com.quizcode.module.participation.domain.entity.participation.Participation;
import com.quizcode.module.participation.domain.entity.status.ReviewStatus;
import com.quizcode.shared.Util;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationRepository partRepository;
    private final ParticipationValidator partValidator;
    private final ParticipationToRoomPort roomPort;
    private final ParticipationToQuestionPort questionPort;
    private final ParticipationToQuizPort quizPort;
    private final AnswerReviewer answerReviewer;

    public ParticipationServiceImpl(ParticipationRepository partRepository, ParticipationValidator partValidator, ParticipationToRoomPort roomPort, ParticipationToQuestionPort questionPort, ParticipationToQuizPort quizPort, AnswerReviewer answerReviewer) {
        this.partRepository = partRepository;
        this.partValidator = partValidator;
        this.roomPort = roomPort;
        this.questionPort = questionPort;
        this.quizPort = quizPort;
        this.answerReviewer = answerReviewer;
    }

    @Override
    public String create(Participation participation) {
        partValidator.validateToCreate(participation);
        return partRepository.create(participation);
    }

    @Override
    public Participation login(String roomId, String username, String password) {
        if (Util.isNull(username) || Util.isNull(password)) throw new InvalidDataExceptionCustom("El nombre de usuario y la contraseña son obligatorios");
        Participation savedPart = partRepository.findByRoomIdAndUsername(roomId, username).orElseThrow(() -> new NotFoundExceptionCustom("El usuario no existe en esta sala"));
        partValidator.validateToLogin(savedPart, password);
        return savedPart;
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
        String quizId = roomPort.getQuizIdByRoom(participation.getRoomId());
        List<QuestionSummary> questions = questionPort.findByQuizId(quizId);
        partValidator.validateToSubmitAnswers(savedPart, participation, questions);
        participation.calculateTotalTime(savedPart.getStartedAt(), quizPort.getTimeLimit(quizId));
        partRepository.update(participation);
        answerReviewer.reviewAnswersAsync(participation.getId(), participation.getAnswers(), questions);
    }

    @Override
    public List<Participation> findByRoomIdToRanking(String roomId) {
        partValidator.validateToFindRanking(roomId);
        List<Participation> parts = roomPort.isRoomReviewed(roomId) ? partRepository.findFinishedByRoomId(roomId) : partRepository.findReviewedByRoomId(roomId);
        return parts.stream()
                .sorted(byReviewStatusGroup().thenComparing(Participation::getTotalScore, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Participation::getTotalTime, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }

    @Override
    public List<Participation> findByRoomIdAsRoomOwner(String ownerId, String quizId, String roomId) {
        partValidator.validateToFindPartsAsRoomOwner(ownerId, quizId, roomId);
        return partRepository.findByRoomId(roomId).stream()
                .sorted(byReviewStatusGroup().thenComparing(Participation::getTotalScore, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Participation::getTotalTime, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }

    private Comparator<Participation> byReviewStatusGroup() {
        return Comparator.comparingInt(p -> {
            if (p.getReviewStatus() == null) return 0;
            return switch (p.getReviewStatus()) {
                case IA_REVIEWED, OWNER_REVIEWED -> 0;
                case PENDING -> 1;
                case IA_FAILED -> 2;
            };
        });
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