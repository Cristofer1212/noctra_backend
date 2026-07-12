package views.auth.login;

import config.exception.DatabaseConnectionException;
import modules.user.controller.UserController; // <--- Importa esto
import modules.user.model.User;             // <--- Importa esto
import modules.user.service.UserService;
import java.util.Optional;                  // <--- Importa esto

public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    public boolean iniciarSesion(String nickname, String pin) {
        try {
            boolean loginExitoso = userService.login(nickname, pin);
            if (loginExitoso) {
                // --- AQUÍ ESTÁ LA MAGIA ---
                Optional<User> userOpt = userService.findByNickname(nickname);
                if (userOpt.isPresent()) {
                    // Guardamos el ID en la variable estática que usa el otro controlador
                    UserController.idUsuarioLogueado = userOpt.get().getId();
                    System.out.println("Login exitoso. ID asignado: " + UserController.idUsuarioLogueado);
                }
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