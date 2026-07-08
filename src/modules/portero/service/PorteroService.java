package modules.portero.service;


import modules.shared.integrations.whatsapp.IWhatsappService;
import modules.shared.integrations.whatsapp.WhatsappService;

public class PorteroService {

    private final IWhatsappService whatsappService;





    public PorteroService(
            IWhatsappService whatsappService
    ) {
        this.whatsappService = whatsappService;
    }




    public void createPortero() {
        String nombreStaff = "Kevin Salinas";
        String numCelular = "1234";
        String nombreEvento = "whatsapp";
        String nombreRemitente = "whatsapp";


        whatsappService.sendPorteroAsignado(nombreStaff, numCelular, nombreEvento, nombreRemitente);

    }


}
