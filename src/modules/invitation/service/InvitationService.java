package modules.invitation.service;


import config.exception.DatabaseConnectionException;
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

public class InvitationService {

    private final GuestService guestService;
    private final IInvitationRepository invitationRepository;
    private final ICloudinaryService cloudinaryService;
    private final IWhatsappService whatsappService;

    public InvitationService(
            GuestService guestService,
            IInvitationRepository invitationRepository,
            ICloudinaryService cloudinaryService,
            IWhatsappService whatsappService

    ) {
        this.guestService = guestService;
        this.invitationRepository = invitationRepository;
        this.cloudinaryService = cloudinaryService;
        this.whatsappService = whatsappService;
    }

    // Crear Invitación
    public void createInvitation(SendInvitationDto sendInvitationDto) throws DatabaseConnectionException {

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
                    codigo_qr
            );


            // 7. Generar QR en base64 (PNG)
            IQrService qrService = new QrService();
            String qrBase64 = qrService.generateQrCodeBase64(codigo_qr);

            // 8. Subir QR a cloudinary con public_id basado en el token
            String qrUrl = cloudinaryService.uploadBase64(qrBase64, token);

            // Guardar ticket en BD con el URL del QR
            invitation.setCodeQr(qrUrl);
            invitationRepository.save(invitation);
            // Enviar Ticket al wsp del invitado
            sendWhatsAppInvitation(sendInvitationDto.getPhoneGuest(), invitation.getCodeQr());
        } catch (Exception e) {
            // Imprime el error real en la consola de tu IDE
            System.err.println("ERROR CRÍTICO EN SERVICE:");
            e.printStackTrace();
            // Relanza la excepción para que el controlador la atrape
            throw new RuntimeException("Error en el servicio: " + e.getMessage());
        }


    }



    public void sendWhatsAppInvitation(String phoneNumber, String qrUrl) {
        whatsappService.sendInvitation(phoneNumber, qrUrl);
    }


}