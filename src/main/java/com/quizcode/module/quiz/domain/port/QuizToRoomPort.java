package com.quizcode.module.quiz.domain.port;

public interface QuizToRoomPort {
    boolean hasRooms(String quizId);
    void deleteRoomsAndPartsByQuizId(String quizId);
}