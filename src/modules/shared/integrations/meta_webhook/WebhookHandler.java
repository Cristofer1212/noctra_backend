package modules.shared.integrations.meta_webhook;



public class WebhookHandler implements IWebhookHandler {

    private final String VERIFY_TOKEN = "mi_token_secreto_para_whatsapp_123";

    @Override
    public String handleVerification(String mode, String token, String challenge) {
        if ("subscribe".equals(mode) && VERIFY_TOKEN.equals(token)) {
            return challenge; // Esto es lo que Meta necesita recibir
        }
        throw new RuntimeException("Token de verificación inválido");
    }

    @Override
    public void handleIncomingMessage(String payload) {
        // Aquí procesarás los mensajes entrantes en la semana 17
        System.out.println("Evento recibido: " + payload);

    }
}
