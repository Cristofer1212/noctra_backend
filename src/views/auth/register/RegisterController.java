package views.auth.register;

import config.exception.DatabaseConnectionException;
import modules.user.dto.UserRegistrationDto;
import modules.user.service.UserService;

public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Intenta registrar un nuevo usuario con un nickname personalizado.
     * @return null si el registro fue exitoso, o un mensaje de error si algo falló.
     */
    public String registrar(String nombre, String apellido, String dni, String nickname, String pin, String repetirPin) {
        if (nombre == null || nombre.isBlank()) return "El nombre no puede estar vacío.";
        if (apellido == null || apellido.isBlank()) return "El apellido no puede estar vacío.";
        if (dni == null || dni.isBlank()) return "El DNI no puede estar vacío.";
        if (nickname == null || nickname.isBlank()) return "El nickname no puede estar vacío.";
        if (pin == null || pin.isBlank()) return "El PIN no puede estar vacío.";
        if (!pin.equals(repetirPin)) return "Los PIN no coinciden.";

        try {
            // Ahora usamos el nickname proporcionado por el usuario en lugar del DNI
            UserRegistrationDto dto = new UserRegistrationDto(nombre, apellido, dni, pin, nickname);

            userService.registerUser(dto);
            return null; // éxito
        } catch (DatabaseConnectionException e) {
            return "[ERROR CRÍTICO] " + e.getMessage();
        } catch (Exception e) {
            return "Error inesperado: " + e.getMessage();
        }
    }
}