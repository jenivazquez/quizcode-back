package com.quizcode.module.authorization.infrastructure.jwt;

import com.quizcode.module.authorization.domain.TokenProvider;
import com.quizcode.module.authorization.domain.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider implements TokenProvider {

    private static final String ROLE_CLAIM = "role";

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey key;
    private final long expirationMillis = 3600_000; // 1 hora

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public String generateToken(String id, Role role) {
        return Jwts.builder()
                .subject(id)
                .claim(ROLE_CLAIM, role.name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(key)
                .compact();
    }

    public String extractId(String token) {
        return parseToken(token).getSubject();
    }

    public Role extractRole(String token) {
        return Role.valueOf(parseToken(token).get(ROLE_CLAIM, String.class));
    }

    @Override
    public Date extractExpiration(String token) {
        return parseToken(token).getExpiration();
    }

    public boolean isValidToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}