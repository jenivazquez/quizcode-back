package com.quizcode.module.authorization.domain;

import com.quizcode.module.authorization.domain.entity.Auth;

public interface AuthService {
    Auth getToken(String email, String password);
}
