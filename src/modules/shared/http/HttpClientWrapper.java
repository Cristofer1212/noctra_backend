package modules.shared.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientWrapper {
    private final HttpClient client = HttpClient.newHttpClient();

    public void post(String url, String json, String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Respuesta API Meta: " + response.statusCode());
            System.out.println("Cuerpo de error de Meta: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
