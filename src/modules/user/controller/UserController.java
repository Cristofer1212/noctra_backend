
package modules.user.controller;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import modules.shared.http.HttpUtils;
import modules.shared.json.JsonUtils;
import modules.user.dto.LoginDto;
import modules.user.dto.UserRegistrationDto;
import modules.user.exception.UserValidatorException;
import modules.user.mapper.UserMapper;
import modules.user.model.User;
import modules.user.service.UserService;
import modules.user.validator.UserValidator;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

public class UserController implements HttpHandler {
    private final UserService userService;
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName()); // estudiar

    // constructor
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    // Gemini
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        // Configurar cabecera para responder siempre en formato JSON
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

        try {
            // 📬 1. RUTA PARA REGISTRAR (POST /users)
            if ("POST".equalsIgnoreCase(method) && "/users".equals(path)) {
                handleRegister(exchange);
            }
            // new ruta
            else if ("POST".equalsIgnoreCase(method) && "/users/login".equals(path) ) {
                // llamar método
                handleLogin(exchange);
            }

            // ❌ RUTA NO ENCONTRADA
            else {
                HttpUtils.sendResponse(exchange, 404, "{\"message\": \"Ruta no encontrada en Noctra\"}");
            }
        } catch (Exception e) {
            HttpUtils.sendResponse(exchange, 500, "{\"error\": \"" + e.getMessage() + "\"}");
        }
    }


    private void handleRegister(HttpExchange exchange) throws IOException {
        try {
            // 1. De String a Json
            String json = HttpUtils.readRequestBody(exchange);
            // 2. De Json a DTO
            UserRegistrationDto userRegistrationDto = JsonUtils.fromJson(json, UserRegistrationDto.class);
            // 3. DTO --> Service
            userService.registerUser(userRegistrationDto);

            HttpUtils.sendResponse(exchange, 201, "{\"message\": \"Usuario creado con éxito en Noctra MVP\"}");

        } catch (UserValidatorException e) {

            HttpUtils.sendResponse(exchange, 400, "{\"error\": \"" + e.getMessage() + "\"}");

        } catch (Exception e) {
            // 2. MANDA EL MENSAJE REAL A POSTMAN (no un mensaje fijo)
            HttpUtils.sendResponse(exchange, 500, "{\"error\": \"" + e.toString() + "\"}");

        }
    }

    private void handleLogin(HttpExchange exchange) throws IOException {
        try {
            // leer json que viene del front
            String json = HttpUtils.readRequestBody(exchange);
            // JSON to Dto.
            LoginDto loginDto = JsonUtils.fromJson(json, LoginDto.class);
            boolean isAuthenticated = userService.login(loginDto.getDni(), loginDto.getPin());
            // 4. Responder según el resultado
            if (isAuthenticated) {
                HttpUtils.sendResponse(exchange, 200, "{\"message\": \"Login exitoso\"}");
            } else {
                HttpUtils.sendResponse(exchange, 401, "{\"error\": \"DNI o PIN incorrecto\"}");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}