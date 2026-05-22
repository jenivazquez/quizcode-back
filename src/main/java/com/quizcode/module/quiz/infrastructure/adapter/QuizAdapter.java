package com.quizcode.module.quiz.infrastructure.adapter;

import com.quizcode.module.question.domain.QuestionToQuizPort;
import com.quizcode.module.quiz.domain.QuizService;
import com.quizcode.module.quiz.domain.entity.Quiz;
import com.quizcode.module.room.domain.RoomToQuizPort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuizAdapter implements QuestionToQuizPort, RoomToQuizPort {

    private final QuizService quizService;

    public QuizAdapter(QuizService quizService) {
        this.quizService = quizService;
    }

    @Override
    public void checkQuizExistByOwner(String quizId, String ownerId) {
        quizService.findById(quizId, ownerId);
    }

    @Override
    public boolean isQuizEditable(String quizId, String ownerId) {
        return quizService.findById(quizId, ownerId).isEditable();
    }

    @Override
    public boolean isRoomAllowedByQuiz(String quizId, String ownerId) {
        return quizService.findById(quizId, ownerId).isRoomAllowed();
    }

    @Override
    public void lockQuizIfHasRooms(String quizId) {
        quizService.lockQuizIfHasRooms(quizId);
    }

    @Override
    public void unlockQuizIfNoRooms(String quizId) {
        quizService.unlockQuizIfNoRooms(quizId);
    }

    @Override
    public List<String> findQuizzesByOwner(String ownerId) {
        return quizService.findByOwnerId(ownerId).stream()
                .map(Quiz::getId)
                .toList();
    }
}