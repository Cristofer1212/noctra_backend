-- Restricciones no agregadas en schema

-- ========================================================
-- CATEGORÍA A: RESTRICCIONES DE UNICIDAD (UNIQUE CONSTRAINTS)
-- ========================================================

-- 1. Un DNI de usuario no puede repetirse jamás en el sistema (Es tu clave de acceso estilo Yape)
IF NOT EXISTS (SELECT * FROM sys.objects WHERE name = 'UQ_user_dni' AND type = 'UQ')
    BEGIN
        ALTER TABLE [user] ADD CONSTRAINT [UQ_user_dni] UNIQUE ([dni]);
    END;

-- 2. Un mismo usuario no puede ser agregado como colaborador más de una vez en el mismo evento
IF NOT EXISTS (SELECT * FROM sys.objects WHERE name = 'UQ_event_member_unique' AND type = 'UQ')
    BEGIN
        ALTER TABLE [event_member] ADD CONSTRAINT [UQ_event_member_unique] UNIQUE ([event_id], [user_id]);
    END;

-- 3. Una misma persona (guest_id) solo puede tener una única invitación para un evento específico
IF NOT EXISTS (SELECT * FROM sys.objects WHERE name = 'UQ_invitation_event_guest' AND type = 'UQ')
    BEGIN
        ALTER TABLE [invitation] ADD CONSTRAINT [UQ_invitation_event_guest] UNIQUE ([event_id], [guest_id]);
    END;

-- 4. El código QR generado debe ser único a nivel global del sistema por seguridad
IF NOT EXISTS (SELECT * FROM sys.objects WHERE name = 'UQ_invitation_code_qr' AND type = 'UQ')
    BEGIN
        ALTER TABLE [invitation] ADD CONSTRAINT [UQ_invitation_code_qr] UNIQUE ([code_qr]);
    END;


-- ========================================================
-- CATEGORÍA B: RESTRICCIONES DE RANGO Y VALIDACIÓN LOGICA (CHECK CONSTRAINTS)
-- ========================================================

-- 1. La capacidad de un evento debe ser un número estrictamente positivo
IF NOT EXISTS (SELECT * FROM sys.objects WHERE name = 'CK_event_capacity' AND type = 'C')
    BEGIN
        ALTER TABLE [event] ADD CONSTRAINT [CK_event_capacity] CHECK ([capacity] > 0);
    END;

-- 2. Coherencia de Fechas: La fecha de fin del evento no puede ocurrir antes de la fecha de inicio
IF NOT EXISTS (SELECT * FROM sys.objects WHERE name = 'CK_event_dates' AND type = 'C')
    BEGIN
        ALTER TABLE [event] ADD CONSTRAINT [CK_event_dates] CHECK ([end_date] >= [start_date]);
    END;


-- ========================================================
-- CATEGORÍA C: RESTRICCIONES DE ESTADOS Y ROLES LIMITADOS (CHECK LISTS)
-- ========================================================

-- 1. El rol asignado a un miembro del evento solo puede ser uno de los permitidos por el negocio
IF NOT EXISTS (SELECT * FROM sys.objects WHERE name = 'CK_event_member_rol' AND type = 'C')
    BEGIN
        ALTER TABLE [event_member] ADD CONSTRAINT [CK_event_member_rol] CHECK ([rol] IN ('Collaborator', 'Partner', 'Staff'));
    END;

-- 2. El estado de la invitación debe regirse estrictamente bajo el flujo del negocio para métricas reales
IF NOT EXISTS (SELECT * FROM sys.objects WHERE name = 'CK_invitation_state' AND type = 'C')
    BEGIN
        ALTER TABLE [invitation] ADD CONSTRAINT [CK_invitation_state] CHECK ([state] IN ('Pending', 'Sent', 'Used', 'Cancelled'));
    END;

-- 3. Los tipos de movimiento registrados en el escáner deben limitarse a acciones lógicas de puerta
IF NOT EXISTS (SELECT * FROM sys.objects WHERE name = 'CK_scan_movement_type' AND type = 'C')
    BEGIN
        ALTER TABLE [scan] ADD CONSTRAINT [CK_scan_movement_type] CHECK ([movement_type] IN ('Entry', 'Re-entry'));
    END;

-- 4. El resultado de un escaneo solo admite las respuestas estándar de validación del sistema
IF NOT EXISTS (SELECT * FROM sys.objects WHERE name = 'CK_scan_result' AND type = 'C')
    BEGIN
        ALTER TABLE [scan] ADD CONSTRAINT [CK_scan_result] CHECK ([result] IN ('Success', 'Failed_Already_Used', 'Failed_Invalid_QR', 'Failed_Expired'));
    END;