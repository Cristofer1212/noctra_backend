package modules.analytics.repository;

import config.DbConnection;
import config.exception.DatabaseConnectionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsRepository {

    public List<Map<String, Object>> getTopReingresos() throws DatabaseConnectionException {
        List<Map<String, Object>> resultados = new ArrayList<>();
        String sql = "{CALL sp_get_top_reingresos()}";

        try (Connection conn = DbConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("invitation_id", rs.getInt("invitation_id"));
                row.put("total_movimientos", rs.getInt("total_movimientos"));
                resultados.add(row);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getMessage());
        }
        return resultados;
    }
}