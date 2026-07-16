USE Noctra_MVP;

DROP PROCEDURE IF EXISTS sp_get_event_stats;

DELIMITER //

CREATE PROCEDURE sp_get_event_stats(IN p_event_id INT)
BEGIN
    -- Declarar variables para almacenar resultados
    DECLARE v_total_asistentes INT DEFAULT 0;
    DECLARE v_recurrentes INT DEFAULT 0;
    DECLARE v_reingreso_mujeres INT DEFAULT 0;
    DECLARE v_reingreso_hombres INT DEFAULT 0;
    DECLARE v_hombres INT DEFAULT 0;
    DECLARE v_mujeres INT DEFAULT 0;
    DECLARE v_hora_pico INT DEFAULT -1;
    DECLARE v_entrando_15m INT DEFAULT 0;
    DECLARE v_total_entradas_ppm INT DEFAULT 0;
    DECLARE v_primero_scan_at DATETIME DEFAULT NULL;
    DECLARE v_ultimo_scan_at DATETIME DEFAULT NULL;

    -- 1. Total Asistentes (distintos guests con escaneo exitoso)
    SELECT COUNT(DISTINCT i.guest_id) INTO v_total_asistentes
    FROM scan s
    JOIN invitation i ON s.invitation_id = i.id
    WHERE i.event_id = p_event_id 
      AND s.result = 'SUCCESS'
      AND s.movement_type IN ('ENTRY', 'RE_ENTRY', 'Entry', 'Reingreso', 'ENTRADA');

    -- 2. Recurrentes (asistentes que tienen invitaciones a otros eventos)
    SELECT COUNT(DISTINCT i.guest_id) INTO v_recurrentes
    FROM scan s
    JOIN invitation i ON s.invitation_id = i.id
    WHERE i.event_id = p_event_id 
      AND s.result = 'SUCCESS'
      AND s.movement_type IN ('ENTRY', 'RE_ENTRY', 'Entry', 'Reingreso', 'ENTRADA')
      AND i.guest_id IN (SELECT guest_id FROM invitation WHERE event_id <> p_event_id);

    -- 3. Reingresos Mujeres
    SELECT COUNT(*) INTO v_reingreso_mujeres
    FROM scan s
    JOIN invitation i ON s.invitation_id = i.id
    JOIN guest g ON i.guest_id = g.id
    WHERE i.event_id = p_event_id 
      AND s.result = 'SUCCESS'
      AND s.movement_type IN ('RE_ENTRY', 'Reingreso')
      AND (LOWER(g.gender) LIKE '%fem%' OR LOWER(g.gender) LIKE '%muj%');

    -- 4. Reingresos Hombres
    SELECT COUNT(*) INTO v_reingreso_hombres
    FROM scan s
    JOIN invitation i ON s.invitation_id = i.id
    JOIN guest g ON i.guest_id = g.id
    WHERE i.event_id = p_event_id 
      AND s.result = 'SUCCESS'
      AND s.movement_type IN ('RE_ENTRY', 'Reingreso')
      AND (LOWER(g.gender) LIKE '%masc%' OR LOWER(g.gender) LIKE '%hom%');

    -- 5. Hombres (total asistentes de género masculino)
    SELECT COUNT(DISTINCT i.guest_id) INTO v_hombres
    FROM scan s
    JOIN invitation i ON s.invitation_id = i.id
    JOIN guest g ON i.guest_id = g.id
    WHERE i.event_id = p_event_id 
      AND s.result = 'SUCCESS'
      AND s.movement_type IN ('ENTRY', 'RE_ENTRY', 'Entry', 'Reingreso', 'ENTRADA')
      AND (LOWER(g.gender) LIKE '%masc%' OR LOWER(g.gender) LIKE '%hom%');

    -- 6. Mujeres (total asistentes de género femenino)
    SELECT COUNT(DISTINCT i.guest_id) INTO v_mujeres
    FROM scan s
    JOIN invitation i ON s.invitation_id = i.id
    JOIN guest g ON i.guest_id = g.id
    WHERE i.event_id = p_event_id 
      AND s.result = 'SUCCESS'
      AND s.movement_type IN ('ENTRY', 'RE_ENTRY', 'Entry', 'Reingreso', 'ENTRADA')
      AND (LOWER(g.gender) LIKE '%fem%' OR LOWER(g.gender) LIKE '%muj%');

    -- 7. Hora Pico (momento de mayor gente)
    SELECT HOUR(s.scan_at) INTO v_hora_pico
    FROM scan s
    JOIN invitation i ON s.invitation_id = i.id
    WHERE i.event_id = p_event_id 
      AND s.result = 'SUCCESS'
      AND s.movement_type IN ('ENTRY', 'RE_ENTRY', 'Entry', 'Reingreso', 'ENTRADA')
    GROUP BY HOUR(s.scan_at)
    ORDER BY COUNT(*) DESC
    LIMIT 1;

    -- 8. Personas entrando en los últimos 15 minutos
    SELECT COUNT(*) INTO v_entrando_15m
    FROM scan s
    JOIN invitation i ON s.invitation_id = i.id
    WHERE i.event_id = p_event_id 
      AND s.result = 'SUCCESS'
      AND s.movement_type IN ('ENTRY', 'RE_ENTRY', 'Entry', 'Reingreso', 'ENTRADA')
      AND s.scan_at >= DATE_SUB(NOW(), INTERVAL 15 MINUTE);

    -- 9. Primero y último scan_at, y total entradas para ppm fallback
    SELECT MIN(s.scan_at), MAX(s.scan_at), COUNT(*)
    INTO v_primero_scan_at, v_ultimo_scan_at, v_total_entradas_ppm
    FROM scan s
    JOIN invitation i ON s.invitation_id = i.id
    WHERE i.event_id = p_event_id 
      AND s.result = 'SUCCESS'
      AND s.movement_type IN ('ENTRY', 'RE_ENTRY', 'Entry', 'Reingreso', 'ENTRADA');

    -- Retornar todos los resultados en una única fila
    SELECT
        v_total_asistentes AS total_asistentes,
        v_recurrentes AS recurrentes,
        (v_total_asistentes - v_recurrentes) AS nuevos,
        v_reingreso_mujeres AS reingreso_mujeres,
        v_reingreso_hombres AS reingreso_hombres,
        v_hombres AS hombres,
        v_mujeres AS mujeres,
        v_hora_pico AS hora_pico,
        v_entrando_15m AS entrando_15m,
        v_total_entradas_ppm AS total_entradas_ppm,
        v_primero_scan_at AS primero_scan_at,
        v_ultimo_scan_at AS ultimo_scan_at;
END //

DELIMITER ;
