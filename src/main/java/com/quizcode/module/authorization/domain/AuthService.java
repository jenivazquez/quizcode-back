package com.quizcode.module.authorization.domain;

import com.quizcode.module.authorization.domain.entity.Auth;
import com.quizcode.module.authorization.domain.entity.PartAuth;

public interface AuthService {
    Auth getToken(String email, String password);
    PartAuth generatePartToken(String partId);
}
