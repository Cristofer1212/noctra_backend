package config;

import com.sun.net.httpserver.HttpServer;
import modules.user.controller.UserController;
import modules.user.mapper.UserMapper;
import modules.user.repository.UserRepository;
import modules.user.repository.IUserRepository;
import modules.user.service.UserService;
import modules.user.validator.UserValidator;

public class AppRouter {

    public static void configure(HttpServer server) {


        // User Module
        IUserRepository IUserRepository = new UserRepository();
        UserValidator validator = new UserValidator();
        UserMapper mapper = new UserMapper();
        UserService userService = new UserService(IUserRepository, validator, mapper);
        UserController userController = new UserController(userService);

        // crear la ruta para postman
        server.createContext("/users", userController);




    }
}