package com.quizcode.module.question.domain;

import com.quizcode.module.question.domain.entity.question.Question;
import java.util.List;

public interface QuestionAdapterService {
    void deleteByQuizId(String quizId);
    boolean hasQuestions(String quizId);
    List<Question> findByQuizId(String quizId);
}