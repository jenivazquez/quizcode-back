package com.quizcode.module.quiz.domain;

public interface QuizToRoomPort {
    boolean hasRooms(String quizId);
    void deleteRoomsByQuizId(String quizId);
}