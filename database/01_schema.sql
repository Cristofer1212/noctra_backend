-- ========================================================
-- 1. DESTRUCCIÓN (Orden inverso para evitar errores de llaves)
-- ========================================================
-- Primero borramos las tablas que dependen de otras (las que tienen Foreign Keys)
IF OBJECT_ID('scan', 'U') IS NOT NULL DROP TABLE [scan];
IF OBJECT_ID('invitation', 'U') IS NOT NULL DROP TABLE [invitation];
IF OBJECT_ID('guest', 'U') IS NOT NULL DROP TABLE [guest];
IF OBJECT_ID('event_member', 'U') IS NOT NULL DROP TABLE [event_member];
IF OBJECT_ID('event', 'U') IS NOT NULL DROP TABLE [event];
IF OBJECT_ID('[user]', 'U') IS NOT NULL DROP TABLE [user];

-- ========================================================
-- 2. CREACIÓN (Orden jerárquico: Tablas padres primero)
-- ========================================================

CREATE TABLE [user] (
                        [id] INT IDENTITY(1,1) PRIMARY KEY,
                        [dni] VARCHAR(20) NOT NULL UNIQUE, -- Agregado UNIQUE para evitar duplicados
                        [name] VARCHAR(50) NOT NULL,
                        [last_name] VARCHAR(50) NOT NULL,
                        [phone] VARCHAR(11) NULL,
                        [address] VARCHAR(255) NULL,
                        [mail] VARCHAR(150) NULL,
                        [password] VARCHAR(255) NOT NULL,
                        [state] VARCHAR(50) DEFAULT 'Active' NOT NULL,
                        [created_at] DATETIME DEFAULT GETDATE() NOT NULL,
                        [updated_at] DATETIME DEFAULT GETDATE() NOT NULL
);

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
                         [updated_at] DATETIME DEFAULT GETDATE() NOT NULL,
                         CONSTRAINT [FK_event_user] FOREIGN KEY ([user_id]) REFERENCES [user]([id])
);

CREATE TABLE [event_member] (
                                [id] INT IDENTITY(1,1) PRIMARY KEY,
                                [event_id] INT NOT NULL,
                                [user_id] INT NOT NULL,
                                [rol] VARCHAR(50) NOT NULL,
                                [assigned_by] INT NOT NULL,
                                CONSTRAINT [FK_event_member_event] FOREIGN KEY ([event_id]) REFERENCES [event]([id]),
                                CONSTRAINT [FK_event_member_user] FOREIGN KEY ([user_id]) REFERENCES [user]([id]),
                                CONSTRAINT [FK_event_member_assigned] FOREIGN KEY ([assigned_by]) REFERENCES [user]([id])
);

CREATE TABLE [guest] (
                         [id] INT IDENTITY(1,1) PRIMARY KEY,
                         [phone] VARCHAR(20) NOT NULL,
                         [gender] VARCHAR(20) NOT NULL,
                         [state] VARCHAR(50) DEFAULT 'Active' NOT NULL,
                         [created_at] DATETIME DEFAULT GETDATE() NOT NULL,
                         [updated_at] DATETIME DEFAULT GETDATE() NOT NULL
);

CREATE TABLE [invitation] (
                              [id] INT IDENTITY(1,1) PRIMARY KEY,
                              [event_id] INT NOT NULL,
                              [issuer_user_id] INT NOT NULL,
                              [guest_id] INT NOT NULL,
                              [code_qr] VARCHAR(255) NOT NULL,
                              [state] VARCHAR(50) DEFAULT 'Pending' NOT NULL,
                              [created_at] DATETIME DEFAULT GETDATE() NOT NULL,
                              [sent_at] DATETIME,
                              [read_at] DATETIME,
                              CONSTRAINT [FK_invitation_event] FOREIGN KEY ([event_id]) REFERENCES [event]([id]),
                              CONSTRAINT [FK_invitation_issuer] FOREIGN KEY ([issuer_user_id]) REFERENCES [user]([id]),
                              CONSTRAINT [FK_invitation_guest] FOREIGN KEY ([guest_id]) REFERENCES [guest]([id])
);

CREATE TABLE [scan] (
                        [id] INT IDENTITY(1,1) PRIMARY KEY,
                        [invitation_id] INT NOT NULL,
                        [scanned_by] INT NOT NULL,
                        [movement_type] VARCHAR(50) NOT NULL,
                        [result] VARCHAR(100) NOT NULL,
                        [scan_at] DATETIME DEFAULT GETDATE() NOT NULL,
                        CONSTRAINT [FK_scan_invitation] FOREIGN KEY ([invitation_id]) REFERENCES [invitation]([id]),
                        CONSTRAINT [FK_scan_scanned_by] FOREIGN KEY ([scanned_by]) REFERENCES [user]([id])
);