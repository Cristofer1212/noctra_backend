package modules.portero.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import modules.portero.dto.PorteroCreateDto;
import modules.portero.service.PorteroService;
import modules.shared.http.HttpUtils;
import modules.shared.utils.scanner.IScanner;

import java.io.IOException;
import java.util.Map;

public class PorteroController implements HttpHandler {

    private final PorteroService porteroService;
    private final IScanner iScanner;

    public PorteroController(PorteroService porteroService, IScanner iScanner  ) {
        this.porteroService = porteroService;
        this.iScanner = iScanner;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // 1. Configurar los headers de CORS para permitir peticiones desde cualquier origen
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        // 2. Responder inmediatamente al "preflight" OPTIONS
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        String method = exchange.getRequestMethod(); // Devuelve "GET", "POST", "PUT", "DELETE", etc.
        String path = exchange.getRequestURI().getPath(); // Devuelve la str de una url, sin dominio, etc.

        // Para que postman (cliente) sepa leer la respuesta
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

        try {
            // ruta para asignar portero
            if("POST".equalsIgnoreCase(method) && path.equals("/portero")) {
                handleCreatePortero(exchange);
            }

            else if ("POST".equalsIgnoreCase(method) && path.equals("/portero/validar")) {
                handleValidarQr(exchange);
            }
            else {
                HttpUtils.sendResponse(exchange, 404, "{\"error\": \"Ruta no encontrada\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void handleCreatePortero(HttpExchange exchange) throws IOException {
        try {
            // 1. Leer el JSON
            String json = HttpUtils.readRequestBody(exchange);
            // 2. Convertir JSON a DTO - serializar -
            PorteroCreateDto porteroCreateDto = new Gson().fromJson(json, PorteroCreateDto.class);
            // 3. Llamar al servicio
            //porteroService.createPortero();


            HttpUtils.sendResponse(exchange, 200, "{\"message\": \"Portero creado con éxito en Noctra MVP\"}");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void handleValidarQr(HttpExchange exchange) throws IOException {
        try {
            // 1. Leer JSON
            String json = HttpUtils.readRequestBody(exchange);
            Map body = new Gson().fromJson(json, Map.class);

            // 2. Extraer token (validar que no sea null)
            if (body == null || body.get("token") == null) {
                HttpUtils.sendResponse(exchange, 400, "{\"error\": \"Token no proporcionado\"}");
                return;
            }
            String token = body.get("token").toString();

            // 3. Extraer porteroId de forma segura
            Integer porteroId = 1; // Valor por defecto
            if (body.get("porteroId") != null) {
                // Gson suele convertir números a Double por defecto en mapas genéricos
                porteroId = ((Number) body.get("porteroId")).intValue();
            } else {
                System.out.println("DEBUG: porteroId no recibido, usando ID por defecto: 1");
            }

            // 4. Delegar a servicio
            boolean esValido = iScanner.scanQrInvitation(token, porteroId);

            // 5. Crear respuesta
            String mensaje = esValido ? "Acceso Permitido" : "Acceso Denegado";
            String response = String.format("{\"valido\": %b, \"mensaje\": \"%s\"}", esValido, mensaje);

            HttpUtils.sendResponse(exchange, 200, response);
        } catch (Exception e) {
            e.printStackTrace();
            HttpUtils.sendResponse(exchange, 500, "{\"error\": \"Error al procesar el QR: " + e.getMessage() + "\"}");
        }
    }



}
