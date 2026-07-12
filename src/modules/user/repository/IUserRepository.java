package modules.user.repository;

import modules.user.model.User;
import config.exception.DatabaseConnectionException;
import java.util.Optional;

public interface IUserRepository {

  void save(User user) throws DatabaseConnectionException;

  Optional<User> findByDni(String dni) throws DatabaseConnectionException;

  void update(User user) throws DatabaseConnectionException;

  void delete(String dni) throws DatabaseConnectionException;

  Optional<User> findByNickname(String nickname) throws DatabaseConnectionException;

  void findByPhone(String phone) throws DatabaseConnectionException;

  Optional<User> findById(Integer id) throws DatabaseConnectionException;
}
