

-- Insertar Usuario
-- borrar el sp si existe
IF OBJECT_ID('sp_user_Insert', 'P') IS NOT NULL
    DROP PROCEDURE sp_user_Insert;
GO
-- Comprobar si existe el store procedure
SELECT * FROM sys.procedures

CREATE PROCEDURE sp_user_Insert
    @dni VARCHAR(20),
    @name VARCHAR(50),
    @lastName VARCHAR(50),
    @password VARCHAR(255)
    -- @phone eliminado
    -- @address VARCHAR(100),
    -- @mail VARCHAR(100),
    -- @state VARCHAR(20)
AS
BEGIN
    INSERT INTO [user] (dni, name, last_name, password)
    VALUES (@dni, @name, @lastName, @password);
END
GO

CREATE PROCEDURE sp_user_FindByDni
@dni NVARCHAR(20)
AS
BEGIN
    SELECT id, dni, name, last_name, phone, mail, password, state, address
    FROM [user]
    WHERE dni = @dni;
END




    -- ctrl + shif  r (acctualizar el cache de msssql)

use Noctra_MVP
SELECT * FROM [user]