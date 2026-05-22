package com.quizcode.module.question.domain;

public interface QuestionToQuizPort {
    void checkQuizExistByOwner(String quizId, String ownerId);
    boolean isQuizEditable(String quizId, String ownerId);
}