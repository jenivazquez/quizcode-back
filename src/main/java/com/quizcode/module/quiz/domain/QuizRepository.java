package com.quizcode.module.quiz.domain;

import com.quizcode.module.quiz.domain.entity.Quiz;
import com.quizcode.module.quiz.domain.entity.QuizStatus;

import java.util.List;
import java.util.Optional;

public interface QuizRepository {
    String create(Quiz quiz);
    Optional<Quiz> findById(String id);
    List<Quiz> findByOwnerId(String ownerId);
    boolean existsByOwnerIdAndTitle(String ownerId, String title);
    boolean existsByOwnerIdAndTitleExcludingId(String ownerId, String title, String id);
    void update(Quiz quiz);
    void updateStatus(String id, QuizStatus status);
    void delete(String id);
}
