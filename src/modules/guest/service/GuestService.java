package modules.guest.service;

import config.exception.DatabaseConnectionException;
import modules.guest.model.Guest;
import modules.guest.repository.IGuestRepository;

import java.sql.SQLException;


public class GuestService {

    private final IGuestRepository iGuestRepository;
    public GuestService(IGuestRepository IGuestRepository) {
        this.iGuestRepository = IGuestRepository;
    }

    public Guest createGuest(String phone, String gender) throws DatabaseConnectionException {
        Guest guest = new Guest();
        guest.setPhone(phone);
        guest.setGender(gender);
        iGuestRepository.save(guest);

        return guest;
    }

}
