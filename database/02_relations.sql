-- Relaciones de la tabla event
ALTER TABLE event ADD CONSTRAINT FK_event_user FOREIGN KEY (user_id) REFERENCES user(id);

-- Relaciones de la tabla event_member
ALTER TABLE event_member ADD CONSTRAINT FK_event_member_event FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE;
ALTER TABLE event_member ADD CONSTRAINT FK_event_member_user FOREIGN KEY (user_id) REFERENCES user(id);
ALTER TABLE event_member ADD CONSTRAINT FK_event_member_assigned FOREIGN KEY (assigned_by) REFERENCES user(id);

-- Relaciones de la tabla invitation
ALTER TABLE invitation ADD CONSTRAINT FK_invitation_event FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE;
ALTER TABLE invitation ADD CONSTRAINT FK_invitation_issuer FOREIGN KEY (issuer_user_id) REFERENCES user(id);
ALTER TABLE invitation ADD CONSTRAINT FK_invitation_guest FOREIGN KEY (guest_id) REFERENCES guest(id);

-- Relaciones de la tabla scan
ALTER TABLE scan ADD CONSTRAINT FK_scan_invitation FOREIGN KEY (invitation_id) REFERENCES invitation(id) ON DELETE CASCADE;
ALTER TABLE scan ADD CONSTRAINT FK_scan_scanned_by FOREIGN KEY (scanned_by) REFERENCES user(id);