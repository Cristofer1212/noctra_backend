package modules.event.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import config.DbConnection;
import config.exception.DatabaseConnectionException;
import modules.event.model.Event;

public class EventRepository implements IEventRepository {

  @Override
  public void createEvent(Event event) throws DatabaseConnectionException {

    String sql = "INSERT INTO event (user_id, name, address, start_date, end_date, capacity, state) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection connection = DbConnection.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      preparedStatement.setInt(1, event.getUserId());
      preparedStatement.setString(2, event.getName());
      preparedStatement.setString(3, event.getAddress());
      preparedStatement.setObject(4, event.getStartDate());
      preparedStatement.setObject(5, event.getEndDate());
      preparedStatement.setInt(6, event.getCapacity());
      preparedStatement.setString(7, event.getState());

      preparedStatement.executeUpdate();

    } catch (SQLException e) {

      throw new DatabaseConnectionException("Error al registrar el evento:  " + event.getName() + " " + e.getMessage(),
          e);

    }

  }

}
