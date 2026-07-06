package modules.shared.integrations.meta_webhook;

public interface IWebhookHandler {

    // Para el GET de verificación
    String handleVerification(String mode, String token, String challenge);

    // Para el POST de mensajes entrantes
    void handleIncomingMessage(String payload);
}
