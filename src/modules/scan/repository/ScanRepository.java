package modules.scan.repository;


import config.DbConnection;
import config.exception.DatabaseConnectionException;
import modules.scan.model.Scan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ScanRepository {

    public void save(Scan scan) throws DatabaseConnectionException {

        String sql = "INSERT INTO scan (invitation_id, scanned_by, movement_type, result) VALUES (?, ?, ?, ?)";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {

            preparedStatement.setInt(1, scan.getInvitationId());
            preparedStatement.setInt(2, scan.getScannedBy());
            preparedStatement.setString(3, scan.getMovementType());
            preparedStatement.setString(4, scan.getResult());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseConnectionException("No se pudo registrar el escaneo en la base de datos", e);
        }
    }

}
