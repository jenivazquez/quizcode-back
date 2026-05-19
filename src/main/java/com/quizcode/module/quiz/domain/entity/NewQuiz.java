package com.quizcode.module.quiz.domain.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import com.quizcode.shared.ValidatorUtil;

@Getter
public class NewQuiz {

    private final Quiz quiz;

    public NewQuiz(String ownerId, String title, String description, Boolean hasLimit, Integer limitMinutes) {
        validate(ownerId, title, description, hasLimit);
        this.quiz = new Quiz(null, ownerId, title, description, hasLimit, limitMinutes, QuizStatus.CREATED, Instant.now(), Instant.now());
    }

    private void validate(String ownerId, String title, String description, Boolean hasLimit) {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("propietario", ownerId);
        fields.put("título", title);
        fields.put("descripción", description);
        fields.put("tiene límite", hasLimit);
        ValidatorUtil.validateFieldsNotNull(fields);
    }
}
