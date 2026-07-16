package modules.shared.utils.scanner;

import config.exception.DatabaseConnectionException;
import modules.event.model.Event;
import modules.event.repository.IEventRepository;
import modules.invitation.repository.IInvitationRepository;
import modules.scan.model.Scan;
import modules.scan.repository.ScanRepository;

import java.util.Optional;

public class ScannerService implements IScanner {

    private final IInvitationRepository invitationRepository;
    private final ScanRepository scanRepository;
    private final IEventRepository eventRepository;

    public ScannerService(IInvitationRepository invitationRepository, ScanRepository scanRepository, IEventRepository eventRepository) {
        this.invitationRepository = invitationRepository;
        this.scanRepository = scanRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public boolean scanQrInvitation(String token, Integer porteroId) {
        if (token == null) return false;

        // 1. Limpieza y extracción del token
        String tokenLimpio = token;
        if (token.contains("token=")) {
            tokenLimpio = token.split("token=")[1];
        }
        tokenLimpio = tokenLimpio.trim();
        System.out.println("DEBUG: Token extraído para buscar: '" + tokenLimpio + "'");

        try {
            return invitationRepository.findByToken(tokenLimpio)
                    .map(invitacion -> {
                        System.out.println("DEBUG: Invitación encontrada: " + invitacion.getId());

                        // 2. Validación de Fecha del Evento
                        Optional<Event> eventoOpt = Optional.empty();
                        try {
                            eventoOpt = eventRepository.findById(invitacion.getEventId());
                        } catch (DatabaseConnectionException e) {
                            throw new RuntimeException(e);
                        }

                        java.time.LocalDateTime ahora = java.time.LocalDateTime.now();

                        // Si el evento existe, validamos si terminó
                        if (eventoOpt.isPresent()) {
                            Event evento = eventoOpt.get();
                            if (evento.getEndDate() != null && ahora.isAfter(evento.getEndDate())) {
                                System.err.println("DEBUG: Acceso denegado. El evento finalizó el: " + evento.getEndDate());
                                return false; // El QR no es válido si el evento ya pasó
                            }
                        }

                        // 3. Lógica de activación (Solo si es la primera vez)
                        if ("SIN_USAR".equals(invitacion.getState())) {
                            invitacion.setState("USADO");
                            try {
                                invitationRepository.update(invitacion);
                            } catch (DatabaseConnectionException e) {
                                System.err.println("DEBUG: Error al actualizar estado: " + e.getMessage());
                                return false;
                            }
                        }

                        // 4. Registro de movimiento
                        try {
                            Scan scan = new Scan(invitacion.getId(), porteroId, "Entry", "SUCCESS");
                            scanRepository.save(scan);
                            return true;
                        } catch (DatabaseConnectionException e) {
                            System.err.println("DEBUG: Error al registrar log: " + e.getMessage());
                            return false;
                        }
                    })
                    .orElseGet(() -> {
                        System.out.println("DEBUG: ¡Token NO encontrado!");
                        return false;
                    });

        } catch (DatabaseConnectionException e) {
            System.err.println("DEBUG: Error grave de BD: " + e.getMessage());
            return false;
        }
    }
}