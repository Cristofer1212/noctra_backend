USE Noctra_MVP;
-- 1. DESTRUCCIÓN
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS scan;
DROP TABLE IF EXISTS invitation;
DROP TABLE IF EXISTS guest;
DROP TABLE IF EXISTS event_member;
DROP TABLE IF EXISTS event;
DROP TABLE IF EXISTS user;
SET FOREIGN_KEY_CHECKS = 1;

-- 2. CREACIÓN
CREATE TABLE user (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      dni VARCHAR(20) NOT NULL UNIQUE,
                      name VARCHAR(50) NOT NULL,
                      last_name VARCHAR(50) NOT NULL,
    -- phone VARCHAR(11) NULL,
                      address VARCHAR(255) NULL,
    -- mail VARCHAR(150) NULL,
                      password VARCHAR(255) NOT NULL,
                      state VARCHAR(50) DEFAULT 'ACTIVE' NOT NULL,
                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
                      updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE event (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       user_id INT NOT NULL,
                       name VARCHAR(50) NOT NULL,
                       address VARCHAR(255) NOT NULL,
                       start_date DATETIME NOT NULL,
                       end_date DATETIME NOT NULL,
                       capacity INT NOT NULL,
                       state VARCHAR(50) DEFAULT 'PENDING' NOT NULL,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                       CONSTRAINT FK_event_user FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE event_member (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              event_id INT NOT NULL,
                              user_id INT NOT NULL,
                              rol VARCHAR(50) NOT NULL,
    -- state VARCHAR(50) DEFAULT 'inactive' NOT NULL,
                              assigned_by INT NOT NULL,

                              CONSTRAINT FK_event_member_event FOREIGN KEY (event_id) REFERENCES event(id),
                              CONSTRAINT FK_event_member_user FOREIGN KEY (user_id) REFERENCES user(id),
                              CONSTRAINT FK_event_member_assigned FOREIGN KEY (assigned_by) REFERENCES user(id)
);

CREATE TABLE guest (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       phone VARCHAR(20) NOT NULL,
                       gender VARCHAR(20) NOT NULL,
    -- state VARCHAR(50) DEFAULT 'Active' NOT NULL
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL
    -- updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
);

CREATE TABLE invitation (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            event_id INT NOT NULL,
                            issuer_user_id INT NOT NULL, -- usuario emisor
                            guest_id INT NOT NULL,
                            token VARCHAR(255) NOT NULL UNIQUE,
                            code_qr VARCHAR(255) NOT NULL,
                            state VARCHAR(50) DEFAULT 'SIN_USAR' NOT NULL,
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
                            sent_at DATETIME NULL,
                            read_at DATETIME NULL,
                            CONSTRAINT FK_invitation_event FOREIGN KEY (event_id) REFERENCES event(id),
                            CONSTRAINT FK_invitation_issuer FOREIGN KEY (issuer_user_id) REFERENCES user(id),
                            CONSTRAINT FK_invitation_guest FOREIGN KEY (guest_id) REFERENCES guest(id)
);

CREATE TABLE scan (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      invitation_id INT NOT NULL,
                      scanned_by INT NOT NULL,
                      movement_type VARCHAR(50) NOT NULL,
                      result VARCHAR(100) NOT NULL,
                      scan_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
                      CONSTRAINT FK_scan_invitation FOREIGN KEY (invitation_id) REFERENCES invitation(id),
                      CONSTRAINT FK_scan_scanned_by FOREIGN KEY (scanned_by) REFERENCES user(id)
);

-- Store Procedure
USE Noctra_MVP;

-- 1. Procedimiento para insertar usuario
DROP PROCEDURE IF EXISTS sp_user_Insert;

DELIMITER //
CREATE PROCEDURE sp_user_Insert(
    IN p_dni VARCHAR(20),
    IN p_name VARCHAR(50),
    IN p_lastName VARCHAR(50),
    IN p_password VARCHAR(255)
)
BEGIN
    INSERT INTO user (dni, name, last_name, password)
    VALUES (p_dni, p_name, p_lastName, p_password);
END //
DELIMITER ;

-- 2. Procedimiento para buscar por DNI
DROP PROCEDURE IF EXISTS sp_user_FindByDni;

DELIMITER //
CREATE PROCEDURE sp_user_FindByDni(
    IN p_dni VARCHAR(20)
)
BEGIN
    SELECT id, dni, name, last_name, phone, mail, password, state, address
    FROM user
    WHERE dni = p_dni;
END //
DELIMITER ;


