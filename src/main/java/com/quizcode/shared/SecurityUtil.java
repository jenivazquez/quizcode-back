package com.quizcode.shared;

import com.quizcode.error.exception.ForbiddenAccessExceptionCustom;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

@UtilityClass
public class SecurityUtil {

    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_PARTICIPATION = "ROLE_PARTICIPATION";

    public static void checkAuthorizedUser(String id) {
        checkRole(ROLE_USER);
        if (!Objects.equals(getAuthId(), id)) {
            throw new ForbiddenAccessExceptionCustom("No tienes permiso para acceder a este recurso");
        }
    }

    public static void checkAuthorizedPart(String participationId) {
        checkRole(ROLE_PARTICIPATION);
        if (!Objects.equals(getAuthId(), participationId)) {
            throw new ForbiddenAccessExceptionCustom("No tienes permiso para acceder a este recurso");
        }
    }

    public static String getAuthPartId() {
        checkRole(ROLE_PARTICIPATION);
        return getAuthId();
    }

    private static void checkRole(String expectedRole) {
        boolean hasExpectedRole = getAuth().getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals(expectedRole));
        if (!hasExpectedRole) {
            throw new ForbiddenAccessExceptionCustom("No tienes permiso para acceder a este recurso");
        }
    }

    private static String getAuthId() {
        return (String) getAuth().getPrincipal();
    }

    private static Authentication getAuth() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new ForbiddenAccessExceptionCustom("No hay sesión autenticada");
        return auth;
    }
}
