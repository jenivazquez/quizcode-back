package com.quizcode.module.quiz.application.validation;

import com.quizcode.error.exception.ForbiddenAccessExceptionCustom;
import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.error.exception.InvalidStatusExceptionCustom;
import com.quizcode.error.exception.NotFoundExceptionCustom;
import com.quizcode.module.quiz.domain.QuizToQuestionPort;
import com.quizcode.module.quiz.domain.QuizToRoomPort;
import com.quizcode.module.quiz.domain.QuizRepository;
import com.quizcode.module.quiz.domain.entity.Quiz;
import com.quizcode.module.quiz.domain.entity.QuizStatus;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class QuizValidator {

    private final QuizRepository quizRepository;
    private final QuizToQuestionPort questionPort;
    private final QuizToRoomPort roomPort;

    public QuizValidator(QuizRepository quizRepository, @Lazy QuizToQuestionPort questionPort, @Lazy QuizToRoomPort roomPort) {
        this.quizRepository = quizRepository;
        this.questionPort = questionPort;
        this.roomPort = roomPort;
    }

    public void validateToCreate(Quiz newQuiz) {
        checkNewTitleUnique(newQuiz.getOwnerId(), newQuiz.getTitle());
    }

    public void validateToUpdate(Quiz newQuiz) {
        Quiz savedQuiz = quizRepository.findById(newQuiz.getId()).orElseThrow(() -> new NotFoundExceptionCustom("El cuestionario no existe"));
        checkOwnership(newQuiz.getOwnerId(), savedQuiz.getOwnerId());
        checkQuizEditable(savedQuiz);
        checkEditTitleUnique(newQuiz.getOwnerId(), newQuiz.getTitle(), newQuiz.getId());
    }

    public void validateToFindById(Quiz savedQuiz, String ownerId) {
        this.checkOwnership(ownerId, savedQuiz.getOwnerId());
    }

    public void validateToUpdateStatus(String id, QuizStatus newStatus, String ownerId) {
        checkStatusInput(newStatus);
        Quiz savedQuiz = quizRepository.findById(id).orElseThrow(() -> new NotFoundExceptionCustom("El cuestionario no existe"));
        checkOwnership(ownerId, savedQuiz.getOwnerId());
        checkStatusTransition(savedQuiz.getStatus(), newStatus);
        if (newStatus == QuizStatus.PUBLISHED) checkHasQuestions(id);
        if (newStatus == QuizStatus.CREATED) checkHasNoRooms(id);
    }

    public void validateToDelete(String id, String ownerId) {
        Quiz savedQuiz = quizRepository.findById(id).orElseThrow(() -> new NotFoundExceptionCustom("El cuestionario no existe"));
        checkOwnership(ownerId, savedQuiz.getOwnerId());
    }

    private void checkStatusInput(QuizStatus newStatus) {
        if (newStatus == null) throw new InvalidDataExceptionCustom("El campo estado es obligatorio");
        if (newStatus == QuizStatus.LOCKED) throw new InvalidDataExceptionCustom("No puedes bloquear este cuestionario manualmente. Se bloqueará automáticamente al crear la primera sala.");
    }

    private void checkStatusTransition(QuizStatus savedStatus, QuizStatus newStatus) {
        if (savedStatus == QuizStatus.LOCKED) throw new InvalidStatusExceptionCustom("El cuestionario tiene salas activas y no puede cambiar de estado manualmente");
        if (savedStatus == newStatus) throw new InvalidStatusExceptionCustom("El cuestionario ya está en ese estado");
    }

    private void checkNewTitleUnique(String ownerId, String title) {
        if (quizRepository.existsByOwnerIdAndTitle(ownerId, title)) {
            throw new InvalidDataExceptionCustom("Ya tienes un cuestionario con ese título");
        }
    }

    private void checkEditTitleUnique(String ownerId, String title, String id) {
        if (quizRepository.existsByOwnerIdAndTitleExcludingId(ownerId, title, id)) {
            throw new InvalidDataExceptionCustom("Ya tienes un cuestionario con ese título");
        }
    }

    private void checkHasQuestions(String quizId) {
        if (!questionPort.hasQuestions(quizId)) {
            throw new InvalidDataExceptionCustom("No puedes publicar un cuestionario sin preguntas");
        }
    }

    private void checkHasNoRooms(String quizId) {
        if (roomPort.hasRooms(quizId)) {
            throw new InvalidStatusExceptionCustom("No puedes despublicar un cuestionario que tiene salas activas");
        }
    }

    private void checkOwnership(String ownerId, String savedOwnerId) {
        if (!ownerId.equals(savedOwnerId)) {
            throw new ForbiddenAccessExceptionCustom("Este cuestionario no pertenece a tu usuario");
        }
    }

    private void checkQuizEditable(Quiz savedQuiz) {
        if (savedQuiz.getStatus() == QuizStatus.PUBLISHED) {
            throw new InvalidStatusExceptionCustom("No puedes modificar un cuestionario publicado");
        }
        if (savedQuiz.getStatus() == QuizStatus.LOCKED) {
            throw new InvalidStatusExceptionCustom("No puedes modificar un cuestionario que tiene salas activas");
        }
    }
}