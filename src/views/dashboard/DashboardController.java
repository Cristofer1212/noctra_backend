package views.dashboard;

import modules.event.model.Event;
import modules.event.service.EventService;
import modules.event.repository.EventRepository;
import modules.user.controller.UserController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    private final EventService eventService;

    public DashboardController() {
        this.eventService = new EventService(new EventRepository());
    }

    public void cargarEventosEnVista(DashboardView vista) {
        try {
            Integer idUsuario = UserController.idUsuarioLogueado;

            List<Event> todosLosEventos = eventService.getEventsByUserId(idUsuario);

            List<Event> eventosActivos = new ArrayList<>();
            List<Event> proximosEventos = new ArrayList<>();
            List<Event> eventosPasados = new ArrayList<>(); // Nueva lista

            LocalDateTime ahora = LocalDateTime.now();

            if (todosLosEventos != null) {
                for (Event evento : todosLosEventos) {
                    if (evento.isActive(ahora)) {
                        eventosActivos.add(evento);
                    } else if (evento.isUpcoming(ahora)) {
                        proximosEventos.add(evento);
                    } else if (evento.isPast(ahora)) {
                        eventosPasados.add(evento); // Clasificación de pasados
                    }
                }
            }

            // Enviamos las 3 listas a la vista
            vista.renderizarEventos(eventosActivos, proximosEventos, eventosPasados);

        } catch (Exception e) {
            System.err.println("Error al cargar y filtrar los eventos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}