package com.quizcode.module.authorization.application;

import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.module.authorization.domain.AuthService;
import com.quizcode.module.authorization.domain.TokenProvider;
import com.quizcode.module.authorization.domain.UserPort;
import com.quizcode.module.authorization.domain.entity.Auth;
import com.quizcode.shared.Util;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserPort userPort;
    private final TokenProvider tokenProvider;

    public AuthServiceImpl(TokenProvider tokenProvider, UserPort userPort) {
        this.tokenProvider = tokenProvider;
        this.userPort = userPort;
    }

    @Override
    public Auth getToken(String email, String password) {
        if(Util.isNull(email) || Util.isNull(password)) throw new InvalidDataExceptionCustom("Los campos email/contraseña son obligatorios");
        String userId = this.userPort.verifyLoginAndGetId(email, password);
        String token = tokenProvider.generateToken(userId);
        return Auth.builder()
                .token(token)
                .validUntil(tokenProvider.extractExpiration(token))
                .userId(userId)
                .build();
    }
}
