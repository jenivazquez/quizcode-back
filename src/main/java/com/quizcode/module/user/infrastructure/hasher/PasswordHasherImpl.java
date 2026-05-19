package com.quizcode.module.user.infrastructure.hasher;

import com.quizcode.module.user.domain.PasswordHasher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHasherImpl implements PasswordHasher {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String hash(String plain) {
        return encoder.encode(plain);
    }

    @Override
    public boolean matches(String plain, String hashed) {
        return encoder.matches(plain, hashed);
    }
}