package modules.user.service;

import config.exception.DatabaseConnectionException;
import modules.user.model.User;
import modules.user.repository.UserRepository;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(User user) throws DatabaseConnectionException {
        // To do cris | Validar existencia de usuario



        userRepository.save(user);
    }

    public Optional<User> getUserByDni(String dni) throws DatabaseConnectionException {
        return userRepository.findByDni(dni);
    }

    public void modifyUser(User user) throws DatabaseConnectionException {
        userRepository.update(user);
    }

    public void removeUser(String dni) throws DatabaseConnectionException {
        userRepository.delete(dni);
    }
}