package auth;



public class SessionManager {

    private static String loggedUserDni;

    public static void login(String dni) {
        loggedUserDni = dni;
    }

    public static String getLoggedUserDni() {
        return loggedUserDni;
    }

    public static void logout() {
        loggedUserDni = null;
    }



}
