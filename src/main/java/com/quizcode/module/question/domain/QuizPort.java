package com.quizcode.module.question.domain;

public interface QuizPort {
    void checkQuizAccess(String quizId, String ownerId);
    boolean isQuizEditable(String quizId, String ownerId);
}
