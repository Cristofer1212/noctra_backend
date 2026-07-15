package views.dashboard;

import modules.event.model.Event;
import modules.portero.dto.PorteroCreateDto;
import modules.portero.service.PorteroService;
import modules.shared.http.HttpClientWrapper;
import modules.shared.integrations.whatsapp.WhatsappService;

public class AddStaffController {

    private final PorteroService porteroService;


    public AddStaffController( ) {
        this.porteroService = new PorteroService(new WhatsappService(new HttpClientWrapper()));
    }
    public String enviarInvitacionStaff(Event event, String dni, String telefono) {
        // 1. Validaciones existentes
        if (dni == null || dni.isBlank()) return "El DNI no puede estar vacío.";
        if (telefono == null || telefono.isBlank()) return "El número telefónico no puede estar vacío.";
        if (!telefono.matches("\\d{9}")) return "El número telefónico debe tener 9 dígitos.";



        // 3. Llamar al servicio
        try {
            String nombreEvento = event.getName();
            String nombreRemitente = event.getName();
            PorteroCreateDto dto = new PorteroCreateDto(dni, telefono);
            porteroService.registrarPortero(dto, nombreEvento,  nombreRemitente);
            return null; // Éxito
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al enviar WhatsApp: " + e.getMessage();
        }
    }
}