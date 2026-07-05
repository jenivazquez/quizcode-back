package com.quizcode.module.user.domain.entity;

import com.quizcode.error.exception.AutoGenerationExceptionCustom;
import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.module.user.domain.PasswordHasher;
import com.quizcode.shared.Util;
import com.quizcode.shared.ValidatorUtil;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class EditUser {

    private final User user;

    public EditUser(String id, String password, String name, String surname1, String surname2, PasswordHasher hasher) {
        validate(id, password, name, surname1, surname2);
        this.user = new User(id, null, hashPassword(password, hasher), name, surname1, surname2, null);
    }

    private void validate(String id, String password, String name, String surname1, String surname2) {
        validateRequiredFields(id, name, surname1, surname2);
        validateFormatPassword(password);
    }

    private void validateRequiredFields(String id, String name, String surname1, String surname2) {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("identificador del usuario", id);
        fields.put("nombre", name);
        fields.put("primer apellido", surname1);
        fields.put("segundo apellido", surname2);
        ValidatorUtil.validateFieldsNotNull(fields);
    }

    private String hashPassword(String password, PasswordHasher hasher) {
        try {
            return Util.isNull(password) ? null : hasher.hash(password);
        } catch (Exception e) {
            throw new AutoGenerationExceptionCustom("Error al procesar la contraseña");
        }
    }

    private void validateFormatPassword(String password) {
        if(!Util.isNull(password)) {
            if (password.length() < 6)
                throw new InvalidDataExceptionCustom("La contraseña debe tener al menos 6 caracteres");
            if (password.chars().noneMatch(Character::isLetter) || password.chars().noneMatch(Character::isDigit))
                throw new InvalidDataExceptionCustom("La contraseña debe contener al menos una letra y un número");
        }
    }
}
