package com.quizcode.module.quiz.domain;

import com.quizcode.module.quiz.domain.entity.Quiz;
import com.quizcode.module.quiz.domain.entity.QuizStatus;

import java.util.List;

public interface QuizService {
    String create(Quiz quiz);
    Quiz findById(String id, String ownerId);
    Quiz findByIdToAnswer(String id);
    List<Quiz> findByOwnerId(String ownerId);
    void update(Quiz quiz);
    void updateStatus(String id, String ownerId, QuizStatus status);
    void delete(String id, String ownerId);
}