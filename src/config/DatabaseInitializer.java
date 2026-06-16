package config;

import config.exception.DatabaseConnectionException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseInitializer {

    // Full IA

    public static void initializeDatabase() throws DatabaseConnectionException {
        // validar si ya existen otros datos
        if (checkIfDatabaseHasData()) {
            System.out.println("\n[MIGRACIÓN] La base de datos ya está configurada. Saltando inicialización para cuidar tus datos.");
            return;
        }

        System.out.println("\n=== [MIGRACIÓN] Base de datos vacía. Iniciando configuración automática ===");

        // 2. contruir tablar
        executeSqlFile("database/01_schema.sql");

        // 3. insertar semilla
        executeSqlFile("database/02_seeding.sql");

        System.out.println("=== [MIGRACIÓN] Estructura y semillas listas ===\n");
    }

    private static boolean checkIfDatabaseHasData() {
        String sql = "SELECT COUNT(*) FROM [user]";
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            // Si la tabla no existe todavía, da error y devuelve false para que se ejecuten las migracines
            return false;
        }
        return false;
    }

    private static void executeSqlFile(String pathString) throws DatabaseConnectionException {
        Path path = Paths.get(pathString);

        if (!Files.exists(path)) {
            System.err.println("[-] Alerta: No se encontró el archivo de migración: " + pathString);
            return;
        }

        try {
            String sqlScript = Files.readString(path);

            // Separador por GO adaptado para SQL Server nativo
            String[] commands = sqlScript.split("(?i)\\bGO\\b");

            try (Connection conn = DbConnection.getConnection();
                 Statement stmt = conn.createStatement()) {

                for (String command : commands) {
                    String cleanedCommand = command.trim();
                    if (!cleanedCommand.isEmpty()) {
                        stmt.addBatch(cleanedCommand);
                    }
                }

                stmt.executeBatch();
                System.out.println("[ OK ] Ejecutado con éxito: " + pathString);

            } catch (Exception e) {
                throw new DatabaseConnectionException("Error al ejecutar el lote SQL de: " + pathString, e);
            }

        } catch (IOException e) {
            throw new DatabaseConnectionException("Error de lectura en el disco para el archivo: " + pathString, e);
        }
    }
}