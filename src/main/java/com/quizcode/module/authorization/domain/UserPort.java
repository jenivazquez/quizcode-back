package com.quizcode.module.authorization.domain;

public interface UserPort {
    String verifyLoginAndGetId(String email, String password);
}
