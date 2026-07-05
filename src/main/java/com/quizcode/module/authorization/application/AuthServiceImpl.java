package com.quizcode.module.authorization.application;

import com.quizcode.error.exception.AutoGenerationExceptionCustom;
import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.module.authorization.domain.AuthService;
import com.quizcode.module.authorization.domain.TokenProvider;
import com.quizcode.module.authorization.domain.port.AuthToUserPort;
import com.quizcode.module.authorization.domain.entity.Auth;
import com.quizcode.shared.Util;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthToUserPort userPort;
    private final TokenProvider tokenProvider;

    public AuthServiceImpl(TokenProvider tokenProvider, AuthToUserPort userPort) {
        this.tokenProvider = tokenProvider;
        this.userPort = userPort;
    }

    @Override
    public Auth getToken(String email, String password) {
        if(Util.isNull(email) || Util.isNull(password)) throw new InvalidDataExceptionCustom("Los campos email/contraseña son obligatorios");
        String userId = this.userPort.verifyLoginAndGetId(email, password);
        String token = generateToken(userId);
        return Auth.builder()
                .token(token)
                .validUntil(tokenProvider.extractExpiration(token))
                .userId(userId)
                .build();
    }

    private String generateToken(String userId) {
        try {
            return tokenProvider.generateToken(userId);
        } catch (Exception e) {
            throw new AutoGenerationExceptionCustom("Error al generar el token de autenticación");
        }
    }
}