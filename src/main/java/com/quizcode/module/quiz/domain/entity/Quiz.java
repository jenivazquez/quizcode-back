package com.quizcode.module.quiz.domain.entity;

import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.shared.Util;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Quiz {

    private final String id;
    private final String ownerId;
    private final String title;
    private final String description;
    private final Boolean hasLimit;
    private final Integer limitMinutes;
    private final QuizStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;

    protected Quiz(String id, String ownerId, String title, String description, Boolean hasLimit, Integer limitMinutes, QuizStatus status, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.title = title;
        this.description = description;
        this.hasLimit = hasLimit;
        this.limitMinutes = limitMinutes;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        validate();
    }

    private void validate() {
        this.validateTitle();
        this.validateDescription();
        this.validateTimeLimit();
        this.validateLimitMinutes();
    }

    private void validateTitle() {
        if (!Util.isNull(title) && title.length() > 50) {
            throw new InvalidDataExceptionCustom("El título no puede superar los 50 caracteres");
        }
    }

    private void validateDescription() {
        if (!Util.isNull(description) && description.length() > 500) {
            throw new InvalidDataExceptionCustom("La descripción no puede superar los 500 caracteres");
        }
    }

    private void validateLimitMinutes() {
        if (limitMinutes != null && limitMinutes <= 0) {
            throw new InvalidDataExceptionCustom("El límite de tiempo debe ser mayor que 0");
        }
    }

    private void validateTimeLimit() {
        if (Boolean.TRUE.equals(hasLimit) && limitMinutes == null) {
            throw new InvalidDataExceptionCustom("Cuando el cuestionario tiene límite de respuesta, el campo tiempo límite es obligatorio");
        }
        if (Boolean.FALSE.equals(hasLimit) && limitMinutes != null) {
            throw new InvalidDataExceptionCustom("Cuando el cuestionario no tiene límite de respuesta, el campo tiempo límite no debe tener valor");
        }
    }

    public boolean isEditable() {
        return status == QuizStatus.CREATED;
    }

    public boolean isRoomAllowed() {
        return status == QuizStatus.PUBLISHED || status == QuizStatus.LOCKED;
    }

    public boolean isAnswerAllowed() {
        return status == QuizStatus.LOCKED;
    }
}
