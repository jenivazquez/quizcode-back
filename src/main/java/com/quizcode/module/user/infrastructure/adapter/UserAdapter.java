package com.quizcode.module.user.infrastructure.adapter;

import com.quizcode.module.authorization.domain.UserPort;
import com.quizcode.module.user.domain.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserAdapter implements UserPort {

    private final UserService userService;

    public UserAdapter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String verifyLoginAndGetId(String email, String password) {
        return userService.verifyLoginAndGetId(email, password);
    }
}
