package modules.shared.integrations.whatsapp;



public interface IWhatsappService {



    void sendInvitation(String phoneNumber, String qrUrl, String nombre, String evento, String fechaInicio, String horaInicio, String fechaFin, String horaFin);


    void sendPorteroAsignado(String  nombreDeStaff, String numCelular, String nombreEvento, String nombreRemitente);

}
