USE noctra_mvp;

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