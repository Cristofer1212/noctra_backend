package modules.invitation.repository;


import config.exception.DatabaseConnectionException;
import modules.invitation.model.Invitation;

public interface IInvitationRepository {

    void save(Invitation invitation) throws DatabaseConnectionException;



}
