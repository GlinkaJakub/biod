package com.glinka.biod.converter.entityToDto;

import com.glinka.biod.converter.ConverterAdapter;
import com.glinka.biod.dto.UsersDto;
import com.glinka.biod.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class UsersToUsersDto extends ConverterAdapter<UsersDto, Users> {

    @Override
    public UsersDto convert(UsersDto target, Users source) {

        if(target == null || source == null)
            return null;

        target.setId(source.getId());
        target.setUsername(source.getUsername());

        return target;
    }
}
