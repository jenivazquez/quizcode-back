package com.quizcode.module.quiz.application;

import com.quizcode.error.exception.NotFoundExceptionCustom;
import com.quizcode.module.quiz.application.validation.QuizValidator;
import com.quizcode.module.quiz.domain.QuizAdapterService;
import com.quizcode.module.quiz.domain.QuizRepository;
import com.quizcode.module.quiz.domain.QuizToRoomPort;
import com.quizcode.module.quiz.domain.entity.Quiz;
import com.quizcode.module.quiz.domain.entity.QuizStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizAdapterServiceImpl implements QuizAdapterService {

    private final QuizRepository quizRepository;
    private final QuizValidator quizValidator;
    private final QuizToRoomPort roomPort;

    public QuizAdapterServiceImpl(QuizRepository quizRepository, QuizValidator quizValidator, QuizToRoomPort roomPort) {
        this.quizRepository = quizRepository;
        this.quizValidator = quizValidator;
        this.roomPort = roomPort;
    }

    @Override
    public Quiz findByIdAndOwnerId(String id, String ownerId) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new NotFoundExceptionCustom("El cuestionario no existe"));
        quizValidator.validateToFindById(quiz, ownerId);
        return quiz;
    }

    @Override
    public Quiz findById(String id) {
        return quizRepository.findById(id).orElseThrow(() -> new NotFoundExceptionCustom("El cuestionario no existe"));
    }

    @Override
    public List<Quiz> findByOwnerId(String ownerId) {
        return quizRepository.findByOwnerId(ownerId);
    }

    @Override
    public void lockQuizIfHasRooms(String id) {
        quizRepository.findById(id)
                .filter(quiz -> quiz.getStatus() != QuizStatus.LOCKED)
                .filter(quiz -> roomPort.hasRooms(id))
                .ifPresent(quiz -> quizRepository.updateStatus(id, QuizStatus.LOCKED));
    }

    @Override
    public void unlockQuizIfNoRooms(String id) {
        quizRepository.findById(id)
                .filter(quiz -> quiz.getStatus() == QuizStatus.LOCKED)
                .filter(quiz -> !roomPort.hasRooms(id))
                .ifPresent(quiz -> quizRepository.updateStatus(id, QuizStatus.PUBLISHED));
    }
}