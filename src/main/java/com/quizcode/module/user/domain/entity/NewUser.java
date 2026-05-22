package com.quizcode.module.user.domain.entity;

import com.quizcode.error.exception.AutoGenerationExceptionCustom;
import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.module.user.domain.PasswordHasher;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

import com.quizcode.shared.ValidatorUtil;

@Getter
public class NewUser {

    private final User user;

    public NewUser(String email, String password, String name, String surname1, String surname2, PasswordHasher hasher) {
        validate(email, password, name, surname1, surname2);
        this.user = new User(null, email, hashPassword(password, hasher), name, surname1, surname2, true);
    }

    private void validate(String email, String password, String name, String surname1, String surname2) {
        validateRequiredFields(email, password, name, surname1, surname2);
        validateFormatPassword(password);
    }

    private void validateRequiredFields(String email, String password, String name, String surname1, String surname2) {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("email", email);
        fields.put("contraseña", password);
        fields.put("nombre", name);
        fields.put("primer apellido", surname1);
        fields.put("segundo apellido", surname2);
        ValidatorUtil.validateFieldsNotNull(fields);
    }

    private String hashPassword(String password, PasswordHasher hasher) {
        try {
            return hasher.hash(password);
        } catch (Exception e) {
            throw new AutoGenerationExceptionCustom("Error al procesar la contraseña");
        }
    }

    private void validateFormatPassword(String password) {
        if (password.length() < 6)
            throw new InvalidDataExceptionCustom("La contraseña debe tener al menos 6 caracteres");
        if (password.chars().noneMatch(Character::isLetter) || password.chars().noneMatch(Character::isDigit))
            throw new InvalidDataExceptionCustom("La contraseña debe contener al menos una letra y un número");
    }
}
