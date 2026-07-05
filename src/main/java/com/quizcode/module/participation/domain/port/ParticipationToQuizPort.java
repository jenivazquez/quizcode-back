package com.quizcode.module.participation.domain.port;

public interface ParticipationToQuizPort {
    void checkQuizExistByOwner(String quizId, String ownerId);
    Integer getTimeLimit(String quizId);
}