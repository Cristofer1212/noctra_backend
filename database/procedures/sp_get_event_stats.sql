USE Noctra_MVP;

DROP PROCEDURE IF EXISTS sp_get_event_stats;

DELIMITER //

CREATE PROCEDURE sp_get_event_stats(IN p_event_id INT)
BEGIN
    -- 1. Declarar variables crudas
    DECLARE v_total_asistentes INT DEFAULT 0;
    DECLARE v_recurrentes INT DEFAULT 0;
    DECLARE v_nuevos INT DEFAULT 0;
    DECLARE v_reingreso_mujeres INT DEFAULT 0;
    DECLARE v_reingreso_hombres INT DEFAULT 0;
    DECLARE v_hombres INT DEFAULT 0;
    DECLARE v_mujeres INT DEFAULT 0;
    DECLARE v_hora_pico INT DEFAULT -1;
    DECLARE v_total_invitaciones INT DEFAULT 0;
    
    -- Variables para cálculos
    DECLARE v_primero_scan_at DATETIME DEFAULT NULL;
    DECLARE v_ultimo_scan_at DATETIME DEFAULT NULL;
    DECLARE v_total_entradas INT DEFAULT 0;
    DECLARE v_minutos_transcurridos INT DEFAULT 0;

    -- 2. Declarar variables formateadas para la UI (Mapeo exacto al Dashboard)
    DECLARE out_momento_mayor_gente VARCHAR(10) DEFAULT 'N/A';
    DECLARE out_personas_por_minuto INT DEFAULT 0;
    DECLARE out_ratio_genero VARCHAR(15) DEFAULT 'N/A';
    DECLARE out_porcentaje_nuevos INT DEFAULT 0;
    DECLARE out_porcentaje_recurrentes INT DEFAULT 0;

    -- =================================================================
    -- CONSULTAS DE EXTRACCIÓN
    -- =================================================================

    -- Total Asistentes
    SELECT COUNT(DISTINCT i.guest_id) INTO v_total_asistentes
    FROM scan s JOIN invitation i ON s.invitation_id = i.id
    WHERE i.event_id = p_event_id AND s.result = 'SUCCESS' AND s.movement_type IN ('ENTRY', 'RE_ENTRY', 'Entry', 'Reingreso', 'ENTRADA');

    -- Total Invitaciones
    SELECT COUNT(*) INTO v_total_invitaciones
    FROM invitation
    WHERE event_id = p_event_id;

    -- Recurrentes
    SELECT COUNT(DISTINCT i.guest_id) INTO v_recurrentes
    FROM scan s JOIN invitation i ON s.invitation_id = i.id
    WHERE i.event_id = p_event_id AND s.result = 'SUCCESS' AND s.movement_type IN ('ENTRY', 'RE_ENTRY', 'Entry', 'Reingreso', 'ENTRADA')
      AND i.guest_id IN (SELECT guest_id FROM invitation WHERE event_id <> p_event_id);

    SET v_nuevos = v_total_asistentes - v_recurrentes;

    -- Reingresos Mujeres
    SELECT COUNT(*) INTO v_reingreso_mujeres
    FROM scan s JOIN invitation i ON s.invitation_id = i.id JOIN guest g ON i.guest_id = g.id
    WHERE i.event_id = p_event_id AND s.result = 'SUCCESS' AND s.movement_type IN ('RE_ENTRY', 'Reingreso')
      AND (LOWER(g.gender) LIKE '%fem%' OR LOWER(g.gender) LIKE '%muj%');

    -- Reingresos Hombres
    SELECT COUNT(*) INTO v_reingreso_hombres
    FROM scan s JOIN invitation i ON s.invitation_id = i.id JOIN guest g ON i.guest_id = g.id
    WHERE i.event_id = p_event_id AND s.result = 'SUCCESS' AND s.movement_type IN ('RE_ENTRY', 'Reingreso')
      AND (LOWER(g.gender) LIKE '%masc%' OR LOWER(g.gender) LIKE '%hom%');

    -- Hombres y Mujeres para el Ratio
    SELECT COUNT(DISTINCT i.guest_id) INTO v_hombres
    FROM scan s JOIN invitation i ON s.invitation_id = i.id JOIN guest g ON i.guest_id = g.id
    WHERE i.event_id = p_event_id AND s.result = 'SUCCESS' AND s.movement_type IN ('ENTRY', 'RE_ENTRY', 'Entry', 'Reingreso', 'ENTRADA')
      AND (LOWER(g.gender) LIKE '%masc%' OR LOWER(g.gender) LIKE '%hom%');

    SELECT COUNT(DISTINCT i.guest_id) INTO v_mujeres
    FROM scan s JOIN invitation i ON s.invitation_id = i.id JOIN guest g ON i.guest_id = g.id
    WHERE i.event_id = p_event_id AND s.result = 'SUCCESS' AND s.movement_type IN ('ENTRY', 'RE_ENTRY', 'Entry', 'Reingreso', 'ENTRADA')
      AND (LOWER(g.gender) LIKE '%fem%' OR LOWER(g.gender) LIKE '%muj%');

    -- Hora Pico
    SELECT HOUR(s.scan_at) INTO v_hora_pico
    FROM scan s JOIN invitation i ON s.invitation_id = i.id
    WHERE i.event_id = p_event_id AND s.result = 'SUCCESS' AND s.movement_type IN ('ENTRY', 'RE_ENTRY', 'Entry', 'Reingreso', 'ENTRADA')
    GROUP BY HOUR(s.scan_at) ORDER BY COUNT(*) DESC LIMIT 1;

    -- Datos para Personas por Minuto
    SELECT MIN(s.scan_at), MAX(s.scan_at), COUNT(*) INTO v_primero_scan_at, v_ultimo_scan_at, v_total_entradas
    FROM scan s JOIN invitation i ON s.invitation_id = i.id
    WHERE i.event_id = p_event_id AND s.result = 'SUCCESS' AND s.movement_type IN ('ENTRY', 'RE_ENTRY', 'Entry', 'Reingreso', 'ENTRADA');

    -- =================================================================
    -- LÓGICA DE NEGOCIO PARA LA INTERFAZ
    -- =================================================================

    -- 1. Hora pico (Si hay datos, formatea, si no, se queda en 'N/A')
    IF v_hora_pico != -1 THEN
        SET out_momento_mayor_gente = CONCAT(LPAD(v_hora_pico, 2, '0'), ':00');
    END IF;

    -- 2. Ratio Hombres:Mujeres
    IF v_mujeres > 0 THEN
        SET out_ratio_genero = CONCAT(ROUND(v_hombres / v_mujeres, 1), ':1');
    ELSEIF v_hombres > 0 THEN
        SET out_ratio_genero = CONCAT(v_hombres, ':0');
    END IF;

    -- 3. Personas por Minuto (Redondeado a entero porque tu UI no muestra decimales)
    IF v_primero_scan_at IS NOT NULL AND v_ultimo_scan_at IS NOT NULL THEN
        SET v_minutos_transcurridos = TIMESTAMPDIFF(MINUTE, v_primero_scan_at, v_ultimo_scan_at);
        IF v_minutos_transcurridos > 0 THEN
            SET out_personas_por_minuto = ROUND(v_total_entradas / v_minutos_transcurridos);
        ELSEIF v_total_entradas > 0 THEN
            SET out_personas_por_minuto = v_total_entradas;
        END IF;
    END IF;

    -- 4. Porcentajes de Asistentes (Gráfico de Dona)
    IF v_total_asistentes > 0 THEN
        SET out_porcentaje_nuevos = ROUND((v_nuevos / v_total_asistentes) * 100);
        SET out_porcentaje_recurrentes = ROUND((v_recurrentes / v_total_asistentes) * 100);
    END IF;

    -- =================================================================
    -- RETORNO PARA EL BACKEND (Columnas Listas para Mapear)
    -- =================================================================
    SELECT 
        out_momento_mayor_gente AS momento_mayor_gente,
        out_personas_por_minuto AS personas_por_minuto,
        out_ratio_genero AS ratio_hombres_mujeres,
        v_reingreso_mujeres AS reingreso_mujeres,
        v_reingreso_hombres AS reingreso_hombres,
        v_total_asistentes AS total_asistentes,
        v_nuevos AS asistentes_nuevos,
        out_porcentaje_nuevos AS porcentaje_nuevos,
        v_recurrentes AS asistentes_recurrentes,
        out_porcentaje_recurrentes AS porcentaje_recurrentes,
        v_total_invitaciones AS total_invitaciones;

END //

DELIMITER ;
