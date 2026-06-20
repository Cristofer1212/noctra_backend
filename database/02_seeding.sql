/*
SET NOCOUNT ON;

-- 1. Desactivamos las restricciones de golpe
        EXEC sp_MSforeachtable 'ALTER TABLE ? NOCHECK CONSTRAINT ALL';

-- 2. Limpieza forzada usando DELETE SOLO DATOS. Barra tabla con DROP
        DELETE FROM [scan];
        DELETE FROM [invitation];
        DELETE FROM [event_member];
        DELETE FROM [event];
        DELETE FROM [guest];
        DELETE FROM [user];

-- 3. Reiniciamos los IDs
        DBCC CHECKIDENT ('[scan]', RESEED, 0);
        DBCC CHECKIDENT ('[invitation]', RESEED, 0);
        DBCC CHECKIDENT ('[event_member]', RESEED, 0);
        DBCC CHECKIDENT ('[event]', RESEED, 0);
        DBCC CHECKIDENT ('[guest]', RESEED, 0);
        DBCC CHECKIDENT ('[user]', RESEED, 0);

-- 3. REACTIVAR RESTRICCIONES
EXEC sp_MSforeachtable 'ALTER TABLE ? WITH CHECK CHECK CONSTRAINT ALL';

-- 4. INSERTAR DATOS
INSERT INTO [user] (dni, name, last_name, phone, address, mail, password, state)
VALUES
    ('77777777', 'Cristofer', 'Organizador', '987654321', 'Av. Las Flores 123', 'cristofer@noctra.com', '$2b$10$EncriptacionDePruebaOrganizador123', 'Active'),
    ('88888888', 'Juan', 'Portero', '912345678', 'Calle Los Pinos 456', 'juan.staff@noctra.com', '$2b$10$EncriptacionDePruebaPortero456', 'Active'),
    ('99999999', 'Admin', 'Sistema', '955555555', 'Oficina Central Noctra', 'admin@noctra.com', '$2b$10$EncriptacionDePruebaAdmin789', 'Active');


INSERT INTO [guest] (phone, gender, state)
VALUES
    ('999888777', 'Masculino', 'Active'),
    ('944333222', 'Femenino', 'Active');



INSERT INTO [event] (user_id, name, address, start_date, end_date, capacity, state)
VALUES
    (1, 'Noctra Grand Opening', 'Discoteca Aura, Larcomar', '2026-06-15 22:00:00', '2026-06-16 05:00:00', 300, 'Active');




INSERT INTO [event_member] (event_id, user_id, rol, assigned_by)
VALUES
    (1, 2, 'Portero/Staff', 3);



INSERT INTO [invitation] (event_id, issuer_user_id, guest_id, code_qr, state, sent_at, read_at)
VALUES
    (1, 1, 1, 'QR_CODE_PROMO_LUJO_001', 'Pending', '2026-05-24 10:00:00', '2026-05-24 10:15:00'),
    (1, 1, 2, 'QR_CODE_PROMO_VIP_002', 'Pending', '2026-05-24 11:00:00', NULL);



INSERT INTO [scan] (invitation_id, scanned_by, movement_type, result, scan_at)
VALUES
    (1, 2, 'Entrada', 'Error: QR Ilegible', '2026-06-15 23:15:00'),
    (1, 2, 'Entrada', 'Exitoso', '2026-06-15 23:16:00');
/*