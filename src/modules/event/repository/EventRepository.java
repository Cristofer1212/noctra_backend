package modules.event.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
      throw new DatabaseConnectionException("Error al registrar el evento:  " + event.getName() + " " + e.getMessage(), e);
    }
  }

  @Override
  public Optional<Event> findById(Integer id) throws DatabaseConnectionException {
    String sql = "SELECT * FROM event WHERE id = ?";

    try (Connection connection = DbConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      preparedStatement.setInt(1, id);

      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          Event event = new Event();
          event.setId(rs.getInt("id"));
          event.setUserId(rs.getInt("user_id"));
          event.setName(rs.getString("name"));
          event.setAddress(rs.getString("address"));
          event.setCapacity(rs.getInt("capacity"));
          event.setState(rs.getString("state"));

          // Mapeo seguro de fechas de MySQL a LocalDateTime
          if (rs.getTimestamp("start_date") != null) {
            event.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());
          }
          if (rs.getTimestamp("end_date") != null) {
            event.setEndDate(rs.getTimestamp("end_date").toLocalDateTime());
          }

          return Optional.of(event);
        }
      }
    } catch (SQLException e) {
      throw new DatabaseConnectionException("Error al buscar el evento con ID: " + id, e);
    }

    return Optional.empty();
  }


  @Override
  public List<Event> getEventsByUserId(Integer userId) throws DatabaseConnectionException {
    String sql = "SELECT * FROM event WHERE user_id = ?";
    List<Event> eventos = new ArrayList<>();

    try (Connection connection = DbConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      preparedStatement.setInt(1, userId);
      ResultSet rs = preparedStatement.executeQuery();

      while (rs.next()) {
        Event event = new Event();

        event.setId(rs.getInt("id"));
        event.setUserId(rs.getInt("user_id"));
        event.setName(rs.getString("name"));
        event.setAddress(rs.getString("address"));
        event.setCapacity(rs.getInt("capacity"));
        event.setState(rs.getString("state"));

        // Se extrae la fecha de MySQL como Timestamp y se pasa a LocalDateTime
        if (rs.getTimestamp("start_date") != null) {
          event.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());
        }
        if (rs.getTimestamp("end_date") != null) {
          event.setEndDate(rs.getTimestamp("end_date").toLocalDateTime());
        }

        eventos.add(event);
      }

    } catch (SQLException e) {
      throw new DatabaseConnectionException("Error al obtener los eventos del usuario: " + e.getMessage(), e);
    }

    return eventos;
  }
}