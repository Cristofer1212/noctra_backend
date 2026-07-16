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
        // 1. Validaciones
        if (dni == null || dni.isBlank()) return "El DNI no puede estar vacío.";
        if (telefono == null || telefono.isBlank()) return "El número telefónico no puede estar vacío.";

        // 2. Lógica de limpieza
        String telefonoSoloNumeros = telefono.replaceAll("[^0-9]", "");
        if (telefonoSoloNumeros.length() == 9) {
            telefonoSoloNumeros = "51" + telefonoSoloNumeros;
        }

        // 3. Llamar al servicio con depuración
        try {
            String nombreEvento = event.getName();
            String nombreRemitente = event.getName();

            // --- BLOQUE DE DEPURACIÓN ---
            System.out.println("--- DEPURACIÓN AddStaffController ---");
            System.out.println("DNI enviado: " + dni);
            System.out.println("Teléfono formateado para Meta: " + telefonoSoloNumeros);
            System.out.println("Nombre del evento: " + nombreEvento);
            System.out.println("Remitente: " + nombreRemitente);
            System.out.println("-------------------------------------");
            // ----------------------------

            PorteroCreateDto dto = new PorteroCreateDto(dni, telefonoSoloNumeros);

            // Aquí es donde el servicio debería estar recibiendo los datos
            porteroService.registrarPortero(dto, nombreEvento, nombreRemitente);

            return null; // Éxito
        } catch (Exception e) {
            System.err.println("Error crítico en AddStaffController: " + e.getMessage());
            e.printStackTrace();
            return "Error al enviar WhatsApp: " + e.getMessage();
        }
    }
}