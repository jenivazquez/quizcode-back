package com.quizcode.module.authorization.api.mapper;

import com.quizcode.module.authorization.api.dto.AuthResponse;
import com.quizcode.module.authorization.domain.entity.Auth;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    AuthResponse authToAuthResponse(Auth auth);
}
