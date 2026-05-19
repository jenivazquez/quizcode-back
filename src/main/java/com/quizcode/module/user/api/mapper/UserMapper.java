package com.quizcode.module.user.api.mapper;

import com.quizcode.module.user.api.dto.CreateUserRequest;
import com.quizcode.module.user.api.dto.UpdateUserRequest;
import com.quizcode.module.user.api.dto.UserResponse;
import com.quizcode.module.user.domain.PasswordHasher;
import com.quizcode.module.user.domain.entity.EditUser;
import com.quizcode.module.user.domain.entity.NewUser;
import com.quizcode.module.user.domain.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordHasher passwordHasher;

    public UserMapper(PasswordHasher passwordHasher) {
        this.passwordHasher = passwordHasher;
    }

    public User createUserRequestToUser(CreateUserRequest request) {
        return new NewUser(request.getEmail(), request.getPassword(), request.getName(), request.getSurname1(), request.getSurname2(), passwordHasher).getUser();
    }

    public User updateUserRequestToUser(String id, UpdateUserRequest request) {
        return new EditUser(id, request.getPassword(), request.getName(), request.getSurname1(), request.getSurname2(), passwordHasher).getUser();
    }

    public UserResponse userToUserResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getName(), user.getSurname1(), user.getSurname2());
    }
}