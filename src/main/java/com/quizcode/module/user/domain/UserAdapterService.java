package com.quizcode.module.user.domain;

public interface UserAdapterService {
    String verifyLoginAndGetId(String email, String password);
}