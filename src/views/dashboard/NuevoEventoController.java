package views.dashboard;

import modules.event.model.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class NuevoEventoController {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("d/M/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("H:mm");

    /**
     * Valida el formulario y arma el Event. Por ahora NO lo guarda en la base
     * de datos (ver TODOs abajo) - solo confirma que los datos están bien formados.
     * @return null si todo OK, o un mensaje de error si algo falló.
     */
    public String crearEvento(String nombre, String fechaInicioTexto, String horaInicioTexto,
                              String fechaFinTexto, String horaFinTexto,
                              String direccion, String aforoTexto) {

        if (nombre.isBlank()) return "El nombre del evento no puede estar vacío.";
        if (direccion.isBlank()) return "La dirección no puede estar vacía.";
        if (fechaInicioTexto.isBlank() || horaInicioTexto.isBlank()) return "Falta la fecha u hora de inicio.";
        if (fechaFinTexto.isBlank() || horaFinTexto.isBlank()) return "Falta la fecha u hora final.";
        if (aforoTexto.isBlank()) return "El aforo no puede estar vacío.";

        int aforo;
        try {
            aforo = Integer.parseInt(aforoTexto);
            if (aforo <= 0) return "El aforo debe ser un número mayor a 0.";
        } catch (NumberFormatException e) {
            return "El aforo debe ser un número entero (ej: 80).";
        }

        LocalDateTime fechaInicio;
        LocalDateTime fechaFin;
        try {
            LocalDate diaInicio = LocalDate.parse(fechaInicioTexto, FORMATO_FECHA);
            LocalTime horaInicio = LocalTime.parse(horaInicioTexto, FORMATO_HORA);
            fechaInicio = LocalDateTime.of(diaInicio, horaInicio);

            LocalDate diaFin = LocalDate.parse(fechaFinTexto, FORMATO_FECHA);
            LocalTime horaFin = LocalTime.parse(horaFinTexto, FORMATO_HORA);
            fechaFin = LocalDateTime.of(diaFin, horaFin);
        } catch (DateTimeParseException e) {
            return "Formato de fecha/hora inválido. Usa dd/mm/aaaa y HH:mm (ej: 25/12/2026 y 20:00).";
        }

        if (!fechaFin.isAfter(fechaInicio)) {
            return "La fecha/hora final debe ser posterior a la de inicio.";
        }

        // TODO: Cristian - el userId=1 es un placeholder. Falta que el flujo de
        // login/dashboard le pase el ID del usuario real que inició sesión
        // (por ahora LoginView/DashboardView no cargan ese dato más allá del
        // nombre para el saludo). Revisar cómo propagar el User autenticado
        // desde UserService.login(...) hasta acá.
        int userIdPlaceholder = 1;

        Event nuevoEvento = new Event(userIdPlaceholder, nombre, direccion, fechaInicio, fechaFin, aforo);

        // OJO: NO hace falta lógica extra para decidir si el evento es "Activo"
        // o "Próximo" - eso ya lo resuelven solos Event.isActive(hoy) e
        // isUpcoming(hoy) comparando fechaInicio/fechaFin contra la fecha de hoy,
        // y el DashboardView ya los usa así. En cuanto el evento se guarde con
        // su fecha real, va a aparecer en la sección correcta automáticamente.

        // TODO: Cristian - acá falta la persistencia real, algo como:
        // eventRepository.save(nuevoEvento);
        // (o vía un EventService si seguimos el mismo patrón que UserService).
        // Además, el DashboardView.obtenerEventosDeEjemplo() debe reemplazarse
        // por una consulta real (ej. eventService.findByUserId(userId)) para que
        // los eventos creados aquí realmente aparezcan en la lista del Dashboard.
        System.out.println("[NUEVO EVENTO - validado, falta guardar] " + nuevoEvento.getName()
                + " | " + nuevoEvento.getStartDate() + " -> " + nuevoEvento.getEndDate()
                + " | Aforo: " + nuevoEvento.getCapacity());

        return null; // validación exitosa
    }
}