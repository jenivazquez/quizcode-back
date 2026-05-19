package com.quizcode.module.quiz.domain.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.Arrays;

import com.quizcode.shared.ValidatorUtil;

@Getter
public class SavedQuiz {

    private final Quiz quiz;

    public SavedQuiz(String id, String ownerId, String title, String description, Boolean hasLimit, Integer limitMinutes, QuizStatus status, Instant createdAt, Instant updatedAt) {
        validate(id, ownerId, title, description, hasLimit, status, createdAt, updatedAt);
        this.quiz = new Quiz(id, ownerId, title, description, hasLimit, limitMinutes, status, createdAt, updatedAt);
    }

    private void validate(String id, String ownerId, String title, String description, Boolean hasLimit, QuizStatus status, Instant createdAt, Instant updatedAt) {
        ValidatorUtil.validateFieldsNotNullAsGroup(Arrays.asList(id, ownerId, title, description, hasLimit, status, createdAt, updatedAt));
    }
}
