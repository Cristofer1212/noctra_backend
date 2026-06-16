package config;


import config.exception.DatabaseConnectionException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DbConnection {


    // Establecer la conección protegiendo las credenciales

    public static Connection getConnection() throws DatabaseConnectionException {

        Properties properties = new Properties();

        try (InputStream input = DbConnection.class.getClassLoader().getResourceAsStream("resources/config.properties") ) {

            if ( input == null ) throw new IOException("Credenciales no enctradas");

            // hacerr mapeable input
            properties.load(input);

            return DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.user"),
                    properties.getProperty("db.password")
            );



        } catch (Exception e) {
            throw new DatabaseConnectionException("Error al establecer la conexión con la base de datos", e );
        }






    }






}


