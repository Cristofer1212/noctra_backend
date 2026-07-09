package modules.invitation.repository;


import config.exception.DatabaseConnectionException;
import modules.invitation.model.Invitation;

import java.util.Optional;

public interface IInvitationRepository {

    void save(Invitation invitation) throws DatabaseConnectionException;

    Optional<Invitation> findByToken(String token) throws DatabaseConnectionException;

    void update(Invitation invitation) throws DatabaseConnectionException;

}
