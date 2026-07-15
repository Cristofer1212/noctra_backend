package modules.portero.service;


import modules.portero.dto.PorteroCreateDto;
import modules.shared.http.HttpClientWrapper;
import modules.shared.integrations.decolecta.DecolectaService;
import modules.shared.integrations.whatsapp.IWhatsappService;

public class PorteroService {
    private final IWhatsappService whatsappService;
    private final DecolectaService decolectaService;

    public PorteroService(IWhatsappService whatsappService) {
        this.whatsappService = whatsappService;
        this.decolectaService = new DecolectaService(new HttpClientWrapper());
    }

    public void registrarPortero(PorteroCreateDto porteroCreateDto, String nombreEvento, String nombreRemitente) {

        String nombreDeStaff = decolectaService.obtenerNombrePorDni(porteroCreateDto.getDni());




        whatsappService.sendPorteroAsignado(
                nombreDeStaff,
                porteroCreateDto.getNumCelular(),
                nombreEvento,
                nombreRemitente);
    }


}
