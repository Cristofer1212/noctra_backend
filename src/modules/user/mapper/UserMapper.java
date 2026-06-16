package modules.user.mapper;

import modules.user.dto.UserRegistrationDto;
import modules.user.model.User;

public class UserMapper {


    public User toEntity(UserRegistrationDto userRegistrationDto) {

        if (userRegistrationDto == null) {
            return null;
        }

        User user = new User();

        user.setName(userRegistrationDto.getName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setDni(userRegistrationDto.getDni());

        user.setState("Ative");

        return user;

    }
}

