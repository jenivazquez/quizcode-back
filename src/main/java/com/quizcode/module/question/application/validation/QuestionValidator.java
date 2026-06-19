package com.quizcode.module.question.application.validation;

import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.error.exception.InvalidStatusExceptionCustom;
import com.quizcode.error.exception.NotFoundExceptionCustom;
import com.quizcode.module.question.domain.QuestionRepository;
import com.quizcode.module.question.domain.QuestionToParticipationPort;
import com.quizcode.module.question.domain.entity.question.Question;
import com.quizcode.module.question.domain.QuestionToQuizPort;
import org.springframework.stereotype.Component;

@Component
public class QuestionValidator {

    private final QuestionRepository questionRepository;
    private final QuestionToQuizPort quizPort;
    private final QuestionToParticipationPort participationPort;

    public QuestionValidator(QuestionRepository questionRepository, QuestionToQuizPort quizPort, QuestionToParticipationPort participationPort) {
        this.questionRepository = questionRepository;
        this.quizPort = quizPort;
        this.participationPort = participationPort;
    }

    public void validateToCreate(String ownerId, Question newQuestion) {
        checkQuizEditable(newQuestion.getQuizId(), ownerId);
        checkOrderNotDuplicated(newQuestion.getQuizId(), newQuestion.getOrder());
    }

    public void validateToFindByQuizId(String ownerId, String quizId) {
        quizPort.checkQuizExistByOwner(quizId, ownerId);
    }

    public void validateToFindByQuizIdToAnswer(String participationId) {
        if (!participationPort.isParticipationStarted(participationId)) {
            throw new InvalidStatusExceptionCustom("La participación no está activa");
        }
    }

    public void validateToFindByQuizIdToReview(String participationId) {
        if (!participationPort.isParticipationFinished(participationId)) {
            throw new InvalidStatusExceptionCustom("La participación no está finalizada. No puedes obtener la lista de preguntas con sus respuestas correctas.");
        }
    }

    public void validateToUpdate(String ownerId, Question newQuestion) {
        checkQuestionExists(newQuestion.getQuizId(), newQuestion.getId());
        checkQuizEditable(newQuestion.getQuizId(), ownerId);
        checkOrderNotDuplicatedForUpdate(newQuestion.getQuizId(), newQuestion.getOrder(), newQuestion.getId());
    }

    public void validateToDelete(String ownerId, String quizId, String id) {
        checkQuestionExists(quizId, id);
        checkQuizEditable(quizId, ownerId);
    }

    private void checkOrderNotDuplicated(String quizId, Integer order) {
        if (questionRepository.existsByQuizIdAndOrder(quizId, order))
            throw new InvalidDataExceptionCustom("Ya existe una pregunta con el orden " + order + " en este cuestionario");
    }

    private void checkOrderNotDuplicatedForUpdate(String quizId, Integer order, String id) {
        if (questionRepository.existsByQuizIdAndOrderAndIdNot(quizId, order, id))
            throw new InvalidDataExceptionCustom("Ya existe una pregunta con el orden " + order + " en este cuestionario");
    }

    private void checkQuestionExists(String quizId, String id) {
        if (!questionRepository.existsByQuizIdAndId(quizId, id)) {
            throw new NotFoundExceptionCustom("La pregunta no existe en este cuestionario");
        }
    }

    private void checkQuizEditable(String quizId, String ownerId) {
        if (!quizPort.isQuizEditable(quizId, ownerId)) {
            throw new InvalidStatusExceptionCustom("Solo puedes modificar las preguntas de un cuestionario en estado borrador");
        }
    }
}
