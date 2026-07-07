package com.quizcode.module.authorization.domain;

import com.quizcode.module.authorization.domain.entity.Role;

import java.util.Date;

public interface TokenProvider {
    String generateToken(String subject, Role type);
    Date extractExpiration(String token);
}