CREATE DATABASE IF NOT EXISTS Noctra_MVP;
USE Noctra_MVP;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS scan;
DROP TABLE IF EXISTS invitation;
DROP TABLE IF EXISTS guest;
DROP TABLE IF EXISTS event_member;
DROP TABLE IF EXISTS event;
DROP TABLE IF EXISTS user;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE user (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      dni VARCHAR(20) NOT NULL UNIQUE,
                      name VARCHAR(50) NOT NULL,
                      last_name VARCHAR(50) NOT NULL,
                      phone VARCHAR(20),
                      nickname VARCHAR(50) NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      address VARCHAR(255),
                      mail VARCHAR(150),
                      state VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
                      created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE event (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       user_id INT NOT NULL,
                       name VARCHAR(50) NOT NULL,
                       address VARCHAR(255) NOT NULL,
                       start_date DATETIME NOT NULL,
                       end_date DATETIME NOT NULL,
                       capacity INT NOT NULL,
                       state VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                       created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT FK_event_user
        FOREIGN KEY (user_id)
        REFERENCES user(id)
);

CREATE TABLE event_member (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              event_id INT NOT NULL,
                              user_id INT NOT NULL,
                              rol VARCHAR(50) NOT NULL,
                              assigned_by INT NOT NULL,

                              CONSTRAINT FK_event_member_event
                                  FOREIGN KEY (event_id)
                                      REFERENCES event(id)
                                      ON DELETE CASCADE,

                              CONSTRAINT FK_event_member_user
                                  FOREIGN KEY (user_id)
                                      REFERENCES user(id),

                              CONSTRAINT FK_event_member_assigned
                                  FOREIGN KEY (assigned_by)
                                      REFERENCES user(id)
);

CREATE TABLE guest (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       phone VARCHAR(20) NOT NULL,
                       gender VARCHAR(20) NOT NULL,
                       created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE invitation (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            event_id INT NOT NULL,
                            issuer_user_id INT NOT NULL,
                            guest_id INT NOT NULL,
                            token VARCHAR(255) NOT NULL UNIQUE,
                            code_qr VARCHAR(255) NOT NULL,
                            state VARCHAR(50) NOT NULL DEFAULT 'SIN_USAR',
                            created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            sent_at DATETIME,
                            read_at DATETIME,

                            CONSTRAINT FK_invitation_event
                                FOREIGN KEY (event_id)
                                    REFERENCES event(id)
                                    ON DELETE CASCADE,

                            CONSTRAINT FK_invitation_issuer
                                FOREIGN KEY (issuer_user_id)
                                    REFERENCES user(id),

                            CONSTRAINT FK_invitation_guest
                                FOREIGN KEY (guest_id)
                                    REFERENCES guest(id)
);

CREATE TABLE scan (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      invitation_id INT NOT NULL,
                      scanned_by INT NOT NULL,
                      movement_type VARCHAR(50) NOT NULL,
                      result VARCHAR(100) NOT NULL,
                      scan_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                      CONSTRAINT FK_scan_invitation
                          FOREIGN KEY (invitation_id)
                              REFERENCES invitation(id)
                              ON DELETE CASCADE,

                      CONSTRAINT FK_scan_scanned_by
                          FOREIGN KEY (scanned_by)
                              REFERENCES user(id)
);