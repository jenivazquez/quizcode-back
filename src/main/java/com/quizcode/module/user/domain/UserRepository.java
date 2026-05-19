package com.quizcode.module.user.domain;

import com.quizcode.module.user.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    void create(User user) ;
    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);
    void update(User user);
    void updateStatus(String id, Boolean active);
}
