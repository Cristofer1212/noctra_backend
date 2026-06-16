package config;

import com.sun.net.httpserver.HttpServer;
import modules.user.controller.UserController;
import modules.user.repository.SqlServerUserRepository;
import modules.user.repository.UserRepository;
import modules.user.service.UserService;

public class AppRouter {

    public static void configure(HttpServer server) {


        // User Module
        UserRepository userRepository = new SqlServerUserRepository();
        UserService userService = new UserService(userRepository);
        UserController userController = new UserController(userService);

        // crear la ruta para postman
        server.createContext("/users", userController);




    }
}