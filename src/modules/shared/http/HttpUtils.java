package modules.shared.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class HttpUtils {


    public static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    // De Bytes a String
    public static  String readRequestBody(HttpExchange exchange) throws IOException {
        try (
                InputStream inputStream = exchange.getRequestBody();
                InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr)) {

            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                jsonBuilder.append(line);
            }

            return jsonBuilder.toString();
        }
    }

}