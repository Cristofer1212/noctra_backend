
-- Restricciones de unicidad (UNIQUE)
ALTER TABLE user ADD CONSTRAINT UQ_user_dni UNIQUE (dni);
ALTER TABLE invitation ADD CONSTRAINT UQ_invitation_token UNIQUE (token);



