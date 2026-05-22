package com.quizcode.module.quiz.domain;

public interface QuizToQuestionPort {
    void deleteQuestionsByQuizId(String quizId);
    boolean hasQuestions(String quizId);
}