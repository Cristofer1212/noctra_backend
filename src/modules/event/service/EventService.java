package modules.event.service;

import config.exception.DatabaseConnectionException;
import modules.event.dto.CreateEventDto;
import modules.event.model.Event;
import modules.event.repository.IEventRepository;
import java.util.List; // Importación necesaria

public class EventService {

  private final IEventRepository eventRepository;

  public EventService(IEventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  public void createEventService(CreateEventDto dto) throws DatabaseConnectionException {

    Event event = new Event(
            dto.getUserId(),
            dto.getName(),
            dto.getAddress(),
            dto.getStartDate(),
            dto.getEndDate(),
            dto.getCapacity()
    );

    this.eventRepository.createEvent(event);
  }

  // ESTE ES EL MeTODO NUEVO QUE CONECTA EL CONTROLADOR CON EL REPOSITORIO
  public List<Event> getEventsByUserId(Integer userId) throws DatabaseConnectionException {
    return this.eventRepository.getEventsByUserId(userId);
  }
}