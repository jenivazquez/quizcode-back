package com.quizcode.module.authorization.application;

import com.quizcode.error.exception.AutoGenerationExceptionCustom;
import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.module.authorization.domain.AuthService;
import com.quizcode.module.authorization.domain.TokenProvider;
import com.quizcode.module.authorization.domain.port.AuthToUserPort;
import com.quizcode.module.authorization.domain.entity.Auth;
import com.quizcode.module.authorization.domain.entity.PartAuth;
import com.quizcode.module.authorization.domain.entity.Role;
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

    @Override
    public PartAuth generatePartToken(String partId) {
        String token = buildPartToken(partId);
        return PartAuth.builder()
                .token(token)
                .validUntil(tokenProvider.extractExpiration(token))
                .partId(partId)
                .build();
    }

    private String buildPartToken(String partId) {
        try {
            return tokenProvider.generateToken(partId, Role.PARTICIPATION);
        } catch (Exception e) {
            throw new AutoGenerationExceptionCustom("Error al generar el token de participación");
        }
    }

    private String generateToken(String userId) {
        try {
            return tokenProvider.generateToken(userId, Role.USER);
        } catch (Exception e) {
            throw new AutoGenerationExceptionCustom("Error al generar el token de autenticación");
        }
    }
}