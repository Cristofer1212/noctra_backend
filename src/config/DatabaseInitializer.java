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
        // Si llegamos aquí, el programa sigue su curso, pero no forzamos la ejecución de archivos .sql
        // porque ya los ejecutamos manualmente en Workbench.
    }

    private static boolean checkIfDatabaseHasData() {
        // Sin corchetes [ ], SQL estándar de MySQL
        String sql = "SELECT COUNT(*) FROM user";
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            // Si hay error (ej. la tabla no existe), devolvemos false
            return false;
        }
        return false;
    }
}
