package modules.invitation.repository;

import config.DbConnection;
import config.exception.DatabaseConnectionException;
import modules.invitation.model.Invitation;

import java.sql.*;
import java.util.Optional;

public class InvitationRepository implements IInvitationRepository {


    @Override
    public void save(Invitation invitation) throws DatabaseConnectionException {
        // 1. Asegúrate de que issuer_user_id esté en la lista
        String sql = "INSERT INTO invitation (issuer_user_id, guest_id, token, code_qr, state, created_at) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // 2. Aquí es donde se asigna el ID. ¡Verifica que el índice sea correcto!
            stmt.setInt(1, invitation.getIssuerUserId()); // <-- ESTO ES LO QUE TIENE QUE LLEGAR
            stmt.setInt(2, invitation.getGuestId());
            stmt.setString(3, invitation.getToken());
            stmt.setString(4, invitation.getCodeQr());
            stmt.setString(5, invitation.getState());
            stmt.setObject(6, invitation.getCreatedAt());

            stmt.executeUpdate();
            System.out.println("DEBUG: Guardado exitoso con issuer_user_id: " + invitation.getIssuerUserId());
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error al guardar: " + e.getMessage());
        }
    }
    @Override
    public Optional<Invitation> findByToken(String token) throws DatabaseConnectionException {
        String sql = "SELECT * FROM noctra_mvp.invitation WHERE token = ?";
        try(
                Connection connection = DbConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ) {
            preparedStatement.setString(1, token);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Mapteamos los datos de la as de datos al objeto invitación
                    Invitation invitation = new Invitation();
                    invitation.setId(resultSet.getInt("id"));
                    invitation.setEventId(resultSet.getInt("event_id"));
                    invitation.setIssuerUserId(resultSet.getInt("issuer_user_id"));
                    invitation.setGuestId(resultSet.getInt("guest_id"));
                    invitation.setToken(resultSet.getString("token"));
                    invitation.setCodeQr(resultSet.getString("code_qr"));
                    invitation.setState(resultSet.getString("state"));

                    // Convertimos el Timestamp de SQL a LocalDateTime de Java
                    if (resultSet.getTimestamp("created_at") != null) {
                        invitation.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                    }
                    return Optional.of(invitation);
                }

            }
            return Optional.empty();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseConnectionException("Error en BD", e);
        }



    }

    @Override
    public void update(Invitation invitation) throws DatabaseConnectionException {
        String sql = "UPDATE invitation SET state = ? WHERE id = ?";
        try (Connection connection = DbConnection.getConnection() ;
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, invitation.getState());
            preparedStatement.setInt(2, invitation.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error al guardar estado", e);
        }
    }
}
