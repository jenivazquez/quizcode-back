package com.quizcode.module.room.domain.entity;

import com.quizcode.shared.ValidatorUtil;
import lombok.Getter;

import java.time.Instant;
import java.util.Arrays;

@Getter
public class SavedRoom {

    private final Room room;

    public SavedRoom(String id, String name, String description, String code, RoomStatus status, String quizId, Instant createdAt, Instant startedAt, Instant finishedAt) {
        validate(id, name, description, status, quizId, createdAt);
        this.room = new Room(id, name, description, code, status, quizId, createdAt, startedAt, finishedAt);
    }

    private void validate(String id, String name, String description, RoomStatus status, String quizId, Instant createdAt) {
        ValidatorUtil.validateFieldsNotNullAsGroup(Arrays.asList(id, name, description, status, quizId, createdAt));
    }
}
