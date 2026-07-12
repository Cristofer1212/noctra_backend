package modules.invitation.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import modules.invitation.dto.SendInvitationDto;
import modules.invitation.service.InvitationService;
import modules.shared.http.HttpUtils;
import modules.shared.json.JsonUtils;
import modules.user.controller.UserController;
import modules.user.service.UserService;

import java.io.IOException;

public class InvitationController implements HttpHandler {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {

        this.invitationService = invitationService;
    }



    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod(); // Obtener el verbo de la petición
        String path = exchange.getRequestURI().getPath(); // De la petición retorna "/invitations"

        // Configurar cabecerea
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

        try {
            if ("POST".equalsIgnoreCase(method) && "/invitations".equals(path)) {
                handleInvite(exchange);
            }
            else {
                HttpUtils.sendResponse(exchange, 404, "{\"invitation\": \"Ruta no encontrada en Noctra\"}");
            }
        } catch (Exception e) {
            HttpUtils.sendResponse(exchange, 500, "{\"error\": \"" + e.getMessage() + "\"}");
        }




    }


    public void handleInvite(HttpExchange exchange) throws IOException {
        System.out.println("DEBUG: Iniciando handleInvite en InvitationController");

        try {
            // 1. Leer el cuerpo UNA SOLA VEZ
            String json = HttpUtils.readRequestBody(exchange);
            System.out.println("DEBUG: JSON recibido: " + json);

            // 2. Convertir JSON a DTO
            SendInvitationDto sendInvitationDto = JsonUtils.fromJson(json, SendInvitationDto.class);

            // 3. Obtener el ID del emisor (issuerUserId) del contexto de la sesión
            // Como no tienes clase de sesión, aquí lo sacas de donde tengas guardado al usuario logueado
            Integer idEmisor = UserController.idUsuarioLogueado; // <--- AQUÍ DEBES PONER EL ID DEL USUARIO REAL QUE HIZO LOGIN
            if (idEmisor == null) {
                HttpUtils.sendResponse(exchange, 401, "{\"error\": \"Usuario no autenticado\"}");
                return;
            }
            // 4. Llamar al servicio pasando el DTO Y el contexto (emisor)
            // Nota: Ajusta tu createInvitation en el Service para recibir estos parámetros
            invitationService.createInvitation(sendInvitationDto, idEmisor);

            // 5. Enviar respuesta exitosa
            HttpUtils.sendResponse(exchange, 201, "{\"message\": \"Invitación creada exitosamente\"}");

        } catch (Throwable t) {
            // 6. Manejo centralizado de errores
            System.err.println("ERROR FATAL CAPTURADO:");
            t.printStackTrace();
            HttpUtils.sendResponse(exchange, 500, "{\"error\": \"" + t.getMessage() + "\"}");
        }
    }
}
