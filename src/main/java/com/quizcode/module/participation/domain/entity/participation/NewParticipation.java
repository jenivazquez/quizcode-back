package com.quizcode.module.participation.domain.entity.participation;

import com.quizcode.error.exception.AutoGenerationExceptionCustom;
import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.module.participation.domain.entity.status.ParticipationStatus;
import com.quizcode.module.user.domain.PasswordHasher;
import com.quizcode.shared.ValidatorUtil;
import lombok.Getter;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class NewParticipation {

    private final Participation participation;

    public NewParticipation(String roomId, String username, String password, PasswordHasher passwordHasher) {
        validate(roomId, username, password);
        this.participation = new Participation(null, roomId, username, hashPassword(password, passwordHasher), ParticipationStatus.STARTED, null, Instant.now(), null, null, null, List.of());
    }

    private void validate(String roomId, String username, String password) {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("identificador de la sala", roomId);
        fields.put("nombre de usuario", username);
        fields.put("contraseña", password);
        ValidatorUtil.validateFieldsNotNull(fields);
        validateFormatPassword(password);
    }

    private void validateFormatPassword(String password) {
        if (password.length() < 6)
            throw new InvalidDataExceptionCustom("La contraseña debe tener al menos 6 caracteres");
        if (password.chars().noneMatch(Character::isLetter) || password.chars().noneMatch(Character::isDigit))
            throw new InvalidDataExceptionCustom("La contraseña debe contener al menos una letra y un número");
    }

    private String hashPassword(String password, PasswordHasher passwordHasher) {
        try {
            return passwordHasher.hash(password);
        } catch (Exception e) {
            throw new AutoGenerationExceptionCustom("Error al procesar la contraseña");
        }
    }
}
