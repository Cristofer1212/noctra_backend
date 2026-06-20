-- Funciones de Agregación

-- Contar eventos por estado
-- 1. Primero, asegúrate de cambiar a la base de datos 'master'


USE Noctra_MVP
select * from  [user]

-- Cuenta cuántos escaneos exitosos de entrada ("Entry") se han realizado para el Evento ID = 1
SELECT
    COUNT(s.id) AS Total_Ingresos_Reales
FROM [scan] s
         INNER JOIN [invitation] i ON s.invitation_id = i.id
WHERE i.event_id = 1
  AND s.movement_type = 'Entry'
  AND s.result = 'Success';

-- Cuenta cuántas invitaciones exitosas ("Sent") emitió cada miembro del staff
SELECT
    u.id AS Colaborador_ID,
    u.name + ' ' + u.last_name AS Nombre_Colaborador,
    COUNT(i.id) AS Invitaciones_Enviadas
FROM [invitation] i
         INNER JOIN [user] u ON i.issuer_user_id = u.id
WHERE i.event_id = 1 AND i.state = 'Sent'
GROUP BY u.id, u.name, u.last_name
ORDER BY Invitaciones_Enviadas DESC;

-- Obtiene la hora exacta del primer y último ingreso exitoso del evento
SELECT
    MIN(scan_at) AS Hora_Primer_Ingreso,
    MAX(scan_at) AS Hora_Ultimo_Ingreso
FROM [scan] s
         INNER JOIN [invitation] i ON s.invitation_id = i.id
WHERE i.event_id = 1
  AND s.movement_type = 'Entry'
  AND s.result = 'Success';

-- Calcula el total de QRs generados vs cuántos se escanearon con éxito
SELECT
    COUNT(id) AS Total_Invitaciones_Creadas,
    COUNT(CASE WHEN state = 'Used' THEN 1 END) AS Total_Asistentes_Reales,
    -- Operación matemática básica de porcentaje
    (COUNT(CASE WHEN state = 'Used' THEN 1 END) * 100.0 / COUNT(id)) AS Porcentaje_Asistencia
FROM [invitation]
WHERE event_id = 1;

