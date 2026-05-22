package com.quizcode.module.room.domain.entity;

import com.quizcode.shared.ValidatorUtil;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class EditRoom {

    private final Room room;

    public EditRoom(String id, String name, String description, String quizId) {
        validate(id, name, description, quizId);
        this.room = new Room(id, name, description, null, null, quizId, null, null, null);
    }

    private void validate(String id, String name, String description, String quizId) {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("identificador de la sala", id);
        fields.put("nombre", name);
        fields.put("descripción", description);
        fields.put("identificador del cuestionario", quizId);
        ValidatorUtil.validateFieldsNotNull(fields);
    }
}
