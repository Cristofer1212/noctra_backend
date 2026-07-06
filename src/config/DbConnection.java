package config;
import config.exception.DatabaseConnectionException;
import modules.shared.config.ConfigLoader;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;



public class DbConnection {

    // Establecer la connection protegiendo las credenciales

    public static Connection getConnection() throws DatabaseConnectionException {

        try {
            // Ahora solo le pides los datos al cargador de configuración
            return DriverManager.getConnection(
                    ConfigLoader.getProperty("db.url"),
                    ConfigLoader.getProperty("db.user"),
                    ConfigLoader.getProperty("db.password")
            );
        } catch (Exception e) {
            throw new DatabaseConnectionException("Error en BD", e);
        }

    }

}


