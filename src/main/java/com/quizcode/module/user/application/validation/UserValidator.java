package com.quizcode.module.user.application.validation;

import com.quizcode.error.exception.InvalidCredentialsExceptionCustom;
import com.quizcode.error.exception.InvalidDataExceptionCustom;
import com.quizcode.error.exception.InvalidStatusExceptionCustom;
import com.quizcode.error.exception.NotFoundExceptionCustom;
import com.quizcode.module.user.domain.PasswordHasher;
import com.quizcode.module.user.domain.UserRepository;
import com.quizcode.module.user.domain.entity.User;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserValidator {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UserValidator(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public void validateUserToCreate(User user) {
        this.checkEmailDuplicated(user.getEmail());
    }

    public void validateUserToUpdate(User user) {
        User savedUser = userRepository.findById(user.getId()).orElseThrow(() -> new NotFoundExceptionCustom("El usuario no existe"));
        this.checkUserActive(savedUser);
    }

    public void validateUserToUpdateStatus(String id, Boolean active) {
        if (active==null) throw new InvalidDataExceptionCustom("El campo activo es obligatorio");
        User savedUser = userRepository.findById(id).orElseThrow(() -> new NotFoundExceptionCustom("El usuario no existe"));
        if (Objects.equals(savedUser.getActive(), active)) throw new InvalidDataExceptionCustom("Estas intentando activar/desactivar un usuario que ya está en ese estado");
    }

    public void validateCredentialsUser(User user, String password) {
        this.checkUserActive(user);
        this.checkUserPassword(password, user);
    }

    private void checkEmailDuplicated(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new InvalidDataExceptionCustom("Ya existe un usuario con ese email");
        }
    }

    private void checkUserPassword(String password, User user) {
        if (!passwordHasher.matches(password, user.getPassword())) {
            throw new InvalidCredentialsExceptionCustom("Las credenciales son incorrectas.");
        }
    }

    public void checkUserActive(User user) {
        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new InvalidStatusExceptionCustom("El usuario está inactivo.");
        }
    }
}
