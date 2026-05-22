package com.quizcode.module.user.infrastructure.adapter;

import com.quizcode.module.authorization.domain.AuthToUserPort;
import com.quizcode.module.user.domain.UserAdapterService;
import org.springframework.stereotype.Component;

@Component
public class UserAdapter implements AuthToUserPort {

    private final UserAdapterService userAdapterService;

    public UserAdapter(UserAdapterService userAdapterService) {
        this.userAdapterService = userAdapterService;
    }

    @Override
    public String verifyLoginAndGetId(String email, String password) {
        return userAdapterService.verifyLoginAndGetId(email, password);
    }
}