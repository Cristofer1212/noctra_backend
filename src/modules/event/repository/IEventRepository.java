package modules.event.repository;

import config.exception.DatabaseConnectionException;
import modules.event.dto.CreateEventDto;
import modules.event.model.Event;

import java.util.List;
import java.util.Optional;

public interface IEventRepository {

  void createEvent(Event event) throws DatabaseConnectionException;

  Optional<Event> findById(Integer id) throws DatabaseConnectionException;

  List<Event> getEventsByUserId(Integer userId) throws DatabaseConnectionException;

  void deleteEvent(Integer id) throws DatabaseConnectionException;
}
