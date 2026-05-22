package com.quizcode.module.question.domain;

public interface QuestionAdapterService {
    void deleteByQuizId(String quizId);
    boolean hasQuestions(String quizId);
}