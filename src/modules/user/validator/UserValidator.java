package modules.user.validator;

import modules.user.dto.UserRegistrationDto;
import modules.user.exception.UserValidatorException;

public class UserValidator {

    public void validate(UserRegistrationDto userRegistrationDto) throws UserValidatorException {

        if (userRegistrationDto.getName() == null || userRegistrationDto.getName().isBlank() ) {
            throw new UserValidatorException("El nombre no puede estar vacío");
        }



    }

}