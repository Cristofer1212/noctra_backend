package config;

import config.exception.DatabaseConnectionException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initializeDatabase() throws DatabaseConnectionException {
        // 1. Verificamos si ya existen datos de forma segura para MySQL
        if (checkIfDatabaseHasData()) {
            System.out.println("\nBase de datos detectada. Todo listo para trabajar.");
            return;
        }

        System.out.println("\nBase de datos vacía o no detectada. Verifique en Workbench");
    }
    private static boolean checkIfDatabaseHasData() {
        // Esta consulta verifica si la tabla 'user' existe en el esquema
        String sql = "SHOW TABLES LIKE 'user'";
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Si hay un resultado, es que la tabla existe
            return rs.next();
        } catch (Exception e) {
            return false;
        }
    }
}
