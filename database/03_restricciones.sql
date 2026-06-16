

-- Agregar restricciones a la tabla 'event'
ALTER TABLE [event]
    ADD CONSTRAINT CHK_Event_Capacity CHECK (capacity > 0);

ALTER TABLE [event]
    ADD CONSTRAINT CHK_Event_Dates CHECK (end_date >= start_date);

-- Agregar restricción a la tabla 'user' para el estado
ALTER TABLE [user]
    ADD CONSTRAINT CHK_User_State CHECK (state IN ('Active', 'Inactive', 'Suspended'));