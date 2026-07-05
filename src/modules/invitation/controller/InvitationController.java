package modules.invitation.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import config.exception.DatabaseConnectionException;
import modules.shared.http.HttpUtils;

import java.io.IOException;

public class InvitationController implements HttpHandler {


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


    }

}
