package modules.shared.integrations.whatsapp;



public interface IWhatsappService {



    void sendInvitation(String phoneNumber, String qrUrl, String nombre, String evento, String fechaInicio, String fechaFin);


    void sendPorteroAsignado(String  nombreDeStaff, String numCelular, String nombreEvento, String nombreRemitente);

}
