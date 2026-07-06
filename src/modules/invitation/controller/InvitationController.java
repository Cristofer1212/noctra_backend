package modules.invitation.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import modules.invitation.dto.SendInvitationDto;
import modules.invitation.service.InvitationService;
import modules.shared.http.HttpUtils;
import modules.shared.json.JsonUtils;
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
        System.out.println("DEBUG: Iniciando handleInvite en InvitationController"); // Log de entrada
        try {
            String json = HttpUtils.readRequestBody(exchange);
            System.out.println("DEBUG: JSON recibido: " + json); // Ver qué llega

            SendInvitationDto sendInvitationDto = JsonUtils.fromJson(json, SendInvitationDto.class);
            invitationService.createInvitation(sendInvitationDto);

            HttpUtils.sendResponse(exchange, 201, "{\"message\": \"Invitación creada exitosamente\"}");
        } catch (Throwable t) { // Capturamos Throwable para ver errores graves o errores de carga
            System.err.println("ERROR FATAL CAPTURADO:");
            t.printStackTrace(); // Esto DEBE salir en tu consola
            HttpUtils.sendResponse(exchange, 500, "{\"error\": \"" + t.getMessage() + "\"}");
        }
        try {
            // String a jSON
            String json = HttpUtils.readRequestBody(exchange);
            // JSON a DTO
            SendInvitationDto sendInvitationDto = JsonUtils.fromJson(json, SendInvitationDto.class);
            // DTO --> Service
            invitationService.createInvitation(sendInvitationDto);
            // Respuesta de éxito
            HttpUtils.sendResponse(exchange, 201, "{\"message\": \"Invitación creada exitosamente\"}");




        } catch (Exception e) {
            e.printStackTrace();
            HttpUtils.sendResponse(exchange, 500, "{\"error\": \"" + e.toString() + "\"}");
        }

    }

}
