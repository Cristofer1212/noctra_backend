package modules.invitation.repository;

import config.DbConnection;
import config.exception.DatabaseConnectionException;
import modules.invitation.model.Invitation;

import java.sql.*;
import java.util.Optional;

public class InvitationRepository implements IInvitationRepository {


    @Override
    public void save(Invitation invitation) throws DatabaseConnectionException {
        System.out.println("DEBUG: Entrando al método save del repositorio...");
        System.out.println("DEBUG: Datos a guardar: Token=" + invitation.getToken() + ", QR=" + invitation.getCodeQr());

        String sql = "INSERT INTO noctra_mvp.invitation (guest_id, code_qr, token) VALUES (?, ?, ?)";

        try(Connection connection = DbConnection.getConnection();
            CallableStatement callableStatement = connection.prepareCall(sql);
        ) {
            // callableStatement.setInt(1, invitation.getEventId());
            // callableStatement.setInt(2, invitation.getIssuerUserId());
            callableStatement.setInt(1, invitation.getGuestId());
            callableStatement.setString(2, invitation.getCodeQr());
            callableStatement.setString(3, invitation.getToken()); // Asignamos el valor del token

            callableStatement.executeUpdate(); // EJECUTA INSERT

            System.out.println("DEBUG: ¡ÉXITO! Registro insertado correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseConnectionException("Eror el regitrar evento" + invitation.getEventId() );
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
