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
     * Intenta registrar un nuevo usuario.
     * @return null si el registro fue exitoso, o un mensaje de error si algo falló.
     */
    public String registrar(String nombre, String apellido, String dni, String pin, String repetirPin) {
        if (nombre == null || nombre.isBlank()) return "El nombre no puede estar vacío.";
        if (apellido == null || apellido.isBlank()) return "El apellido no puede estar vacío.";
        if (dni == null || dni.isBlank()) return "El DNI no puede estar vacío.";
        if (pin == null || pin.isBlank()) return "El PIN no puede estar vacío.";
        if (!pin.equals(repetirPin)) return "Los PIN no coinciden.";

        try {
            // OJO - IMPORTANTE para Cristian:
            // Este formulario NO tiene campo de "nickname" ni de teléfono, pero
            // UserRegistrationDto los pide (nickname es obligatorio en el constructor,
            // y además es lo que se usa para el login en UserService.login(nickname, pin)).
            //
            // Ahora mismo mando el DNI como nickname temporal SOLO para que esto
            // compile y no rompa nada, pero probablemente esto está mal - Niko
            // mencionó que el "id"/nickname real se genera en otro lado (¿Postman?
            // ¿ngrok? no quedó claro). Hay que definir con Cristian:
            //   1) ¿De dónde sale el nickname real de cada usuario?
            //   2) ¿Se genera automático en el backend, o falta agregar un campo
            //      al formulario para que el usuario lo elija?
            // Ajustar esta línea en cuanto se aclare.
            String nicknamePlaceholder = dni;

            UserRegistrationDto dto = new UserRegistrationDto(nombre, apellido, dni, pin, nicknamePlaceholder);
            // dto.setPhone(...) -> no hay campo de teléfono en este formulario, se deja sin setear.

            userService.registerUser(dto);
            return null; // éxito
        } catch (DatabaseConnectionException e) {
            return "[ERROR CRÍTICO] " + e.getMessage();
        } catch (Exception e) {
            return "Error inesperado: " + e.getMessage();
        }
    }
}