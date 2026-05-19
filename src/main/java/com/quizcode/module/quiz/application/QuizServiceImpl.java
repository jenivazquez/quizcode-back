package com.quizcode.module.quiz.application;

import com.quizcode.error.exception.NotFoundExceptionCustom;
import com.quizcode.module.quiz.application.validation.QuizValidator;
import com.quizcode.module.quiz.domain.entity.Quiz;
import com.quizcode.module.quiz.domain.entity.QuizStatus;
import com.quizcode.module.quiz.domain.QuizRepository;
import com.quizcode.module.quiz.domain.QuizService;
import com.quizcode.module.quiz.domain.QuestionPort;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuizValidator quizValidator;
    private final QuestionPort questionPort;

    public QuizServiceImpl(QuizRepository quizRepository, QuizValidator quizValidator, @Lazy QuestionPort questionPort) {
        this.quizRepository = quizRepository;
        this.quizValidator = quizValidator;
        this.questionPort = questionPort;
    }

    @Override
    public String create(Quiz quiz) {
        quizValidator.validateQuizToCreate(quiz);
        return quizRepository.create(quiz);
    }

    @Override
    public void update(Quiz quiz) {
        quizValidator.validateQuizToUpdate(quiz);
        quizRepository.update(quiz);
    }

    @Override
    public Quiz findById(String id, String ownerId) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new NotFoundExceptionCustom("El cuestionario no existe"));
        quizValidator.validateQuizToFindById(quiz, ownerId);
        return quiz;
    }

    @Override
    public List<Quiz> findByOwnerId(String ownerId) {
        return quizRepository.findByOwnerId(ownerId);
    }

    @Override
    public void updateStatus(String id, String ownerId, QuizStatus status) {
        quizValidator.validateQuizToUpdateStatus(id, status, ownerId);
        quizRepository.updateStatus(id, status);
    }

    @Override
    public void delete(String id, String ownerId) {
        quizValidator.validateQuizToDelete(id, ownerId);
        questionPort.deleteQuestionsByQuizId(id);
        quizRepository.delete(id);
    }
}