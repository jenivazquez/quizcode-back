package com.quizcode.module.quiz.domain.entity;

import com.quizcode.shared.ValidatorUtil;
import lombok.Getter;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class EditQuiz {

    private final Quiz quiz;

    public EditQuiz(String id, String ownerId, String title, String description, Boolean hasLimit, Integer limitMinutes) {
        validate(id, ownerId, title, description, hasLimit);
        this.quiz = new Quiz(id, ownerId, title, description, hasLimit, limitMinutes, null, null, Instant.now());
    }

    private void validate(String id, String ownerId, String title, String description, Boolean hasLimit) {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("identificador del cuestionario", id);
        fields.put("propietario", ownerId);
        fields.put("título", title);
        fields.put("descripción", description);
        fields.put("tiene límite", hasLimit);
        ValidatorUtil.validateFieldsNotNull(fields);
    }
}
