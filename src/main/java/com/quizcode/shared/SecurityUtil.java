package com.quizcode.shared;

import com.quizcode.error.exception.ForbiddenAccessExceptionCustom;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

@UtilityClass
public class SecurityUtil {

    public static void checkAuthorized(String id) {
        if (!Objects.equals(getAuthUserId(), id)) {
            throw new ForbiddenAccessExceptionCustom("No tienes permiso para acceder a este recurso");
        }
    }

    private static String getAuthUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new ForbiddenAccessExceptionCustom("No hay sesión autenticada");
        return (String) auth.getPrincipal();
    }
}
