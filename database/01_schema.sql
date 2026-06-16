-- ========================================================
-- 1. CREACIÓN DE TABLAS (MODIFICADAS PARA DESARROLLO / MVP)
-- ========================================================

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'user')
    BEGIN
        CREATE TABLE [user] (
                                [id] INT IDENTITY(1,1) PRIMARY KEY,
                                [dni] VARCHAR(20) NOT NULL,        -- Obligatorio en DTO
                                [name] VARCHAR(50) NOT NULL,       -- Obligatorio en DTO
                                [last_name] VARCHAR(50) NOT NULL,  -- Obligatorio en DTO
                                [phone] VARCHAR(11) NULL,          -- Opcional
                                [address] VARCHAR(255) NULL,       -- Opcional
                                [mail] VARCHAR(150) NULL,          -- Opcional
                                [password] VARCHAR(255) NULL,      -- MODIFICADO: Opcional para desarrollo
                                [state] VARCHAR(50) DEFAULT 'Active' NOT NULL,
                                [created_at] DATETIME DEFAULT GETDATE() NOT NULL,
                                [updated_at] DATETIME DEFAULT GETDATE() NOT NULL
        );
    END;

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'event')
    BEGIN
        CREATE TABLE [event] (
                                 [id] INT IDENTITY(1,1) PRIMARY KEY,
                                 [user_id] INT NOT NULL,
                                 [name] VARCHAR(50) NOT NULL,
                                 [address] VARCHAR(255) NOT NULL,
                                 [start_date] DATETIME NOT NULL,
                                 [end_date] DATETIME NOT NULL,
                                 [capacity] INT NOT NULL,
                                 [state] VARCHAR(50) DEFAULT 'Pending' NOT NULL,
                                 [created_at] DATETIME DEFAULT GETDATE() NOT NULL,
                                 [updated_at] DATETIME DEFAULT GETDATE() NOT NULL
        );
    END;

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'event_member')
    BEGIN
        CREATE TABLE [event_member] (
                                        [id] INT IDENTITY(1,1) PRIMARY KEY,
                                        [event_id] INT NOT NULL,
                                        [user_id] INT NOT NULL,
                                        [rol] VARCHAR(50) NOT NULL,
                                        [assigned_by] INT NOT NULL
        );
    END;

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'guest')
    BEGIN
        CREATE TABLE [guest] (
                                 [id] INT IDENTITY(1,1) PRIMARY KEY,
                                 [phone] VARCHAR(20) NOT NULL,
                                 [gender] VARCHAR(20) NOT NULL,
                                 [state] VARCHAR(50) DEFAULT 'Active' NOT NULL,
                                 [created_at] DATETIME DEFAULT GETDATE() NOT NULL,
                                 [updated_at] DATETIME DEFAULT GETDATE() NOT NULL
        );
    END;

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'invitation')
    BEGIN
        CREATE TABLE [invitation] (
                                      [id] INT IDENTITY(1,1) PRIMARY KEY,
                                      [event_id] INT NOT NULL,
                                      [issuer_user_id] INT NOT NULL,
                                      [guest_id] INT NOT NULL,
                                      [code_qr] VARCHAR(255) NOT NULL,
                                      [state] VARCHAR(50) DEFAULT 'Pending' NOT NULL,
                                      [created_at] DATETIME DEFAULT GETDATE() NOT NULL,
                                      [sent_at] DATETIME,
                                      [read_at] DATETIME
        );
    END;

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'scan')
    BEGIN
        CREATE TABLE [scan] (
                                [id] INT IDENTITY(1,1) PRIMARY KEY,
                                [invitation_id] INT NOT NULL,
                                [scanned_by] INT NOT NULL,
                                [movement_type] VARCHAR(50) NOT NULL,
                                [result] VARCHAR(100) NOT NULL,
                                [scan_at] DATETIME DEFAULT GETDATE() NOT NULL
        );
    END;

-- ========================================================
-- 2. RELACIONES (LLAVES FORÁNEAS)
-- ========================================================

IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_event_user')
    BEGIN
        ALTER TABLE [event] ADD CONSTRAINT [FK_event_user] FOREIGN KEY ([user_id]) REFERENCES [user]([id]);
    END;

IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_event_member_event')
    BEGIN
        ALTER TABLE [event_member] ADD CONSTRAINT [FK_event_member_event] FOREIGN KEY ([event_id]) REFERENCES [event]([id]);
    END;

IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_event_member_user')
    BEGIN
        ALTER TABLE [event_member] ADD CONSTRAINT [FK_event_member_user] FOREIGN KEY ([user_id]) REFERENCES [user]([id]);
    END;

IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_event_member_assigned')
    BEGIN
        ALTER TABLE [event_member] ADD CONSTRAINT [FK_event_member_assigned] FOREIGN KEY ([assigned_by]) REFERENCES [user]([id]);
    END;

IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_invitation_user')
    BEGIN
        ALTER TABLE [invitation] ADD CONSTRAINT [FK_invitation_user] FOREIGN KEY ([event_id]) REFERENCES [event]([id]);
    END;

IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_invitation_issuer')
    BEGIN
        ALTER TABLE [invitation] ADD CONSTRAINT [FK_invitation_issuer] FOREIGN KEY ([issuer_user_id]) REFERENCES [user]([id]);
    END;

IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_invitation_guest')
    BEGIN
        ALTER TABLE [invitation] ADD CONSTRAINT [FK_invitation_guest] FOREIGN KEY ([guest_id]) REFERENCES [guest]([id]);
    END;

IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_scan_invitation')
    BEGIN
        ALTER TABLE [scan] ADD CONSTRAINT [FK_scan_invitation] FOREIGN KEY ([invitation_id]) REFERENCES [invitation]([id]);
    END;

IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_scan_scanned_by')
    BEGIN
        ALTER TABLE [scan] ADD CONSTRAINT [FK_scan_scanned_by] FOREIGN KEY ([scanned_by]) REFERENCES [user]([id]);
    END;


