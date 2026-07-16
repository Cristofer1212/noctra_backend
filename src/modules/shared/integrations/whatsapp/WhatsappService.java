package modules.shared.integrations.whatsapp;


import modules.shared.config.ConfigLoader;
import modules.shared.http.HttpClientWrapper;

import java.net.http.HttpClient;

public class WhatsappService implements IWhatsappService {

    private final HttpClientWrapper httpClient; // Tu cliente en shared/http
    private final String apiUrl;
    private final String token;

    public WhatsappService(HttpClientWrapper httpClient) {
        this.httpClient = httpClient;
        this.apiUrl = ConfigLoader.getProperty("whatsapp.api.url");
        this.token = ConfigLoader.getProperty("whatsapp.access.token");
    }

    @Override
    public void sendInvitation(String phoneNumber, String qrUrl, String nombre, String evento, String fechaInicio, String fechaFin) {
        // templat meta
        String jsonPayload = String.format("""
            {
                "messaging_product": "whatsapp",
                "to": "%s",
                "type": "template",
                "template": {
                    "name": "bievenida_qr",
                    "language": { "code": "es" },
                    "components": [
                        {
                            "type": "header",
                            "parameters": [
                                { "type": "image", "image": { "link": "%s" } }
                            ]
                        },
                        {
                            "type": "body",
                            "parameters": [
                                { "type": "text", "text": "%s" },
                                { "type": "text", "text": "%s" },
                                { "type": "text", "text": "%s" },
                                { "type": "text", "text": "%s" },
                                { "type": "text", "text": "%s" },
                                { "type": "text", "text": "%s" }
                            ]
                        }
                    ]
                }
            }
            """, phoneNumber, qrUrl, nombre, evento, fechaInicio, fechaFin, fechaInicio, fechaFin);

        httpClient.post(apiUrl, jsonPayload, token);
    }

    @Override
    public void sendPorteroAsignado(String nombreStaff, String numCelular, String nombreEvento, String nombreRemitente) {
        // numCelular ya se usó arriba en "to", no lo metas en los parámetros del body
        String jsonPayload = String.format("""
    {
      "messaging_product": "whatsapp",
      "to": "%s",
      "type": "template",
      "template": {
        "name": "portero_asignado",
        "language": { "code": "es" },
        "components": [
          {
            "type": "body",
            "parameters": [
              { "type": "text", "text": "%s" },
              { "type": "text", "text": "%s" },
              { "type": "text", "text": "%s" }
            ]
          }
        ]
      }
    }
    """, numCelular, nombreStaff, nombreEvento, nombreRemitente); // Orden correcto: staff, evento, remitente

        httpClient.post(apiUrl, jsonPayload, token);
    }


}
