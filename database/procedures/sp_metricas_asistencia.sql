USE Noctra_MVP;

-- Reingresos | Lista de invitados que han reingresado más de una vez

DELIMITER //

CREATE PROCEDURE sp_get_top_reingresos()
BEGIN
    SELECT
        invitation_id,
        COUNT(*) as total_movimientos
    FROM scan
    WHERE movement_type = 'Entry'
    GROUP BY invitation_id
    HAVING total_movimientos > 1
    ORDER BY total_movimientos DESC;
END //

DELIMITER ;