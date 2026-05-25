package com.quizcode.module.question.domain;

import com.quizcode.module.question.domain.entity.question.Question;

import java.util.List;

public interface QuestionService {
    String create(String ownerId, Question question);
    List<Question> findByQuizId(String ownerId, String quizId);
    List<Question> findByQuizIdToAnswer(String quizId);
    void update(String ownerId, Question question);
    void delete(String ownerId, String quizId, String id);
}
