
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
  public static Integer idUsuarioLogueado;
  // constructor
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Override
  // Gemini
  public void handle(HttpExchange exchange) throws IOException {
    String method = exchange.getRequestMethod(); // Devuelve "GET", "POST", "PUT", "DELETE", etc.
    String path = exchange.getRequestURI().getPath(); // Devuelve la str+ing de una url, sin dominio, sin otros parametros. Ej. /api/portero/validar

    // Para que postman sepa leer la respuesta
    exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

    try {

      if ("POST".equalsIgnoreCase(method) && "/users".equals(path)) {
        handleRegister(exchange);
      }
      // new ruta
      else if ("POST".equalsIgnoreCase(method) && "/users/login".equals(path)) {
        // llamar método
        handleLogin(exchange);
      }


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
      // 2. Convertir JSON a DTO - serializar -
      UserRegistrationDto userRegistrationDto = JsonUtils.fromJson(json, UserRegistrationDto.class);
      // 3. Llamar al servicio
      userService.registerUser(userRegistrationDto);

      HttpUtils.sendResponse(exchange, 201, "{\"message\": \"Usuario creado con éxito en Noctra MVP\"}");

    } catch (UserValidatorException e) {

      HttpUtils.sendResponse(exchange, 400, "{\"error\": \"" + e.getMessage() + "\"}");

    } catch (Exception e) {
      System.out.println("DEBUG: Entré al catch del controlador!"); // Añade esta línea
      e.printStackTrace();

      HttpUtils.sendResponse(exchange, 500, "{\"error\": \"" + e.toString() + "\"}");

    }
  }

  private void handleLogin(HttpExchange exchange) throws IOException {
    try {
      // leer json que viene del front
      String json = HttpUtils.readRequestBody(exchange);
      // JSON to Dto.
      LoginDto loginDto = JsonUtils.fromJson(json, LoginDto.class);
      boolean isAuthenticated = userService.login(loginDto.getNickname(), loginDto.getPin());
      // Responder según el resultado
      if (isAuthenticated) {
        Optional<User> userOpt = userService.findByNickname(loginDto.getNickname());
        if (userOpt.isPresent()) {
          idUsuarioLogueado = userOpt.get().getId(); // Guardamos el ID globalmente
        }
        HttpUtils.sendResponse(exchange, 200, "{\"message\": \"Login exitoso\"}");
      } else {
        HttpUtils.sendResponse(exchange, 401, "{\"error\": \"DNI o PIN incorrecto\"}");
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
