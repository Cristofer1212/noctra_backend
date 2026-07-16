package modules.invitation.service;


import config.exception.DatabaseConnectionException;
import modules.event.repository.IEventRepository;
import modules.guest.model.Guest;
import modules.guest.repository.GuestRepository;
import modules.guest.service.GuestService;
import modules.invitation.dto.SendInvitationDto;
import modules.invitation.model.Invitation;
import modules.invitation.repository.IInvitationRepository;
import modules.invitation.utils.InvitationFactory;
import modules.invitation.utils.TokenGenerator;
import modules.shared.config.ConfigLoader;
import modules.shared.integrations.cloudinary.ICloudinaryService;
import modules.shared.integrations.whatsapp.IWhatsappService;
import modules.shared.integrations.whatsapp.WhatsappService;
import modules.shared.utils.qr.IQrService;
import modules.shared.utils.qr.QrService;
import modules.user.service.UserService;

public class InvitationService {

    private final GuestService guestService;
    private final IInvitationRepository invitationRepository;
    private final ICloudinaryService cloudinaryService;
    private final IWhatsappService whatsappService;
    private final UserService userService;
    private final IEventRepository eventRepository;
    public InvitationService(
            GuestService guestService,
            IInvitationRepository invitationRepository,
            ICloudinaryService cloudinaryService,
            IWhatsappService whatsappService,
            UserService userService,
            IEventRepository eventRepository


    ) {
        this.guestService = guestService;
        this.invitationRepository = invitationRepository;
        this.cloudinaryService = cloudinaryService;
        this.whatsappService = whatsappService;
        this.userService = userService;
        this.eventRepository = eventRepository;
    }

    // Crear Invitación
    public void createInvitation(SendInvitationDto sendInvitationDto, Integer issuerUserId) throws DatabaseConnectionException {
        System.out.println("DEBUG (Service): ID recibido en createInvitation es: " + issuerUserId);
        try {
            // 1. Validar que quien invite existe


            // 2. Verificar que el evento exista en la base de datos

            // 3. Validar que el mismo número de celular no tenga ticket para este evento

            // 4. Generar token único
            String token = TokenGenerator.generate();

            // 5. Generar URL (Ticket)
            String baseUrl = ConfigLoader.getProperty("app.base.url"); // Exponer localhost
            String codigo_qr = baseUrl + "/validate?token=" + token; // URL

            // 6. Crear invitación (Objeto Invitation - Ticket)
            Guest guest = guestService.createGuest(sendInvitationDto.getPhoneGuest(), sendInvitationDto.getGender());

            Invitation invitation = InvitationFactory.create(
                    guest.getId(),
                    token,
                    codigo_qr,
                    sendInvitationDto.getEventId(),
                    issuerUserId
            );




            // 7. Generar QR en base64 (PNG)
            IQrService qrService = new QrService();
            String qrBase64 = qrService.generateQrCodeBase64(codigo_qr);

            // 8. Subir QR a cloudinary con public_id basado en el token
            String qrUrl = cloudinaryService.uploadBase64(qrBase64, token);

            // Guardar ticket en BD con el URL del QR
            invitation.setCodeQr(qrUrl);
            // Justo antes de guardar, imprime los valores:
            System.out.println("--- DEPURACIÓN CRÍTICA ANTES DE GUARDAR ---");
            System.out.println("Event ID: " + invitation.getEventId());
            System.out.println("Guest ID: " + invitation.getGuestId());
            System.out.println("ISSUER USER ID (EL QUE FALLA): [" + invitation.getIssuerUserId() + "]");

// Verificación de seguridad
            if (invitation.getIssuerUserId() == null || invitation.getIssuerUserId() <= 0) {
                System.err.println("¡ERROR DETECTADO! El ID del usuario es inválido o nulo.");
            }
            invitationRepository.save(invitation);
            // Enviar Ticket al wsp del invitado
            sendWhatsAppInvitation(sendInvitationDto.getPhoneGuest(), invitation, issuerUserId);        } catch (Exception e) {
            // Imprime el error real en la consola de tu IDE
            System.err.println("ERROR CRÍTICO EN SERVICE:");
            e.printStackTrace();
            // Relanza la excepción para que el controlador la atrape
            throw new RuntimeException("Error en el servicio: " + e.getMessage());
        }


    }




    public void sendWhatsAppInvitation(String phoneNumber, Invitation invitation, Integer issuerUserId) throws DatabaseConnectionException {
        String nombre = "Usuario";

        // 1. Buscamos el emisor (esto ya lo tenías)
        try {
            var userOptional = userService.findById(issuerUserId);
            if (userOptional.isPresent()) {
                nombre = userOptional.get().getName();
            }
        } catch (Exception e) {
            System.err.println("Error al buscar el nombre del emisor: " + e.getMessage());
        }

        // 2. BUSCAMOS EL EVENTO REAL
        String eventoNombre = "Evento";
        String fechaInicio = "Próximamente";
        String horaInicio = "";
        String fechaFin = "";
        String horaFin = "";

        try {
            // Asumiendo que tu repositorio tiene un método findById
            var eventoOpt = eventRepository.findById(invitation.getEventId());
            if (eventoOpt.isPresent()) {
                var eventoReal = eventoOpt.get();
                eventoNombre = eventoReal.getName();
                
                // Formateadores amigables para el mensaje de WhatsApp
                java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("hh:mm a", java.util.Locale.US);
                
                if (eventoReal.getStartDate() != null) {
                    fechaInicio = eventoReal.getStartDate().format(dateFormatter);
                    horaInicio = eventoReal.getStartDate().format(timeFormatter);
                }
                if (eventoReal.getEndDate() != null) {
                    fechaFin = eventoReal.getEndDate().format(dateFormatter);
                    horaFin = eventoReal.getEndDate().format(timeFormatter);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener datos del evento: " + e.getMessage());
        }

        // 3. ENVIAMOS CON LOS DATOS DINÁMICOS
        whatsappService.sendInvitation(phoneNumber, invitation.getCodeQr(), nombre, eventoNombre, fechaInicio, horaInicio, fechaFin, horaFin);
    }
}