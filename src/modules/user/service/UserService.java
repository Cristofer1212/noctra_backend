package modules.user.service;

import config.exception.DatabaseConnectionException;
import modules.user.dto.UserRegistrationDto;
import modules.user.mapper.UserMapper;
import modules.user.model.User;
import modules.user.repository.SqlServerUserRepository;
import modules.user.repository.UserRepository;
import modules.user.validator.UserValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserValidator userValidator, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.userMapper = userMapper;
    }

    public void registerUser(UserRegistrationDto userRegistrationDto) throws DatabaseConnectionException {
        // validar
        userValidator.validateRegistration(userRegistrationDto);
        // convertir (delegamos a maper)
        User user = userMapper.toEntity(userRegistrationDto);

        // hashear pin
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        // guardar base de datos
        userRepository.save(user);
    }

    public boolean login(String dni, String pin) throws DatabaseConnectionException {
        Optional<User> userOptional = userRepository.findByDni(dni);
        if ( userOptional.isEmpty()) {
            return false;
        }
        User user = userOptional.get();
        return BCrypt.checkpw(pin, user.getPassword());
    }
}