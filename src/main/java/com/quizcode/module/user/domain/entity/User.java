package com.quizcode.module.user.domain.entity;

import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.shared.Util;
import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public class User {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-zÀ-ÿ '-]+$");

    private final String id;
    private final String email;
    private final String password;
    private final String name;
    private final String surname1;
    private final String surname2;
    private final Boolean active;

    protected User(String id, String email, String password, String name, String surname1, String surname2, Boolean active) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname1 = surname1;
        this.surname2 = surname2;
        this.active = active;
        validate();
    }

    private void validate() {
        validateEmail(this.email);
        validateNameField(this.name, "nombre");
        validateNameField(this.surname1, "apellido 1");
        validateNameField(this.surname2, "apellido 2");
    }

    private void validateEmail(String email) {
        if (!Util.isNull(email) && !EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidDataExceptionCustom("El email no tiene un formato válido");
        }
    }

    private void validateNameField(String value, String fieldName) {
        if (!Util.isNull(value) && !NAME_PATTERN.matcher(value).matches()) {
            throw new InvalidDataExceptionCustom("El campo " + fieldName + " contiene caracteres inválidos. Solo se permiten letras, espacios, guiones y acentos");
        }
    }
}