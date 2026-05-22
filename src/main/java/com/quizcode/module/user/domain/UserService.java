package com.quizcode.module.user.domain;

import com.quizcode.module.user.domain.entity.User;

public interface UserService {
    void create(User user);
    void update(User user);
    User findById(String id);
    void updateStatus(String id, Boolean active);
}
