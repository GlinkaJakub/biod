package com.glinka.biod.dto;

import lombok.Data;

@Data
public class UsersRegisterDto {

    private String username;

    private String password;

    private String confirmPassword;

}
