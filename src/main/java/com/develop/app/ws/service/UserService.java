package com.develop.app.ws.service;

import com.develop.app.ws.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto user);

    UserDto getUser(String email);

    UserDto getUserByUserId(String id);

    List<UserDto> getUsers(int page, int limit);

    UserDto updateUser(String userId, UserDto userDto);

    void deleteUser(String userId);
}