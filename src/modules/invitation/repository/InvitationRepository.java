package modules.invitation.repository;

import config.DbConnection;
import config.exception.DatabaseConnectionException;
import modules.invitation.model.Invitation;

import java.sql.*;
import java.util.Optional;

public class InvitationRepository implements IInvitationRepository {


    @Override
    public void save(Invitation invitation) throws DatabaseConnectionException {
        // 1. Agregamos 'event_id' a la consulta SQL
        String sql = "INSERT INTO invitation (event_id, issuer_user_id, guest_id, token, code_qr, state, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DbConnection.getConnection();

             PreparedStatement stmt = connection.prepareStatement(sql)) {
            System.out.println("DEBUG: Base de datos activa: " + connection.getCatalog());
            // 2. Asignamos los valores (¡El orden es muy importante!)
            stmt.setInt(1, invitation.getEventId());      // Nuevo campo
            stmt.setInt(2, invitation.getIssuerUserId());
            stmt.setInt(3, invitation.getGuestId());
            stmt.setString(4, invitation.getToken());
            stmt.setString(5, invitation.getCodeQr());
            stmt.setString(6, invitation.getState());
            stmt.setObject(7, invitation.getCreatedAt());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error al guardar: " + e.getMessage());
        }
    }
    @Override
    public Optional<Invitation> findByToken(String token) throws DatabaseConnectionException {

        String sql = "SELECT * FROM invitation WHERE token = ?";

        try (
                Connection connection = DbConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(1, token);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {

                    Invitation invitation = new Invitation();

                    invitation.setId(resultSet.getInt("id"));
                    invitation.setEventId(resultSet.getInt("event_id"));
                    invitation.setIssuerUserId(resultSet.getInt("issuer_user_id"));
                    invitation.setGuestId(resultSet.getInt("guest_id"));
                    invitation.setToken(resultSet.getString("token"));
                    invitation.setCodeQr(resultSet.getString("code_qr"));
                    invitation.setState(resultSet.getString("state"));

                    Timestamp created = resultSet.getTimestamp("created_at");
                    if (created != null) {
                        invitation.setCreatedAt(created.toLocalDateTime());
                    }

                    return Optional.of(invitation);
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error al buscar invitación por token", e);
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
