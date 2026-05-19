package com.quizcode.module.authorization.api;

import com.quizcode.module.authorization.api.dto.AuthRequest;
import com.quizcode.module.authorization.api.dto.AuthResponse;
import com.quizcode.module.authorization.api.mapper.AuthMapper;
import com.quizcode.module.authorization.domain.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class AuthController {

    private final AuthService authorizationService;
    private final AuthMapper authMapper;

    public AuthController(AuthService authorizationService, AuthMapper authMapper){
        this.authorizationService = authorizationService;
        this.authMapper = authMapper;
    }

    @PostMapping(path = "/token")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse getToken(@RequestBody AuthRequest authRequest){
        return authMapper.authToAuthResponse(this.authorizationService.getToken(authRequest.getEmail(), authRequest.getPassword()));
    }
}
