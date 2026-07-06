package modules.invitation.utils;

import modules.invitation.model.Invitation;

import java.time.LocalDateTime;

public class InvitationFactory {

    public static Invitation create(Integer guestId,String token, String codeQr) {
        Invitation ticket = new Invitation();
        // Asignación de IDs (Temporalmente hardcodeados hasta integración con otros módulos)
        ticket.setEventId(1);
        ticket.setIssuerUserId(1);
        // Asignación de datos reales
        ticket.setGuestId(guestId);
        ticket.setToken(token);
        ticket.setCodeQr(codeQr);
        // Valores por defecto
        ticket.setState("SIN_USAR");
        ticket.setCreatedAt(LocalDateTime.now());
        // Nulls
        ticket.setSentAt(null);
        ticket.setReadAt(null);

        return ticket;

    }


}
