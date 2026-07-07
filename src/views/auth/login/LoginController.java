package views.auth.login;

import config.exception.DatabaseConnectionException;
import modules.user.service.UserService;

public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    public boolean iniciarSesion(String nickname, String pin) {
        try {
            boolean loginExitoso = userService.login(nickname, pin);
            if (loginExitoso) {
                System.out.println("Login exitoso para: " + nickname);
            } else {
                System.out.println("Nickname o contraseña incorrectos.");
            }
            return loginExitoso;
        } catch (DatabaseConnectionException e) {
            System.err.println("[ERROR CRÍTICO] " + e.getMessage());
            return false;
        }
    }
}