package com.quizcode.module.authorization.domain.port;

public interface AuthToUserPort {
    String verifyLoginAndGetId(String email, String password);
}