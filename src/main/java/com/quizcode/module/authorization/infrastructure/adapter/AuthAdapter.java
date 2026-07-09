package com.quizcode.module.authorization.infrastructure.adapter;

import com.quizcode.module.authorization.domain.AuthService;
import com.quizcode.module.authorization.domain.entity.PartAuth;
import com.quizcode.module.participation.domain.entity.participation.PartToken;
import com.quizcode.module.participation.domain.port.PartToAuthPort;
import org.springframework.stereotype.Component;

@Component
public class AuthAdapter implements PartToAuthPort {

    private final AuthService authService;

    public AuthAdapter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public PartToken generatePartToken(String partId) {
        PartAuth partAuth = authService.generatePartToken(partId);
        return PartToken.builder()
                .token(partAuth.getToken())
                .validUntil(partAuth.getValidUntil().toInstant())
                .build();
    }
}
