package com.quizcode.module.participation.domain.port;

import com.quizcode.module.participation.domain.entity.participation.PartToken;

public interface PartToAuthPort {
    PartToken generatePartToken(String partId);
}
