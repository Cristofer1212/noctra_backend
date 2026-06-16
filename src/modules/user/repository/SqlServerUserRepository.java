package modules.user.repository;
import config.DbConnection;
import config.exception.DatabaseConnectionException;
import modules.user.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;



public  class SqlServerUserRepository implements UserRepository {


    // Crear usuario
    @Override
    public void save(User user) throws DatabaseConnectionException {

        // Model SQL
        String sql = " INSERT INTO [user]  (dni, name, last_name, phone, address, mail, password, state ) " + "VALUES (?,?,?,?,?,?,?,?)";

        try(Connection connection = DbConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            // Inyectamos los datos del objeto en cada '?'
            stmt.setString(1, user.getDni());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getMail());
            stmt.setString(7, user.getPassword());
            stmt.setString(8, user.getState());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error al registrar el usuario con DNI: " + user.getDni(), e);
        }

    }







    // Buscar usuario por DNI
    @Override
    public Optional<User> findByDni(String dni) throws DatabaseConnectionException {
        String sql = "SELECT id, dni, name, last_name, phone, mail, password, state FROM [user] WHERE dni = ?";

        try(
                Connection connection = DbConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ) {
            stmt.setString(1, dni);

            // estudiar clase ResultSet
            try(ResultSet rs = stmt.executeQuery()) {
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
            throw new DatabaseConnectionException("Error al buscar usuario por DNI: " + dni, e);
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