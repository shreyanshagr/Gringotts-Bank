package com.gringotts.userservice.service;

import com.gringotts.userservice.model.dto.CreateUser;
import com.gringotts.userservice.model.dto.UserDto;
import com.gringotts.userservice.model.dto.UserUpdate;
import com.gringotts.userservice.model.dto.UserUpdateStatus;
import com.gringotts.userservice.model.dto.response.Response;

import java.util.List;

public interface UserService {


    Response createUser(CreateUser userDto);

    List<UserDto> readAllUsers();

    UserDto readUser(String authId);

    Response updateUserStatus(Long id, UserUpdateStatus userUpdate);

    Response updateUser(Long id, UserUpdate userUpdate);

    UserDto readUserById(Long userId);

    UserDto readUserByAccountId(String accountId);
}