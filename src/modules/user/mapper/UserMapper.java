package modules.user.mapper;

import modules.user.dto.UserRegistrationDto;
import modules.user.model.User;

public class UserMapper {


    public User toEntity(UserRegistrationDto userRegistrationDto) {

        if (userRegistrationDto == null) {
            return null;
        }
        // No Null
        User user = new User();
        user.setDni(userRegistrationDto.getDni());
        user.setName(userRegistrationDto.getName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setPassword(userRegistrationDto.getPin());
        // Nulls
        user.setPhone(null);
        user.setAddress(null);
        user.setMail(null);
        user.setState("Active");
        // ¡AQUÍ ES DONDE ESTÁ EL FALLO!
        // Asegúrate de que esta línea exista en el Mapper:
        user.setNickname(userRegistrationDto.getNickname());

        return user;

    }
}

