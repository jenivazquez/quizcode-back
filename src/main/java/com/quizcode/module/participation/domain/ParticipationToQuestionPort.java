package com.quizcode.module.participation.domain;

import com.quizcode.module.participation.domain.entity.question.QuestionSummary;

import java.util.List;

public interface ParticipationToQuestionPort {
    boolean questionBelongsToQuiz(String questionId, String quizId);
    List<QuestionSummary> findByQuizId(String quizId);
}
