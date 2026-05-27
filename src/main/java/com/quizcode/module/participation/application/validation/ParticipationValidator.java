package com.quizcode.module.participation.application.validation;

import com.quizcode.error.exception.ForbiddenAccessExceptionCustom;
import com.quizcode.error.exception.InvalidCredentialsExceptionCustom;
import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.error.exception.InvalidStatusExceptionCustom;
import com.quizcode.error.exception.NotFoundExceptionCustom;
import com.quizcode.module.participation.domain.ParticipationRepository;
import com.quizcode.module.participation.domain.ParticipationToQuizPort;
import com.quizcode.module.participation.domain.ParticipationToRoomPort;
import com.quizcode.module.participation.domain.entity.question.QuestionSummary;
import com.quizcode.module.participation.domain.entity.answer.Answer;
import com.quizcode.module.participation.domain.entity.participation.Participation;
import com.quizcode.module.participation.domain.entity.status.ParticipationStatus;
import com.quizcode.module.participation.domain.entity.status.ReviewStatus;
import com.quizcode.module.user.domain.PasswordHasher;
import com.quizcode.shared.Util;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ParticipationValidator {

    private final ParticipationRepository participationRepository;
    private final ParticipationToRoomPort roomPort;
    private final ParticipationToQuizPort quizPort;
    private final PasswordHasher passwordHasher;

    public ParticipationValidator(ParticipationRepository participationRepository, ParticipationToRoomPort roomPort, ParticipationToQuizPort quizPort, PasswordHasher passwordHasher) {
        this.participationRepository = participationRepository;
        this.roomPort = roomPort;
        this.quizPort = quizPort;
        this.passwordHasher = passwordHasher;
    }

    public void validateToCreate(Participation newPart) {
        checkRoomOpened(newPart.getRoomId());
        checkUsernameUniqueInRoom(newPart.getRoomId(), newPart.getUsername());
    }

    public void validateToLogin(Participation savedPart, String password) {
        checkRoomNotCreatedStatus(savedPart.getRoomId());
        checkParticipationExistsByUsername(savedPart.getRoomId(), savedPart.getUsername());
        checkCredentials(savedPart.getPassword(), password);
    }

    public void validateToFindById(Participation savedPart, String roomId) {
        checkParticipationBelongsToRoom(savedPart.getRoomId(), roomId);
    }

    public void validateToSubmitAnswers(Participation savedPart, Participation newPart, List<QuestionSummary> questions) {
        checkRoomOpened(newPart.getRoomId());
        checkParticipationBelongsToRoom(savedPart.getRoomId(), newPart.getRoomId());
        checkParticipationIsStarted(savedPart);
        checkAnswersMatchQuestions(newPart.getAnswers(), questions);
        checkAnswerTypes(newPart.getAnswers(), questions);
    }

    public void validateToFindRanking(String roomId) {
        checkRoomNotCreatedStatus(roomId);
    }

    public void validateToFindPartsAsRoomOwner(String ownerId, String quizId, String roomId) {
        quizPort.checkQuizExistByOwner(quizId, ownerId);
        checkRoomBelongToQuiz(quizId, roomId);
    }

    public void validateToFindPartAsRoomOwner(Participation savedPart, String ownerId, String quizId, String roomId) {
        quizPort.checkQuizExistByOwner(quizId, ownerId);
        checkRoomBelongToQuiz(quizId, roomId);
        checkParticipationBelongsToRoom(savedPart.getRoomId(), roomId);
    }

    public void validateToDeleteAsRoomOwner(Participation savedPart, String ownerId, String quizId, String roomId) {
        quizPort.checkQuizExistByOwner(quizId, ownerId);
        checkRoomBelongToQuiz(quizId, roomId);
        checkParticipationBelongsToRoom(savedPart.getRoomId(), roomId);
    }

    public void validateToUpdateReviewAsRoomOwner(Participation savedPart, String ownerId, String quizId, String roomId) {
        quizPort.checkQuizExistByOwner(quizId, ownerId);
        checkRoomBelongToQuiz(quizId, roomId);
        checkParticipationBelongsToRoom(savedPart.getRoomId(), roomId);
        checkParticipationIsReviewed(savedPart);
        checkRoomIsNotReviewed(roomId);
    }

    private void checkRoomIsNotReviewed(String roomId) {
        if (roomPort.isRoomReviewed(roomId)) {
            throw new InvalidStatusExceptionCustom("La sala ya ha sido marcada como revisada");
        }
    }

    private void checkUsernameUniqueInRoom(String roomId, String username) {
        if (participationRepository.existsByRoomIdAndUsername(roomId, username)) {
            throw new InvalidDataExceptionCustom("Ya existe una participación con ese nombre de usuario en esta sala");
        }
    }

    private void checkParticipationExistsByUsername(String roomId, String username) {
        if (!participationRepository.existsByRoomIdAndUsername(roomId, username)) {
            throw new NotFoundExceptionCustom("No existe ninguna participación con ese nombre de usuario en esta sala");
        }
    }

    private void checkParticipationBelongsToRoom(String savedRoomId, String newRoomId) {
        if (!savedRoomId.equals(newRoomId)) {
            throw new ForbiddenAccessExceptionCustom("La participación no pertenece a esta sala");
        }
    }

    public void checkRoomBelongToQuiz(String quizId, String roomId) {
        if (!roomPort.roomBelongsToQuiz(roomId, quizId)) {
            throw new ForbiddenAccessExceptionCustom("La sala no pertenece a ese cuestionario");
        }
    }

    private void checkParticipationIsStarted(Participation participation) {
        if (participation.getStatus() != ParticipationStatus.STARTED) {
            throw new InvalidStatusExceptionCustom("Esta participación ya ha sido finalizada");
        }
    }

    private void checkParticipationIsReviewed(Participation participation) {
        if (participation.getReviewStatus() == null || participation.getReviewStatus() == ReviewStatus.PENDING) {
            throw new InvalidStatusExceptionCustom("La participación aún no ha sido revisada por la IA");
        }
    }

    private void checkRoomOpened(String roomId) {
        if (!roomPort.isRoomOpened(roomId)) {
            throw new InvalidStatusExceptionCustom("La sala no está abierta. No se puede realizar esta operación.");
        }
    }

    private void checkRoomNotCreatedStatus(String roomId) {
        if (roomPort.isRoomCreated(roomId)) {
            throw new InvalidStatusExceptionCustom("No se puede acceder a una sala que aún no está abierta");
        }
    }

    private void checkCredentials(String savedPassword, String newPassword) {
        if (!passwordHasher.matches(newPassword, savedPassword)) {
            throw new InvalidCredentialsExceptionCustom("Las credenciales son incorrectas");
        }
    }

    private void checkAnswersMatchQuestions(List<Answer> answers, List<QuestionSummary> questions) {
        Set<String> questionIds = questions.stream().map(QuestionSummary::getId).collect(Collectors.toSet());
        Set<String> answerIds = answers.stream().map(Answer::getQuestionId).collect(Collectors.toSet());
        if (questionIds.size() != answerIds.size()) {
            throw new InvalidDataExceptionCustom("El número de respuestas no coincide con el número de preguntas del cuestionario");
        }
        if (!questionIds.equals(answerIds)) {
            throw new InvalidDataExceptionCustom("Las preguntas respondidas no corresponden al cuestionario de esta sala");
        }
    }

    private void checkAnswerTypes(List<Answer> answers, List<QuestionSummary> questions) {
        Map<String, QuestionSummary> questionMap = questions.stream().collect(Collectors.toMap(QuestionSummary::getId, q -> q));
        for (Answer answer : answers) {
            String type = questionMap.get(answer.getQuestionId()).getType();
            if ("EDIT_CODE".equals(type)) {
                if (Util.isNull(answer.getWrittenAnswer())) {
                    throw new InvalidDataExceptionCustom("La pregunta de tipo código requiere una respuesta escrita");
                }
                if (!Util.isNull(answer.getCodeOptions())) {
                    throw new InvalidDataExceptionCustom("La pregunta de tipo código no admite opciones");
                }
            } else {
                if (Util.isNull(answer.getCodeOptions())) {
                    throw new InvalidDataExceptionCustom("La pregunta de tipo opción requiere al menos una opción seleccionada");
                }
                if (!Util.isNull(answer.getWrittenAnswer())) {
                    throw new InvalidDataExceptionCustom("La pregunta de tipo opción no admite respuesta escrita");
                }
                if ("SINGLE_CHOICE".equals(type) && answer.getCodeOptions().size() != 1) {
                    throw new InvalidDataExceptionCustom("La pregunta de opción simple requiere exactamente una opción seleccionada");
                }
            }
        }
    }
}
