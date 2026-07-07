package com.quizcode.module.authorization.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class PartAuth {
    private String token;
    private Date validUntil;
    private String partId;
}