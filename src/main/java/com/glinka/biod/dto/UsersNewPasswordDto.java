package com.glinka.biod.dto;

import lombok.Data;

@Data
public class UsersNewPasswordDto {

    private String username;

    private String oldPassword;

    private String newPassword;

    private String confirmPassword;

}