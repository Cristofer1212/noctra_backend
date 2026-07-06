package modules.shared.integrations.meta_webhook;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class WebhookController implements HttpHandler {

    private final IWebhookHandler webhookHandler;

    public WebhookController(IWebhookHandler webhookHandler) {
        this.webhookHandler = webhookHandler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = parseQuery(query);

        if ("GET".equals(exchange.getRequestMethod())) {
            String mode = params.get("hub.mode");
            String token = params.get("hub.verify_token");
            String challenge = params.get("hub.challenge");

            try {
                String response = webhookHandler.handleVerification(mode, token, challenge);
                exchange.sendResponseHeaders(200, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } catch (Exception e) {
                String error = "Error de verificación";
                exchange.sendResponseHeaders(403, error.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(error.getBytes());
                }
            }
        } else if ("POST".equals(exchange.getRequestMethod())) {
            // Leer el cuerpo del mensaje que envía Meta
            java.io.InputStream is = exchange.getRequestBody();
            byte[] bodyBytes = is.readAllBytes();
            String body = new String(bodyBytes);

            System.out.println("EVENTO RECIBIDO DE META: " + body);

            // Responder a Meta para que deje de marcar error
            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseBody().close();
        }
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> queryPairs = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                if (idx > 0) {
                    queryPairs.put(pair.substring(0, idx), pair.substring(idx + 1));
                }
            }
        }
        return queryPairs;
    }
}