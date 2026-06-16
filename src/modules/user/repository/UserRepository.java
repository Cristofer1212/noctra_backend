package modules.user.repository;

import modules.user.model.User;
import config.exception.DatabaseConnectionException;
import java.util.Optional;

public interface UserRepository {

    void save(User user) throws DatabaseConnectionException;
    Optional<User> findByDni(String dni) throws DatabaseConnectionException;
    void update(User user) throws DatabaseConnectionException;
    void delete(String dni) throws DatabaseConnectionException;

}