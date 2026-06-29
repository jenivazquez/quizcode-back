package com.quizcode.module.quiz.domain.port;

public interface QuizToQuestionPort {
    void deleteQuestionsByQuizId(String quizId);
    boolean hasQuestions(String quizId);
}