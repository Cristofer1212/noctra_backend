package modules.user.repository;

import config.DbConnection;
import config.exception.DatabaseConnectionException;
import modules.user.model.User;

import java.sql.*;
import java.util.Optional;

public class UserRepository implements IUserRepository {

  @Override
  public Optional<User> findByNickname(String nickname) throws DatabaseConnectionException {

    String sql = "SELECT * FROM user WHERE nickname = ?";

    try (Connection conn = DbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, nickname);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          // Actualizado: usamos 'password' en lugar de 'pin'
          return Optional.of(new User(
                  rs.getInt("id"),
                  rs.getString("password")));
        }
      }
    } catch (SQLException e) {
      throw new DatabaseConnectionException("Error al buscar usuario: " + e.getMessage());
    }
    return Optional.empty();
  }

  @Override
  public void findByPhone(String phone) throws DatabaseConnectionException {
  }

  @Override
  public Optional<User> findById(Integer id) throws DatabaseConnectionException {
    String sql = "SELECT * FROM user WHERE id = ?";
    try(Connection connection = DbConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
    ) {
      preparedStatement.setInt(1, id);

      try(ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          User user = new User();
          user.setId(resultSet.getInt("id"));
          user.setDni(resultSet.getString("dni"));
          user.setName(resultSet.getString("name"));
          user.setLastName(resultSet.getString("last_name"));
          user.setPhone(resultSet.getString("phone"));
          user.setAddress(resultSet.getString("address"));
          user.setMail(resultSet.getString("mail"));
          user.setPassword(resultSet.getString("password"));
          user.setState(resultSet.getString("state"));

          return Optional.of(user);
        }
      }
    } catch (SQLException e) {
      throw new DatabaseConnectionException("Error al buscar usuario: " + e.getMessage());
    }
    return Optional.empty();
  }

  // Insertar Usuario
  @Override
  public void save(User user) throws DatabaseConnectionException {
    // Actualizado: cambiamos 'pin' por 'password' en el SQL
    String sql = "INSERT INTO user (dni, name, last_name, phone, password, nickname) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection connection = DbConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      preparedStatement.setString(1, user.getDni());
      preparedStatement.setString(2, user.getName());
      preparedStatement.setString(3, user.getLastName());
      preparedStatement.setString(4, user.getPhone());
      preparedStatement.setString(5, user.getPassword()); // Actualizado: usamos getPassword()
      preparedStatement.setString(6, user.getNickname());

      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      throw new DatabaseConnectionException("Error al registrar el usuario con DNI: " + user.getDni(), e);
    }
  }

  // Buscar usuario por DNI
  @Override
  public Optional<User> findByDni(String dni) throws DatabaseConnectionException {
    String sql = "SELECT id, dni, name, last_name, phone, address, mail, password, state FROM user WHERE dni = ?";

    try (Connection connection = DbConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) { // Corregido a prepareStatement

      preparedStatement.setString(1, dni);

      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          User user = new User();
          user.setId(rs.getInt("id"));
          user.setDni(rs.getString("dni"));
          user.setName(rs.getString("name"));
          user.setLastName(rs.getString("last_name"));
          user.setPhone(rs.getString("phone"));
          user.setAddress(rs.getString("address"));
          user.setMail(rs.getString("mail"));
          user.setPassword(rs.getString("password"));
          user.setState(rs.getString("state"));
          return Optional.of(user);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DatabaseConnectionException("Error al buscar usuario por DNI: " + dni, e);
    }
    return Optional.empty();
  }

  @Override
  public void update(User user) throws DatabaseConnectionException {
    String sql = "UPDATE [user] SET name = ?, last_name = ?, phone = ?, address = ?, mail = ?, password = ?, state = ? WHERE dni = ?";

    try (Connection connection = DbConnection.getConnection();
         PreparedStatement stmt = connection.prepareStatement(sql)) {

      stmt.setString(1, user.getName());
      stmt.setString(2, user.getLastName());
      stmt.setString(3, user.getPhone());
      stmt.setString(4, user.getAddress());
      stmt.setString(5, user.getMail());
      stmt.setString(6, user.getPassword());
      stmt.setString(7, user.getState());
      stmt.setString(8, user.getDni());

      stmt.executeUpdate();

    } catch (SQLException e) {
      throw new DatabaseConnectionException("Error al actualizar el usuario con DNI: " + user.getDni(), e);
    }
  }

  @Override
  public void delete(String dni) throws DatabaseConnectionException {
    String sql = "DELETE FROM [user] WHERE dni = ?";

    try (Connection connection = DbConnection.getConnection();
         PreparedStatement stmt = connection.prepareStatement(sql)) {

      stmt.setString(1, dni);
      stmt.executeUpdate();

    } catch (SQLException e) {
      throw new DatabaseConnectionException("Error al eliminar el usuario con DNI: " + dni, e);
    }
  }
}