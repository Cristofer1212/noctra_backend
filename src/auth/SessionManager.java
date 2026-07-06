package auth;



public class SessionManager {

    // Almacena el DNI del usuario que hizo login
    private static String loggedUserDni;

    // Se llama cuando el usuario ingresa sus credenciales correctamente
    public static void login(String dni) {
        loggedUserDni = dni;
    }

    public static String getLoggedUserDni() {
        return loggedUserDni;
    }

    // Se llama cuando el usuario cierra sesión
    public static void logout() {
        loggedUserDni = null;
    }



}
