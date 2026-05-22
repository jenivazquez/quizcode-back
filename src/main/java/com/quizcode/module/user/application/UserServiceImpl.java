package com.quizcode.module.user.application;

import com.quizcode.error.exception.InvalidCredentialsExceptionCustom;
import com.quizcode.error.exception.NotFoundExceptionCustom;
import com.quizcode.module.user.application.validation.UserValidator;
import com.quizcode.module.user.domain.UserRepository;
import com.quizcode.module.user.domain.UserService;
import com.quizcode.module.user.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public UserServiceImpl(UserRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    @Override
    public void create(User user) {
        userValidator.validateToCreate(user);
        userRepository.create(user);
    }

    @Override
    public void update(User user) {
        userValidator.validateToUpdate(user);
        userRepository.update(user);
    }

    @Override
    public User findById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundExceptionCustom("El usuario no existe"));
        userValidator.checkUserActive(user);
        return user;
    }

    @Override
    public void updateStatus(String id, Boolean active) {
        userValidator.validateToUpdateStatus(id, active);
        userRepository.updateStatus(id, active);
    }

    @Override
    public String verifyLoginAndGetId(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new InvalidCredentialsExceptionCustom("Las credenciales son incorrectas."));
        userValidator.validateCredentials(user, password);
        return user.getId();
    }
}