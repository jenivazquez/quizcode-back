package com.quizcode.module.authorization.domain;

import java.util.Date;

public interface TokenProvider {
    String generateToken(String userId);
    Date extractExpiration(String token);
}