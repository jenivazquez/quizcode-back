package com.quizcode.module.quiz.application;

import com.quizcode.error.exception.NotFoundExceptionCustom;
import com.quizcode.module.quiz.application.validation.QuizValidator;
import com.quizcode.module.quiz.domain.entity.Quiz;
import com.quizcode.module.quiz.domain.entity.QuizStatus;
import com.quizcode.module.quiz.domain.QuizRepository;
import com.quizcode.module.quiz.domain.QuizService;
import com.quizcode.module.quiz.domain.QuizToQuestionPort;
import com.quizcode.module.quiz.domain.QuizToRoomPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuizValidator quizValidator;
    private final QuizToQuestionPort questionPort;
    private final QuizToRoomPort roomPort;

    public QuizServiceImpl(QuizRepository quizRepository, QuizValidator quizValidator, QuizToQuestionPort questionPort, QuizToRoomPort roomPort) {
        this.quizRepository = quizRepository;
        this.quizValidator = quizValidator;
        this.questionPort = questionPort;
        this.roomPort = roomPort;
    }

    @Override
    public String create(Quiz quiz) {
        quizValidator.validateToCreate(quiz);
        return quizRepository.create(quiz);
    }

    @Override
    public void update(Quiz quiz) {
        quizValidator.validateToUpdate(quiz);
        quizRepository.update(quiz);
    }

    @Override
    public Quiz findById(String id, String ownerId) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new NotFoundExceptionCustom("El cuestionario no existe"));
        quizValidator.validateToFindById(quiz, ownerId);
        return quiz;
    }

    @Override
    public Quiz findByIdToAnswer(String id) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new NotFoundExceptionCustom("El cuestionario no existe"));
        quizValidator.validateToFindByIdToAnswer(quiz);
        return quiz;
    }

    @Override
    public List<Quiz> findByOwnerId(String ownerId) {
        return quizRepository.findByOwnerId(ownerId);
    }

    @Override
    public void updateStatus(String id, String ownerId, QuizStatus status) {
        quizValidator.validateToUpdateStatus(id, status, ownerId);
        quizRepository.updateStatus(id, status);
    }

    @Override
    public void delete(String id, String ownerId) {
        quizValidator.validateToDelete(id, ownerId);
        roomPort.deleteRoomsByQuizId(id);
        questionPort.deleteQuestionsByQuizId(id);
        quizRepository.delete(id);
    }
}