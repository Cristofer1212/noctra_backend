package views.dashboard;

import modules.event.model.Event;

public class AddStaffController {

    /**
     * Valida los datos e "intenta" enviar el link de invitación al staff.
     * @return null si todo OK (por ahora solo valida el formato),
     *         o un mensaje de error si algo está mal.
     */
    public String enviarInvitacionStaff(Event event, String dni, String telefono) {
        if (dni == null || dni.isBlank()) {
            return "El DNI no puede estar vacío.";
        }
        if (telefono == null || telefono.isBlank()) {
            return "El número telefónico no puede estar vacío.";
        }
        if (!telefono.matches("\\d{9}")) {
            return "El número telefónico debe tener 9 dígitos.";
        }

        // TODO: Cristian - acá va la conexión real, algo como:
        // 1. staffRepository.save(new Staff(event.getId(), dni, telefono, "DOORMAN"));
        // 2. generar el link/QR de invitación (revisar si ya existe algo reusable
        //    de modules.shared.integrations.meta_webhook, ya que ahí está la
        //    integración de WhatsApp que se usa para las invitaciones normales)
        // 3. enviar el link al "telefono" vía la API de WhatsApp (meta_webhook)
        //
        // Por ahora esto NO llama a ningún servicio real, solo valida el formulario.
        System.out.println("[STAFF] Evento: " + event.getName() + " | DNI: " + dni + " | Tel: " + telefono);

        return null; // "éxito" (validación pasó)
    }
}