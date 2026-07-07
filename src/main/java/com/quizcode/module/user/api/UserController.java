package com.quizcode.module.user.api;

import com.quizcode.module.user.api.dto.CreateUserRequest;
import com.quizcode.module.user.api.dto.UpdateUserRequest;
import com.quizcode.module.user.api.dto.UpdateUserStatusRequest;
import com.quizcode.module.user.api.dto.UserResponse;
import com.quizcode.module.user.api.mapper.UserMapper;
import com.quizcode.module.user.domain.UserService;
import com.quizcode.shared.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping(path = "/user")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody CreateUserRequest userRequest) {
        userService.create(userMapper.createUserRequestToUser(userRequest));
    }

    @PatchMapping(path = "/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String id, @RequestBody UpdateUserRequest userRequest) {
        SecurityUtil.checkAuthorizedUser(id);
        userService.update(userMapper.updateUserRequestToUser(id, userRequest));
    }

    @GetMapping(path = "/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse findById(@PathVariable String id) {
        SecurityUtil.checkAuthorizedUser(id);
        return userMapper.userToUserResponse(userService.findById(id));
    }

    @PatchMapping(path = "/user/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable String id, @RequestBody UpdateUserStatusRequest statusRequest) {
        SecurityUtil.checkAuthorizedUser(id);
        userService.updateStatus(id, statusRequest.getActive());
    }
}