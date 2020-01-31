package com.glinka.biod.converter.dtoToEntity;

import com.glinka.biod.converter.ConverterAdapter;
import com.glinka.biod.dto.UsersRegisterDto;
import com.glinka.biod.entity.Users;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsersRegisterDtoToUsers extends ConverterAdapter<Users, UsersRegisterDto> {

    @Override
    public Users convert(Users target, UsersRegisterDto source) {

        if(target == null || source == null)
            return null;

        target.setUsername(source.getUsername());
        if (source.getPassword().equals(source.getConfirmPassword())){
//            String encoded = new BCryptPasswordEncoder().encode(source.getPassword());
            String salt = BCrypt.gensalt(12);
            String hash = BCrypt.hashpw(source.getPassword(), salt);

            target.setPassword(hash);
        } else {
            return null;
        }
        target.setEnabled(true);

        return target;
    }
}
