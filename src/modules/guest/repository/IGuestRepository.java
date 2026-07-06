package modules.guest.repository;

import config.exception.DatabaseConnectionException;
import modules.guest.model.Guest;

public interface IGuestRepository {

    public void save(Guest guest) throws DatabaseConnectionException;
}
