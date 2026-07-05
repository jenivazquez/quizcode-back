package com.quizcode.module.user.application;

import com.quizcode.error.exception.InvalidCredentialsExceptionCustom;
import com.quizcode.module.user.application.validation.UserValidator;
import com.quizcode.module.user.domain.UserAdapterService;
import com.quizcode.module.user.domain.UserRepository;
import com.quizcode.module.user.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserAdapterServiceImpl implements UserAdapterService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public UserAdapterServiceImpl(UserRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    @Override
    public String verifyLoginAndGetId(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new InvalidCredentialsExceptionCustom("Las credenciales son incorrectas."));
        userValidator.validateCredentials(user, password);
        return user.getId();
    }
}