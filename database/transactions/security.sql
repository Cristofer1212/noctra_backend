        DELIMITER //

        CREATE PROCEDURE AssignStaffToEvent(
            IN p_event_id INT,
            IN p_user_id INT,
            IN p_assigned_by INT
        )
        BEGIN
            DECLARE exit handler for sqlexception
            BEGIN
                -- Si ocurre un error, deshacemos todos los cambios
                ROLLBACK;
            END;

                    START TRANSACTION;
            -- Aquí irían las validaciones y el INSERT
            INSERT INTO event_member (event_id, user_id, rol, assigned_by)
            VALUES (p_event_id, p_user_id, 'STAFF', p_assigned_by);

            -- Si todo sale bien, confirmamos
            COMMIT;
        END //

DELIMITER;