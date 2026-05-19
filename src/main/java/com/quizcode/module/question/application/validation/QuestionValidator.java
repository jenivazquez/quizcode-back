package com.quizcode.module.question.application.validation;

import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.error.exception.InvalidStatusExceptionCustom;
import com.quizcode.error.exception.NotFoundExceptionCustom;
import com.quizcode.module.question.domain.QuestionRepository;
import com.quizcode.module.question.domain.entity.question.Question;
import com.quizcode.module.question.domain.QuizPort;
import org.springframework.stereotype.Component;

@Component
public class QuestionValidator {

    private final QuestionRepository questionRepository;
    private final QuizPort quizPort;

    public QuestionValidator(QuestionRepository questionRepository, QuizPort quizPort) {
        this.questionRepository = questionRepository;
        this.quizPort = quizPort;
    }

    public void validateToCreate(String ownerId, Question question) {
        checkQuizEditable(question.getQuizId(), ownerId);
        checkOrderNotDuplicated(question.getQuizId(), question.getOrder());
    }

    public void validateToFindByQuizId(String ownerId, String quizId) {
        quizPort.checkQuizAccess(quizId, ownerId);
    }

    public void validateToUpdate(String ownerId, Question question) {
        checkQuestionExists(question.getQuizId(), question.getId());
        checkQuizEditable(question.getQuizId(), ownerId);
        checkOrderNotDuplicatedForUpdate(question.getQuizId(), question.getOrder(), question.getId());
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
