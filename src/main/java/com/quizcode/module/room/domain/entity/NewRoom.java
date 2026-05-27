package com.quizcode.module.room.domain.entity;

import com.quizcode.shared.ValidatorUtil;
import lombok.Getter;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class NewRoom {

    private final Room room;

    public NewRoom(String name, String description, String quizId) {
        validate(name, description, quizId);
        this.room = new Room(null, name, description, null, RoomStatus.CREATED, quizId, Instant.now(), null, null, false);
    }

    private void validate(String name, String description, String quizId) {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("nombre", name);
        fields.put("descripción", description);
        fields.put("identificador del cuestionario", quizId);
        ValidatorUtil.validateFieldsNotNull(fields);
    }
}
