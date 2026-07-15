USE Noctra_MVP;

DELIMITER //

-- Obtener métricas de asistencia de un evento específico
CREATE PROCEDURE GetEventMetrics(IN p_event_id INT)
BEGIN
    SELECT
        COUNT(i.id) AS total_invitados,
        SUM(CASE WHEN s.movement_type = 'ENTRADA' THEN 1 ELSE 0 END) AS presentes,
        (SELECT COUNT(*) FROM invitation WHERE event_id = p_event_id AND state = 'SIN_USAR') AS pendientes
    FROM invitation i
             LEFT JOIN scan s ON i.id = s.invitation_id
    WHERE i.event_id = p_event_id;
END //

DELIMITER ;