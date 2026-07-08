package modules.event.repository;

import config.exception.DatabaseConnectionException;
import modules.event.dto.CreateEventDto;
import modules.event.model.Event;

public interface IEventRepository {

  void createEvent(Event event) throws DatabaseConnectionException;

}
