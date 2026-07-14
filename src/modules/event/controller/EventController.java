package modules.event.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import config.exception.DatabaseConnectionException;
import java.io.IOException;
import modules.event.dto.CreateEventDto;
import modules.event.service.EventService;
import modules.shared.http.HttpUtils;
import modules.shared.json.JsonUtils;
import modules.user.controller.UserController;

public class EventController implements HttpHandler {

  private final EventService eventService;


  public EventController(EventService eventService) {
    this.eventService = eventService;
  }

  // 1. Este método cumple el contrato obligatorio con HttpHandler.
  // Actúa como el "enrutador" de tu controlador.
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String method = exchange.getRequestMethod();
    String path = exchange.getRequestURI().getPath();

    if ("POST".equalsIgnoreCase(method) && "/event".equals(path)) {
      // Delegamos la petición a nuestro método personalizado
      createEventController(exchange);
    }
    // NUEVO: Manejo del DELETE
    // Si la ruta empieza por /event/ y es DELETE -> Eliminar
    else if (path.startsWith("/event/") && "DELETE".equalsIgnoreCase(method)) {
      handleDeleteEvent(exchange);
    }
    else {
      HttpUtils.sendResponse(exchange, 405, "{\"error\": \"Método no permitido\"}");
    }
  }

  // 2. Este es tu método personalizado con el nombre que querías.
  // Al ser privado e interno, puedes nombrarlo como gustes.
  private void createEventController(HttpExchange exchange) throws IOException {
    try {
      // Leemos el cuerpo de la petición (JSON) como un String
      String jsonBody = HttpUtils.readRequestBody(exchange);

      // Convertimos ese String JSON en tu objeto CreateEventDto
      CreateEventDto dto = JsonUtils.fromJson(jsonBody, CreateEventDto.class);

      // Validación básica en caso de que el cuerpo venga completamente vacío
      if (dto == null) {
        HttpUtils.sendResponse(exchange, 400, "{\"error\": \"El cuerpo de la solicitud no puede estar vacío\"}");
        return;
      }
      dto.setUserId(UserController.idUsuarioLogueado);

      // Enviamos el DTO al servicio para procesarlo
      eventService.createEventService(dto);

      // Respondemos con éxito (201 Created)
      HttpUtils.sendResponse(exchange, 201, "{\"message\": \"Evento creado con éxito\"}");

    } catch (DatabaseConnectionException e) {
      e.printStackTrace();

      // Creamos un mapa con la estructura limpia del error
      java.util.Map<String, String> errorResponse = java.util.Map.of(
          "error", "No se pudo registrar el evento debido a un fallo en la base de datos.",
          "details", e.getMessage() // El error real del repositorio irá seguro aquí dentro del JSON
      );

      // Convertimos el mapa a JSON de forma segura usando Gson
      HttpUtils.sendResponse(exchange, 500, JsonUtils.toJson(errorResponse));

    } catch (Exception e) {
      e.printStackTrace();

      java.util.Map<String, String> errorResponse = java.util.Map.of(
          "error", "Solicitud incorrecta",
          "details", e.getMessage() != null ? e.getMessage() : "Error desconocido");

      HttpUtils.sendResponse(exchange, 400, JsonUtils.toJson(errorResponse));
    }
  }

  private void handleDeleteEvent(HttpExchange exchange) throws IOException {
    try {
      // Obtenemos el path, ej: "/event/5"
      String path = exchange.getRequestURI().getPath();
      // Dividimos el path y obtenemos el último segmento como ID
      String[] segments = path.split("/");
      String idString = segments[segments.length - 1];
      Integer id = Integer.parseInt(idString);

      // Llamamos al servicio
      eventService.deleteEvent(id); // Asegúrate de tener este método en tu EventService

      HttpUtils.sendResponse(exchange, 200, "{\"message\": \"Evento eliminado correctamente\"}");

    } catch (NumberFormatException e) {
      HttpUtils.sendResponse(exchange, 400, "{\"error\": \"ID de evento no válido\"}");
    } catch (DatabaseConnectionException e) {
      e.printStackTrace();
      HttpUtils.sendResponse(exchange, 500, "{\"error\": \"Error al eliminar el evento\"}");
    } catch (Exception e) {
      e.printStackTrace();
      HttpUtils.sendResponse(exchange, 400, "{\"error\": \"Solicitud incorrecta\"}");
    }
  }


}
