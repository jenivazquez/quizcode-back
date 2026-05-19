package com.quizcode.module.authorization.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class Auth {
    String token;
    Date validUntil;
    String userId;
}
