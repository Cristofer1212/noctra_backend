package config.exception;

public class DatabaseConnectionException extends Exception {

    public DatabaseConnectionException(String mensaje) {
        super(mensaje);
    }

    public DatabaseConnectionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}