package com.quizcode.module.quiz.domain;

public interface QuestionPort {
    void deleteQuestionsByQuizId(String quizId);
    boolean hasQuestions(String quizId);
}
