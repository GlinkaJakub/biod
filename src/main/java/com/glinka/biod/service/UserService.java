package com.glinka.biod.service;

import com.glinka.biod.dto.UsersDto;
import com.glinka.biod.dto.UsersRegisterDto;
import com.glinka.biod.entity.Users;

import java.util.List;

public interface UserService {

    List<UsersDto> findAllUsers();

    Users findByUsername(String username);

    Users changePassword();

    Users register(UsersRegisterDto users);

}