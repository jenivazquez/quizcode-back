package com.quizcode.module.room.domain.entity;

import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.shared.Util;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Room {

    private final String id;
    private final String name;
    private final String description;
    private final String code;
    private final RoomStatus status;
    private final String quizId;
    private final Instant createdAt;
    private final Instant startedAt;
    private final Instant finishedAt;
    private final Boolean reviewed;

    protected Room(String id, String name, String description, String code, RoomStatus status, String quizId, Instant createdAt, Instant startedAt, Instant finishedAt, Boolean reviewed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.code = code;
        this.status = status;
        this.quizId = quizId;
        this.createdAt = createdAt;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.reviewed = reviewed;
        validate();
    }

    public boolean isEditable() {
        return status == RoomStatus.CREATED;
    }

    private void validate() {
        validateName();
        validateDescription();
    }

    private void validateName() {
        if (!Util.isNull(name) && name.length() > 100) {
            throw new InvalidDataExceptionCustom("El nombre de la sala no puede superar los 100 caracteres");
        }
    }

    private void validateDescription() {
        if (!Util.isNull(description) && description.length() > 500) {
            throw new InvalidDataExceptionCustom("La descripción de la sala no puede superar los 500 caracteres");
        }
    }
}