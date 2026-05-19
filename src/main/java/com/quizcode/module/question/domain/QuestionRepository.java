package com.quizcode.module.question.domain;

import com.quizcode.module.question.domain.entity.question.Question;

import java.util.List;

public interface QuestionRepository {
    String create(Question question);
    List<Question> findByQuizId(String quizId);
    boolean existsByQuizIdAndId(String quizId, String id);
    boolean existsByQuizIdAndOrder(String quizId, Integer order);
    boolean existsByQuizIdAndOrderAndIdNot(String quizId, Integer order, String id);
    void update(Question question);
    void delete(String id);
    void deleteByQuizId(String quizId);
}
