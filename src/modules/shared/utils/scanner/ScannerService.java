package modules.shared.utils.scanner;

import config.exception.DatabaseConnectionException;
import modules.invitation.repository.IInvitationRepository;

public class ScannerService implements IScanner {

    private final IInvitationRepository invitationRepository;

    public ScannerService(IInvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    @Override
    public boolean scanQrInvitation(String token) {
        if (token == null) return false;

        // Extraer token de url larga
        String tokenLimpio = token;
        if (token.contains("token=")) {
            tokenLimpio = token.split("token=")[1];
        }

        tokenLimpio = tokenLimpio.trim();
        System.out.println("DEBUG: Token extraído para buscar: '" + tokenLimpio + "'");

        try {
            return invitationRepository.findByToken(tokenLimpio)
                    .map(invitacion -> {
                        System.out.println("DEBUG: Invitación encontrada: " + invitacion.getId() + " - Estado: " + invitacion.getState());

                        if ("SIN_USAR".equals(invitacion.getState())) {
                            invitacion.setState("USADO");
                            try {
                                invitationRepository.update(invitacion);
                                System.out.println("DEBUG: Estado actualizado a USADO correctamente.");
                                return true;
                            } catch (DatabaseConnectionException e) {
                                System.err.println("DEBUG: Error al actualizar BD: " + e.getMessage());
                                return false;
                            }
                        }
                        System.out.println("DEBUG: El estado no es SIN_USAR, es: " + invitacion.getState());
                        return false;
                    })
                    .orElseGet(() -> {
                        System.out.println("DEBUG: ¡Token NO encontrado en la base de datos!");
                        return false;
                    });

        } catch (DatabaseConnectionException e) {
            System.err.println("DEBUG: Error grave de BD: " + e.getMessage());
            return false;
        }
    }
}