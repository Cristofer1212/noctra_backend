package modules.user.validator;

import modules.user.dto.UserRegistrationDto;
import modules.user.exception.UserValidatorException;

public class UserValidator {

  public void validateLogin(UserRegistrationDto userRegistrationDto) throws UserValidatorException {
    if (userRegistrationDto.getDni() == null || userRegistrationDto.getDni().length() != 8) {
      throw new UserValidatorException("El DNI debe tener 8 dígitos");
    }
    if (userRegistrationDto.getPin() == null || !userRegistrationDto.getPin().matches("\\d{4}")) {
      throw new UserValidatorException("El PIN debe ser exactamente de 4 dígitos");
    }
    if (userRegistrationDto.getNickname() == null || userRegistrationDto.getNickname().length() < 1) {
      throw new UserValidatorException("Debe ingresar un Nickname");
    }

  }

  public void validateRegistration(UserRegistrationDto userRegistrationDto) throws UserValidatorException {
    if (userRegistrationDto.getName() == null || userRegistrationDto.getName().isBlank()) {
      throw new UserValidatorException("El nombre es obligatorio");
    }
    if (userRegistrationDto.getLastName() == null || userRegistrationDto.getLastName().isBlank()) {
      throw new UserValidatorException("El apelido es necesario");
    }
    validateLogin(userRegistrationDto);

  }

}
