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

    public void validateToCreate(User newUser) {
        this.checkEmailDuplicated(newUser.getEmail());
    }

    public void validateToUpdate(User newUser) {
        User savedUser = userRepository.findById(newUser.getId()).orElseThrow(() -> new NotFoundExceptionCustom("El usuario no existe"));
        this.checkUserActive(savedUser);
    }

    public void validateToUpdateStatus(String id, Boolean active) {
        if (active==null) throw new InvalidDataExceptionCustom("El campo activo es obligatorio");
        User savedUser = userRepository.findById(id).orElseThrow(() -> new NotFoundExceptionCustom("El usuario no existe"));
        if (Objects.equals(savedUser.getActive(), active)) throw new InvalidDataExceptionCustom("Estas intentando activar/desactivar un usuario que ya está en ese estado");
    }

    public void validateCredentials(User savedUser, String password) {
        this.checkUserActive(savedUser);
        this.checkUserPassword(password, savedUser);
    }

    private void checkEmailDuplicated(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new InvalidDataExceptionCustom("Ya existe un usuario con ese email");
        }
    }

    private void checkUserPassword(String password, User savedUser) {
        if (!passwordHasher.matches(password, savedUser.getPassword())) {
            throw new InvalidCredentialsExceptionCustom("Las credenciales son incorrectas.");
        }
    }

    public void checkUserActive(User savedUser) {
        if (!Boolean.TRUE.equals(savedUser.getActive())) {
            throw new InvalidStatusExceptionCustom("El usuario está inactivo.");
        }
    }
}
