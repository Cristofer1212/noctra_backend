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
        PreparedStatement stmt = conn.prepareStatement(sql)) { // <- Todo dentro de los paréntesis

      stmt.setString(1, nickname);

      try (ResultSet rs = stmt.executeQuery()) { // Es buena práctica cerrar el ResultSet también
        if (rs.next()) {
          return Optional.of(new User(
              rs.getInt("id"),
              rs.getString("pin")));
        }
      }
    } catch (SQLException e) {
      throw new DatabaseConnectionException("Error al buscar usuario: " + e.getMessage());
    }
    return Optional.empty(); // Si no encuentra nada, retorna un Optional vacío

  }

  // Insertar Usuario
  @Override
  public void save(User user) throws DatabaseConnectionException {
    // Aquí tenemos 6 signos '?'
    String sql = "INSERT INTO user (dni, name, last_name, phone, pin, nickname) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection connection = DbConnection.getConnection();
         // OJO: Cambia prepareCall por prepareStatement
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      // Aquí tenemos que enviar 6 valores en total
      preparedStatement.setString(1, user.getDni());
      preparedStatement.setString(2, user.getName());
      preparedStatement.setString(3, user.getLastName());
      preparedStatement.setString(4, user.getPhone());
      preparedStatement.setString(5, user.getPin());
      preparedStatement.setString(6, user.getNickname());

      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      throw new DatabaseConnectionException("Error al registrar el usuario con DNI: " + user.getDni(), e);
    }
  }

  // Buscar usuario por DNI
  @Override
  public Optional<User> findByDni(String dni) throws DatabaseConnectionException {
    // Llamada al SP
    String sql = "SELECT id, dni, name, last_name, phone, address, mail, password, state FROM user WHERE dni = ?";

    try (Connection connection = DbConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareCall(sql)) {

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
      throw new DatabaseConnectionException("Error al buscar usuario por DNI mediante SP: " + dni, e);
    }
    return Optional.empty();
  }

  // Actualizar usuario
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
      stmt.setString(8, user.getDni()); // El DNI va al final para el WHERE

      stmt.executeUpdate();
      System.out.println("🔄 Usuario con DNI " + user.getDni() + " actualizado en la base de datos.");

    } catch (SQLException e) {
      throw new DatabaseConnectionException("Error al actualizar el usuario con DNI: " + user.getDni(), e);
    }
  }

  // Eliminar usuario (O cambiar estado a inactivo si prefieres)
  @Override
  public void delete(String dni) throws DatabaseConnectionException {
    String sql = "DELETE FROM [user] WHERE dni = ?";

    try (Connection connection = DbConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {

      stmt.setString(1, dni);
      stmt.executeUpdate();
      System.out.println("🗑️ Usuario con DNI " + dni + " eliminado de la base de datos.");

    } catch (SQLException e) {
      throw new DatabaseConnectionException("Error al eliminar el usuario con DNI: " + dni, e);
    }
  }

}
