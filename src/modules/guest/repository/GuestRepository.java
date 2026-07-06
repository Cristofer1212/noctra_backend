package modules.guest.repository;

import config.DbConnection;
import config.exception.DatabaseConnectionException;
import modules.guest.model.Guest;

import java.sql.*;

public class GuestRepository implements IGuestRepository {


    @Override
    public void save(Guest guest) throws DatabaseConnectionException {

        String sql = "INSERT INTO guest (phone, gender) VALUES (?, ?) ";

        try (Connection connection = DbConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, guest.getPhone());
            preparedStatement.setString(2, guest.getGender());

            int affectedRows = preparedStatement.executeUpdate(); // Ejecuta el INSERT y Devuelve 1 o 0 si se inserto una fila
            if (affectedRows == 0) {
                throw new SQLException("Error al crear invitado, no se afectaron filas.");
            }

            // Traer ID
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    guest.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Error al crear invitado, no se obtuvo ID.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error al registrar invitado" + e.getMessage());
        }

    }
}
