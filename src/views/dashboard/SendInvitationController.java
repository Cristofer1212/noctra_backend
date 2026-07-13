package views.dashboard;

import modules.event.repository.EventRepository;
import modules.invitation.dto.SendInvitationDto;
import modules.invitation.service.InvitationService;
import modules.guest.service.GuestService;
import modules.guest.repository.GuestRepository;
import modules.invitation.repository.InvitationRepository;
import modules.shared.http.HttpClientWrapper;
import modules.shared.integrations.cloudinary.CloudinaryService;
import modules.shared.integrations.whatsapp.WhatsappService;
import modules.user.mapper.UserMapper;
import modules.user.repository.UserRepository;
import modules.user.service.UserService;
import modules.user.validator.UserValidator;

import javax.swing.*;

public class SendInvitationController {
    private final SendInvitationView vista;
    private final InvitationService invitationService;
    private final Integer eventId;

    // Instancias requeridas por el UserService
    HttpClientWrapper httpClient = new HttpClientWrapper();
    UserRepository userRepo = new UserRepository();
    UserValidator userValidator = new UserValidator();
    UserMapper userMapper = new UserMapper();
    UserService userService = new UserService(userRepo, userValidator, userMapper);

    public SendInvitationController(SendInvitationView vista, Integer eventId) {
        this.vista = vista;
        this.eventId = eventId;

        this.invitationService = new InvitationService(
                new GuestService(new GuestRepository()),
                new InvitationRepository(),
                new CloudinaryService(),
                new WhatsappService(httpClient),
                userService,
                new EventRepository()
        );

        initListeners();
    }

    private void initListeners() {
        this.vista.btnEnviar.addActionListener(e -> enviarInvitacion());
    }

    private void enviarInvitacion() {
        String genero = (String) vista.cbGenero.getSelectedItem();
        String celular = vista.txtCelular.getText().trim();

        if (celular.isEmpty() || "Seleccionar...".equals(genero)) {
            JOptionPane.showMessageDialog(vista, "Por favor, seleccione un género y escriba un celular.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        vista.btnEnviar.setText("Enviando...");
        vista.btnEnviar.setEnabled(false);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                SendInvitationDto dto = new SendInvitationDto(genero, celular);
                dto.setEventId(eventId);

                // Obtenemos el ID del emisor logueado (Devuelve 1)
                Integer idEmisor = modules.user.controller.UserController.idUsuarioLogueado;
                System.out.println("DEBUG (Controller): ID recuperado de UserController es: " + idEmisor);

                // Enviamos los datos al servicio
                invitationService.createInvitation(dto, idEmisor);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(vista, "¡Invitación enviada con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    vista.dispose();
                } catch (Exception ex) {
                    // Captura el mensaje real omitiendo el envoltorio del SwingWorker
                    Throwable causa = (ex.getCause() != null) ? ex.getCause() : ex;
                    JOptionPane.showMessageDialog(vista, "Error al enviar invitación:\n" + causa.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    vista.btnEnviar.setText("Enviar Invitación");
                    vista.btnEnviar.setEnabled(true);
                }
            }
        };

        worker.execute();
    }

    public void mostrar() {
        vista.setVisible(true);
    }
}