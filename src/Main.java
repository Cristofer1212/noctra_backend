import com.sun.net.httpserver.HttpServer;
import config.*;
import config.exception.DatabaseConnectionException;
import views.user.LoginView;
import modules.user.mapper.UserMapper;
import modules.user.repository.*;
import modules.user.service.UserService;
import modules.user.validator.UserValidator;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.util.Scanner;
import java.util.Random;

public class Main {

    // variables
    static long comparaciones = 0;
    static long recursiones = 0;
    private static final int PORT = 8080;

    public static void main(String[] args) {

        ejecutarQuickSort();

        iniciarServidorYApp();
    }

    private static void ejecutarQuickSort() {
        Scanner sc = new Scanner(System.in);
        System.out.println("--- QUICKSORT ---");
        System.out.println("Selecciona modo: 1 (Aleatorio) | 2 (Manual, ej: 10,5,2,8)");
        int opcion = Integer.parseInt(sc.nextLine());
        int[] arr;

        if (opcion == 2) {
            System.out.println("Escribe los números separados por coma:");
            String linea = sc.nextLine();
            String[] partes = linea.split(",");
            arr = new int[partes.length];
            for (int i = 0; i < partes.length; i++) {
                arr[i] = Integer.parseInt(partes[i].trim());
            }
        } else {
            System.out.println("Tamaño del arreglo:");
            int n = Integer.parseInt(sc.nextLine());
            arr = new int[n];
            Random rnd = new Random();
            for (int i = 0; i < n; i++) {
                arr[i] = rnd.nextInt(10);
            }
        }

        System.out.print("Desordenado: ");
        for (int num : arr) System.out.print(num + " ");

        // tiempo
        long inicio = System.nanoTime();
        quickSort(arr, 0, arr.length - 1);
        long fin = System.nanoTime();


        long duracionNano = fin - inicio;
        double duracionMili = duracionNano / 1_000_000.0;

        System.out.print("\nOrdenado: ");
        for (int num : arr) System.out.print(num + " ");

        System.out.println("\n\n--- ESTADÍSTICAS DEL ALGORITMO ---");
        System.out.println("Comparaciones: " + comparaciones);
        System.out.println("Recursiones:   " + recursiones);
        System.out.println("Tiempo total:  " + duracionNano + " ns (" + String.format("%.4f", duracionMili) + " ms)");
        System.out.println("----------------------------------\n");
    }

    private static void iniciarServidorYApp() {
        try {
            DatabaseInitializer.initializeDatabase();
            try (Connection connection = DbConnection.getConnection()) {
                if (connection != null && !connection.isClosed()) {
                    System.out.println("Entorno sincronizado");
                }
            }
            IUserRepository userRepository = new UserRepository();
            UserValidator validator = new UserValidator();
            UserMapper mapper = new UserMapper();
            UserService userService = new UserService(userRepository, validator, mapper);

            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            AppRouter.configure(server);
            server.setExecutor(null);
            server.start();

            System.out.println("Corriendo Puerto: " + PORT);

            SwingUtilities.invokeLater(() -> {
                LoginView loginView = new LoginView(userService);
                loginView.setVisible(true);
            });

        } catch (DatabaseConnectionException e) {
            System.err.println("[ERROR CRÍTICO] " + e.getMessage());
        } catch (IOException e) {
            System.err.println("[ERROR CRÍTICO WEB] " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //  QuickSort
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            recursiones++;
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    public static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            comparaciones++;
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            } // menores, mayores | pivote
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp; // menores <- (pivote) -> mayores
        return i + 1; // index pivote
    }
}