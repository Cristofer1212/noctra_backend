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

        String sql = "INSERT INTO invitation (event_id, issuer_user_id, guest_id, code_qr) VALUES (?, ?, ?, ?)";

        try(Connection connection = DbConnection.getConnection();
            CallableStatement callableStatement = connection.prepareCall(sql);
        ) {
            callableStatement.setInt(1, invitation.getEventId());
            callableStatement.setInt(2, invitation.getIssuerUserId());
            callableStatement.setInt(3, invitation.getGuestId());
            callableStatement.setString(4, invitation.getCodeQr());

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseConnectionException("Eror el regitrar evento" + invitation.getEventId() );
        }


    }
}
