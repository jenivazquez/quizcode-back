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

    public void validateQuizToCreate(Quiz quiz) {
        checkNewTitleUnique(quiz.getOwnerId(), quiz.getTitle());
    }

    public void validateQuizToUpdate(Quiz quiz) {
        Quiz savedQuiz = quizRepository.findById(quiz.getId()).orElseThrow(() -> new NotFoundExceptionCustom("El cuestionario no existe"));
        checkOwnership(quiz.getOwnerId(), savedQuiz.getOwnerId());
        checkQuizEditable(savedQuiz);
        checkEditTitleUnique(quiz.getOwnerId(), quiz.getTitle(), quiz.getId());
    }

    public void validateQuizToFindById(Quiz quiz, String sentOwnerId) {
        this.checkOwnership(sentOwnerId, quiz.getOwnerId());
    }

    public void validateQuizToUpdateStatus(String id, QuizStatus sentStatus, String sentOwnerId) {
        checkStatusInput(sentStatus);
        Quiz savedQuiz = quizRepository.findById(id).orElseThrow(() -> new NotFoundExceptionCustom("El cuestionario no existe"));
        checkOwnership(sentOwnerId, savedQuiz.getOwnerId());
        checkStatusTransition(savedQuiz.getStatus(), sentStatus);
        if (sentStatus == QuizStatus.PUBLISHED) checkHasQuestions(id);
        if (sentStatus == QuizStatus.CREATED) checkHasNoRooms(id);
    }

    public void validateQuizToDelete(String id, String sentOwnerId) {
        Quiz savedQuiz = quizRepository.findById(id).orElseThrow(() -> new NotFoundExceptionCustom("El cuestionario no existe"));
        checkOwnership(sentOwnerId, savedQuiz.getOwnerId());
    }

    private void checkStatusInput(QuizStatus sentStatus) {
        if (sentStatus == null) throw new InvalidDataExceptionCustom("El campo estado es obligatorio");
        if (sentStatus == QuizStatus.LOCKED) throw new InvalidDataExceptionCustom("No puedes bloquear este cuestionario manualmente. Se bloqueará automáticamente al crear la primera sala.");
    }

    private void checkStatusTransition(QuizStatus savedStatus, QuizStatus sentStatus) {
        if (savedStatus == QuizStatus.LOCKED) throw new InvalidStatusExceptionCustom("El cuestionario tiene salas activas y no puede cambiar de estado manualmente");
        if (savedStatus == sentStatus) throw new InvalidStatusExceptionCustom("El cuestionario ya está en ese estado");
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

    private void checkOwnership(String sentOwnerId, String savedOwnerId) {
        if (!sentOwnerId.equals(savedOwnerId)) {
            throw new ForbiddenAccessExceptionCustom("Este cuestionario no pertenece a tu usuario");
        }
    }

    private void checkQuizEditable(Quiz quiz) {
        if (quiz.getStatus() == QuizStatus.PUBLISHED) {
            throw new InvalidStatusExceptionCustom("No puedes modificar un cuestionario publicado");
        }
        if (quiz.getStatus() == QuizStatus.LOCKED) {
            throw new InvalidStatusExceptionCustom("No puedes modificar un cuestionario que tiene salas activas");
        }
    }
}