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

public class Main {

  // Ya se pueden enviar mensajes al WhatsApp al crear invitación. Falta que se acepte la plantilla de meta
  private static final int PORT = 8080;

  public static void main(String[] args) {

    iniciarServidorYApp();
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

      System.out.println("Linea 40");

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

}
