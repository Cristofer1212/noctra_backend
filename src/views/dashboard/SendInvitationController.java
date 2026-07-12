package views.dashboard;

import modules.invitation.dto.SendInvitationDto;
import modules.invitation.service.InvitationService;

// Importaciones de tus dependencias (Ajusta los paquetes según tu proyecto)
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
    private SendInvitationView vista;
    private InvitationService invitationService;
    HttpClientWrapper httpClient = new HttpClientWrapper();
    // 1. Necesitas instanciar lo que el UserService requiere primero
    UserRepository userRepo = new UserRepository();
    UserValidator userValidator = new UserValidator();
    UserMapper userMapper = new UserMapper();
    UserService userService = new UserService(userRepo, userValidator, userMapper);


    // MODIFICACIÓN: Ya no pedimos el servicio por parámetro, solo la vista
    public SendInvitationController(SendInvitationView vista) {
        this.vista = vista;

        // Instanciamos el servicio aquí adentro.
        // IMPORTANTE: Reemplaza las implementaciones (ej. InvitationRepository)
        // con los nombres reales de las clases que tienes en tu proyecto.
        this.invitationService = new InvitationService(
                new GuestService(new GuestRepository()), // Ajusta si GuestService recibe argumentos
                new InvitationRepository(),              // Implementación de IInvitationRepository
                new CloudinaryService(),                 // Implementación de ICloudinaryService
                new WhatsappService(httpClient),                    // Implementación de IWhatsappService
                userService
        );

        initListeners();
    }

    private void initListeners() {
        this.vista.btnEnviar.addActionListener(e -> enviarInvitacion());
    }

    private void enviarInvitacion() {
        String genero = (String) vista.cbGenero.getSelectedItem();
        String celular = vista.txtCelular.getText().trim(); // .trim() quita espacios accidentales

        // 1. Validar que los campos no estén vacíos
        if (celular.isEmpty() || genero.equals("Seleccionar...")) {
            JOptionPane.showMessageDialog(vista, "Por favor, seleccione un género y escriba un celular.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Feedback visual (deshabilitar botón mientras carga)
        vista.btnEnviar.setText("Enviando...");
        vista.btnEnviar.setEnabled(false);

        // 3. Ejecutar la llamada al backend en un hilo secundario para no congelar la pantalla
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                // 1. Instanciar el DTO
                SendInvitationDto dto = new SendInvitationDto(genero, celular);

                // 2. OBTENER EL ID DEL EMISOR QUE GUARDAMOS EN EL LOGIN
                // Debes importar modules.user.controller.UserController
                Integer idEmisor = modules.user.controller.UserController.idUsuarioLogueado;
                System.out.println("DEBUG (Controller): ID recuperado de UserController es: " + idEmisor);
                // 3. Llamar a la lógica pasando ambos argumentos
                invitationService.createInvitation(dto, idEmisor);

                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // Si ocurrió un error en doInBackground(), get() lanzará la excepción

                    // Si todo sale bien, mostramos mensaje y cerramos el modal
                    JOptionPane.showMessageDialog(vista, "¡Invitación enviada con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    vista.dispose();

                } catch (Exception ex) {
                    // Si algo falla, mostramos el error y reactivamos el botón
                    JOptionPane.showMessageDialog(vista, "Error al enviar invitación:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    vista.btnEnviar.setText("Enviar Invitación");
                    vista.btnEnviar.setEnabled(true);
                }
            }
        };

        worker.execute(); // Iniciar el hilo
    }

    public void mostrar() {
        vista.setVisible(true);
    }
}