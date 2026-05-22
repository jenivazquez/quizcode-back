package com.quizcode.module.authorization.domain;

public interface AuthToUserPort {
    String verifyLoginAndGetId(String email, String password);
}