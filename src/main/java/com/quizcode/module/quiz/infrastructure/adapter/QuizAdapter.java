package com.quizcode.module.quiz.infrastructure.adapter;

import com.quizcode.module.participation.domain.ParticipationToQuizPort;
import com.quizcode.module.question.domain.QuestionToQuizPort;
import com.quizcode.module.quiz.domain.QuizAdapterService;
import com.quizcode.module.quiz.domain.entity.Quiz;
import com.quizcode.module.room.domain.RoomToQuizPort;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class QuizAdapter implements QuestionToQuizPort, RoomToQuizPort, ParticipationToQuizPort {

    private final QuizAdapterService quizAdapterService;

    public QuizAdapter(QuizAdapterService quizAdapterService) {
        this.quizAdapterService = quizAdapterService;
    }

    @Override
    public void checkQuizExistByOwner(String quizId, String ownerId) {
        quizAdapterService.findByIdAndOwnerId(quizId, ownerId);
    }

    @Override
    public boolean isQuizEditable(String quizId, String ownerId) {
        return quizAdapterService.findByIdAndOwnerId(quizId, ownerId).isEditable();
    }

    @Override
    public boolean isAnswerAllowedByQuiz(String quizId) {
        return quizAdapterService.findById(quizId).isAnswerAllowed();
    }

    @Override
    public boolean isRoomAllowedByQuiz(String quizId, String ownerId) {
        return quizAdapterService.findByIdAndOwnerId(quizId, ownerId).isRoomAllowed();
    }

    @Override
    public void lockQuizIfHasRooms(String quizId) {
        quizAdapterService.lockQuizIfHasRooms(quizId);
    }

    @Override
    public void unlockQuizIfNoRooms(String quizId) {
        quizAdapterService.unlockQuizIfNoRooms(quizId);
    }

    @Override
    public String findQuizTitleById(String quizId) {
        return quizAdapterService.findById(quizId).getTitle();
    }

    @Override
    public Map<String, String> findQuizTitlesByOwner(String ownerId) {
        return quizAdapterService.findByOwnerId(ownerId).stream()
                .collect(Collectors.toMap(Quiz::getId, Quiz::getTitle));
    }
}