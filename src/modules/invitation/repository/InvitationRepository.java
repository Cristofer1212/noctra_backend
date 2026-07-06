package modules.invitation.repository;

import config.DbConnection;
import config.exception.DatabaseConnectionException;
import modules.invitation.model.Invitation;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

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
            callableStatement.setString(3, invitation.getToken()); // 2. Asignamos el valor del token

            callableStatement.executeUpdate(); // EJECUTA INSERT

            System.out.println("DEBUG: ¡ÉXITO! Registro insertado correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseConnectionException("Eror el regitrar evento" + invitation.getEventId() );
        }


    }
}
