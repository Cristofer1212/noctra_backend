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
        System.out.println("DEBUG: Iniciando handleInvite");

        try {
            String json = HttpUtils.readRequestBody(exchange);
            System.out.println("DEBUG: JSON recibido: " + json);

            SendInvitationDto sendInvitationDto = JsonUtils.fromJson(json, SendInvitationDto.class);

            // --- VERIFICACIÓN DE SEGURIDAD ---
            Integer idEmisor = UserController.idUsuarioLogueado;
            System.out.println("DEBUG: ID del usuario logueado en UserController: " + idEmisor);
            System.out.println("DEBUG: Event ID en el DTO: " + (sendInvitationDto != null ? sendInvitationDto.getEventId() : "NULL DTO"));

            if (idEmisor == null) {
                HttpUtils.sendResponse(exchange, 401, "{\"error\": \"Usuario no autenticado\"}");
                return;
            }

            invitationService.createInvitation(sendInvitationDto, idEmisor);
            HttpUtils.sendResponse(exchange, 201, "{\"message\": \"Invitación creada exitosamente\"}");

        } catch (Throwable t) {
            t.printStackTrace();
            HttpUtils.sendResponse(exchange, 500, "{\"error\": \"" + t.getMessage() + "\"}");
        }
    }
}
