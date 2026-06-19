package com.quizcode.module.authorization.infrastructure.config;

import com.quizcode.module.authorization.infrastructure.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.Instant;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/v1/user").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/token").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/room/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/room/code/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/quiz/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/quiz/*/question").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/quiz/*/question/review").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/room/*/participation").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/room/*/participation/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/room/*/participation").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/room/*/participation/*").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/api/v1/room/*/participation/*").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) -> {
                    res.setStatus(HttpStatus.UNAUTHORIZED.value());
                    res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    res.getWriter().write("""
                            {"code":"Unauthorized","message":"No hay sesion autenticada","timestamp":"%s"}"""
                            .formatted(Instant.now()));
                })
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
