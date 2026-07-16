package views.dashboard;

import modules.event.model.Event;
import modules.shared.http.HttpClientWrapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class NuevoEventoController {
    // Necesitas una instancia del cliente HTTP
    private final HttpClientWrapper httpClient = new HttpClientWrapper();

    public String crearEvento(String nombre, String fechaInicio, String horaInicio, String fechaFin, String horaFin, String direccion) {
        // 1. Validación de campos (lo que ya tienes)
        if (nombre.isBlank()) return "El nombre es obligatorio.";

        try {
            // 2. Construir el JSON manualmente o usando un Map/DTO
            // Nota: Debes combinar fecha y hora en el formato que espera tu backend (dd/MM/yyyy HH:mm:ss)
            String start = fechaInicio + " " + horaInicio + ":00";
            String end = fechaFin + " " + horaFin + ":00";

            String json = String.format(
                    "{\"name\":\"%s\", \"address\":\"%s\", \"startDate\":\"%s\", \"endDate\":\"%s\", \"capacity\":0, \"state\":\"ACTIVO\"}",
                    nombre, direccion, start, end
            );

            // 3. Enviar la petición al servidor (asegúrate de usar la URL completa)
            String response = httpClient.post("http://localhost:8080/event", json, null);
            if (response != null && response.contains("éxito")) {
                return null;
            } else {
                return response; // Si falla, devuelve el mensaje de error real
            }
        } catch (Exception e) {
            return "Error de conexión: " + e.getMessage();
        }
    }
}