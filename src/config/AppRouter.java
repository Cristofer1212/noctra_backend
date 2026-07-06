package config;

import com.sun.net.httpserver.HttpServer;
import modules.guest.repository.GuestRepository;
import modules.guest.repository.IGuestRepository;
import modules.guest.service.GuestService;
import modules.invitation.controller.InvitationController;
import modules.invitation.repository.IInvitationRepository;
import modules.invitation.repository.InvitationRepository;
import modules.invitation.service.InvitationService;
import modules.shared.http.HttpClientWrapper;
import modules.shared.integrations.cloudinary.CloudinaryService;
import modules.shared.integrations.cloudinary.ICloudinaryService;
import modules.shared.integrations.meta_webhook.IWebhookHandler;
import modules.shared.integrations.meta_webhook.WebhookController;
import modules.shared.integrations.meta_webhook.WebhookHandler;
import modules.shared.integrations.whatsapp.IWhatsappService;
import modules.shared.integrations.whatsapp.WhatsappService;
import modules.shared.utils.qr.IQrService;
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


        // Invitation Module
        IInvitationRepository invitationRepository = new InvitationRepository();
        IGuestRepository guestRepository = new GuestRepository();
        GuestService guestService = new GuestService(guestRepository);
        ICloudinaryService cloudinaryService = new CloudinaryService();
        HttpClientWrapper httpClient = new HttpClientWrapper();
        IWhatsappService whatsappService = new WhatsappService(httpClient);
        InvitationService invitationService = new InvitationService(guestService, invitationRepository, cloudinaryService,whatsappService );
        InvitationController invitationController = new InvitationController(invitationService);

        // ... al final de tu método configure
        IWebhookHandler webhookHandler = new WebhookHandler();
        WebhookController webhookController = new WebhookController(webhookHandler);

        server.createContext("/api/webhook", webhookController);



        // crear la ruta para postman
        server.createContext("/users", userController);
        server.createContext("/invitations", invitationController);



    }
}