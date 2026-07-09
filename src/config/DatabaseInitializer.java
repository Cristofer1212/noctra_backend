package config;

import config.exception.DatabaseConnectionException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

  public static void initializeDatabase() throws DatabaseConnectionException {

    try {
      // 1. Verificamos si ya existen datos de forma segura para MySQL
      if (checkIfDatabaseHasData()) {
        System.out.println("\nBase de datos detectada. Todo listo para trabajar.");
        return;
      }

      System.out.println("\nBase de datos vacía o no detectada. Verifique en Workbench");
      // Si llegamos aquí, el programa sigue su curso, pero no forzamos la ejecución
      // de archivos .sql
      // porque ya los ejecutamos manualmente en Workbench.

    } catch (SQLException e) {

      // Aquí capturamos el error real de la base de datos (credenciales, puerto,
      // etc.)
      System.err.println("\n[ERROR] No se pudo verificar la base de datos debido a un problema de conexión.");

      // Imprimimos el error en la consola de error para facilitar la depuración
      e.printStackTrace();

      // Envolvemos la excepción original dentro de la excepción personalizada para no
      // perder la causa raíz
      throw new DatabaseConnectionException("Error al conectar o inicializar la base de datos", e);

    }

  }

  private static boolean checkIfDatabaseHasData() throws SQLException {
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
