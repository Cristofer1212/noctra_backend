import com.sun.net.httpserver.HttpServer;
import config.AppRouter;
import config.DatabaseInitializer;
import config.DbConnection;
import config.exception.DatabaseConnectionException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;

public class Main {

    // FULL IA - estudiar
    private static final int PORT = 8080;

    public static void main(String[] args) {


        // --- PRUEBA QUICK SORT ---
        int[] arreglo = {55, 18, 72, 6, 40, 29, 88, 13};
        System.out.println("--- Inicio prueba QuickSort ---");
        System.out.println("Array original: " + java.util.Arrays.toString(arreglo));
        // llamar al método
        quickSort(arreglo, 0, arreglo.length - 1);
        System.out.println("Array ordenado: " + java.util.Arrays.toString(arreglo));
        System.out.println("--- Fin prueba QuickSort ---\n");
        // -------------------------





        try {
            // 1. Inicializar base de datos
            DatabaseInitializer.initializeDatabase();

            // 2. Valida la conexión
            try (Connection connection = DbConnection.getConnection()) {
                if (connection != null && !connection.isClosed()) {
                    System.out.println("[ENTORNO] Noctra_MVP sincronizada, poblada y lista en el backend.");
                }
            }

            // 3. LEVANTAR EL SERVIDOR WEB (para usar postman)
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

            // 4. Registrar las rutas de tus módulos (UserController, etc.)
            AppRouter.configure(server);

            // 5. Encender el servidor en segundo plano
            server.setExecutor(null);
            server.start();

            System.out.println("\n🚀 [Noctra Backend] Servidor web encendido con éxito.");
            System.out.println("🌍 Escuchando peticiones HTTP en: http://localhost:" + PORT + "/users");

        } catch (DatabaseConnectionException e) {
            System.err.println("[ERROR CRÍTICO INFRAESTRUCTURA] " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("-> Detalle del motor: " + e.getCause().getMessage());
            }
        } catch (IOException e) {
            System.err.println("[ERROR CRÍTICO WEB] No se pudo arrancar el servidor en el puerto " + PORT + ": " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado en el sistema: " + e.getMessage());
        }







    }

    // --- MÉTODOS DEL ALGORITMO (Copiados de tu imagen) ---
    public static void  quickSort(int[] arreglo, int low, int high) {

        if (low < high) {
            int pivote = partition(arreglo, low, high);
            quickSort(arreglo, low, pivote - 1);
            quickSort(arreglo,  pivote + 1, high);
        }

    }



    // función de apoyo del caso recursivo

    public  static int partition(int[] arreglo ,int low ,int high) {
        // objetivo: menores, mayores | PIVOTE
        int pivote = arreglo[high];
        int i = low - 1; // con quien cabimaos de posicion si el valor es menor que el pivote
        for (int j = low; j < high; j++) {
            if ( arreglo[j] <= pivote ) {
                i++;
                int temporal = arreglo[i];
                arreglo[i] = arreglo[j];
                arreglo[j] = temporal;
            }
        }

        // partirlo
        int temporal = arreglo[ i + 1 ];
        arreglo[i + 1] = pivote;
        arreglo[high] = temporal;

        return i + 1;
    }


}