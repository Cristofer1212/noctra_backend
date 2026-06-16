
package modules.user.controller;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import modules.shared.http.HttpUtils;
import modules.shared.json.JsonUtils;
import modules.user.dto.UserRegistrationDto;
import modules.user.exception.UserValidatorException;
import modules.user.mapper.UserMapper;
import modules.user.model.User;
import modules.user.service.UserService;
import modules.user.validator.UserValidator;
import java.io.IOException;
import java.util.logging.Logger;

public class UserController implements HttpHandler {
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserValidator userValidator;
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName()); // estudiar

    // constructor
    public UserController(UserService userService) {
        this.userService = userService;
        this.userValidator = new UserValidator();
        this.userMapper = new UserMapper();
    }

    @Override
    // Estudiar
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
            // 🔍 2. RUTA PARA BUSCAR POR DNI (GET /users?dni=XXXXXX)

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
            // 1. Leer el JSON
            String json = HttpUtils.readRequestBody(exchange);

            // 2. Convertir JSON a DTO
            UserRegistrationDto userRegistrationDto = JsonUtils.fromJson(json, UserRegistrationDto.class);

            // 3. Validar
            userValidator.validate(userRegistrationDto);


            // 3. Convertir DTO a Modelo
            User user = userMapper.toEntity(userRegistrationDto);

            // 4. Llamar al servicio
            userService.registerUser(user);

            // 5. Respuesta de éxito
            HttpUtils.sendResponse(exchange, 201, "{\"message\": \"Usuario creado con éxito en Noctra MVP\"}");

        } catch (UserValidatorException e) {

            HttpUtils.sendResponse(exchange, 400, "{\"error\": \"" + e.getMessage() + "\"}");

        } catch (Exception e) {
            e.printStackTrace();

            // 2. MANDA EL MENSAJE REAL A POSTMAN (no un mensaje fijo)
            HttpUtils.sendResponse(exchange, 500, "{\"error\": \"" + e.toString() + "\"}");

        }
    }

}