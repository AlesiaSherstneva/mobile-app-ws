package com.develop.app.ws.service;

import com.develop.app.ws.shared.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto user);
}