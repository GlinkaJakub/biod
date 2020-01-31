package com.glinka.biod.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UsersRegisterDto {

    @Size(min = 3, max = 128)
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9]{8,}", message = "Password must be contain at least 1 upper-case character, 1 lower-case character and 1 digit")
    @Size(min = 8, max = 128)
    private String password;

    private String confirmPassword;

}
