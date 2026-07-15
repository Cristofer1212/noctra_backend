        DELIMITER //

        -- Al insertar un escaneo, actualizamos automáticamente el estado de la invitación
        CREATE TRIGGER trg_update_invitation_state
            AFTER INSERT ON scan
                            FOR EACH ROW
        BEGIN
            IF NEW.movement_type = 'ENTRADA' THEN
                UPDATE invitation SET state = 'USADO' WHERE id = NEW.invitation_id;
        END IF;
        END //

DELIMITER ; ;