package modules.invitation.service;



public class InvitationService {





    // Crear Invitación
    public void createInvitation() {

        // 1. Verificar que el usuario existe en la base de datos




        // 2. Verificar que el evento exista en la base de datos

        // 3. Validar que el mismo DNI no tenga ticket para este evento

        // 4. Generar tocken único y url publica


        // 5. Generar URL (Ticket)

        // 6. Crear el ticket en memoria (sin guardar aún)


        // 7. Generar QR en base64 (PNG)

        // 8. Subir QR a cloudinary con public_id basado en el token

        // 0. Guardar ticket en BD con el URL del QR




    }



    public void sendWhatsAppInvitation() {

        // Enviar ticket a usuario por WhatsApp


    }


}