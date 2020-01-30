package com.glinka.biod.converter.dtoToEntity;

import com.glinka.biod.converter.ConverterAdapter;
import com.glinka.biod.dto.UsersNewPasswordDto;
import com.glinka.biod.entity.Users;
import com.glinka.biod.repository.UsersRepository;
import com.glinka.biod.service.UserService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsersNewPasswordDtoToUsers extends ConverterAdapter<Users, UsersNewPasswordDto> {

    private final UsersRepository usersRepository;

    public UsersNewPasswordDtoToUsers(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    @Override
    public Users convert(Users target, UsersNewPasswordDto source) {

        if(target == null || source == null)
            return null;

        Users user = usersRepository.findByUsername(source.getUsername());
        target.setId(user.getId());
        target.setUsername(user.getUsername());
        target.setEnabled(user.isEnabled());

        if (BCrypt.checkpw(source.getOldPassword(), user.getPassword())){
            if ( source.getNewPassword().equals(source.getConfirmPassword())) {
//                String encoded = new BCryptPasswordEncoder().encode(source.getNewPassword());
                String hash = BCrypt.hashpw(source.getNewPassword(), BCrypt.gensalt(12));
//                target.setPassword(encoded);
                target.setPassword(hash);
            }
        } else {
            return null;
        }

        return target;
    }
}
