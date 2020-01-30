package com.glinka.biod.service;

import com.glinka.biod.converter.ConverterAdapter;
import com.glinka.biod.dto.UsersDto;
import com.glinka.biod.dto.UsersNewPasswordDto;
import com.glinka.biod.dto.UsersRegisterDto;
import com.glinka.biod.entity.Authorities;
import com.glinka.biod.entity.Users;
import com.glinka.biod.repository.AuthoritiesRepository;
import com.glinka.biod.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;
    private final AuthoritiesRepository authoritiesRepository;

    private final ConverterAdapter<Users, UsersRegisterDto> usersRegDtoToUsersConverter;
    private final ConverterAdapter<UsersDto, Users> usersToUsersDtoConverter;
    private final ConverterAdapter<Users, UsersNewPasswordDto> usersPassDtoToUsersConverter;

    public UserServiceImpl(UsersRepository usersRepository, AuthoritiesRepository authoritiesRepository, ConverterAdapter<Users, UsersRegisterDto> usersRegDtoToUsersConverter, ConverterAdapter<UsersDto, Users> usersToUsersDtoConverter, ConverterAdapter<Users, UsersNewPasswordDto> usersPassDtoToUsersConverter) {
        this.usersRepository = usersRepository;
        this.authoritiesRepository = authoritiesRepository;
        this.usersRegDtoToUsersConverter = usersRegDtoToUsersConverter;
        this.usersToUsersDtoConverter = usersToUsersDtoConverter;
        this.usersPassDtoToUsersConverter = usersPassDtoToUsersConverter;
    }

    @Override
    public List<UsersDto> findAllUsers() {
        return usersToUsersDtoConverter.convertToList(usersRepository.findAll());
    }

    @Override
    public Users findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    @Override
    public Users changePassword(UsersNewPasswordDto users) {
        Users user = usersPassDtoToUsersConverter.convert(users);
        return usersRepository.saveAndFlush(user);
    }

    @Override
    public Users register(UsersRegisterDto usersDto) {
        Users users = usersRegDtoToUsersConverter.convert(usersDto);
        Users newUser =  usersRepository.saveAndFlush(users);
        Authorities authorities = new Authorities();
        authorities.setAuthority("USER");
        authorities.setUsers(newUser);
        authoritiesRepository.saveAndFlush(authorities);
        return newUser;
    }
}
