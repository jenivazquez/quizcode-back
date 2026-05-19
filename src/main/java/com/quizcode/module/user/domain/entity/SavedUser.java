package com.quizcode.module.user.domain.entity;

import lombok.Getter;

import java.util.Arrays;

import com.quizcode.shared.ValidatorUtil;

@Getter
public class SavedUser {

    private final User user;

    public SavedUser(String id, String email, String password, String name, String surname1, String surname2, Boolean active) {
        validate(id, email, name, surname1, surname2, active);
        this.user = new User(id, email, password, name, surname1, surname2, active);
    }

    private void validate(String id, String email, String name, String surname1, String surname2, Boolean active) {
        ValidatorUtil.validateFieldsNotNullAsGroup(Arrays.asList(id, email, name, surname1, surname2, active));
    }
}
