package com.develop.app.ws.ui.controller;

import com.develop.app.ws.service.UserService;
import com.develop.app.ws.shared.dto.UserDto;
import com.develop.app.ws.ui.model.request.UserDetailsRequestModel;
import com.develop.app.ws.ui.model.response.OperationStatusModel;
import com.develop.app.ws.ui.model.response.ResponseOperationStatus;
import com.develop.app.ws.ui.model.response.UserResponseModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserResponseModel getUser(@PathVariable String id) {
        UserResponseModel response = new UserResponseModel();

        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, response);

        return response;
    }

    @GetMapping
    public List<UserResponseModel> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                            @RequestParam(value = "limit", defaultValue = "25") int limit) {
        List<UserResponseModel> response = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);
        for(UserDto user: users) {
            UserResponseModel userResponse = new UserResponseModel();
            BeanUtils.copyProperties(user, userResponse);
            response.add(userResponse);
        }

        return response;
    }

    @PostMapping
    public UserResponseModel createUser(@RequestBody UserDetailsRequestModel userDetails) {
        UserResponseModel response = new UserResponseModel();

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, response);

        return response;
    }

    @PutMapping(path = "/{id}")
    public UserResponseModel updateUser(@PathVariable String id,
                                        @RequestBody UserDetailsRequestModel userDetails) {
        UserResponseModel response = new UserResponseModel();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updatedUser, response);

        return response;
    }

    @DeleteMapping(path = "/{id}")
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel response = new OperationStatusModel();
        response.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(id);

        response.setOperationResult(ResponseOperationStatus.SUCCESS.name());
        return response;
    }
}