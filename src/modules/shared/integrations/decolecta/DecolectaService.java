package modules.shared.integrations.decolecta;


import modules.shared.config.ConfigLoader;
import modules.shared.http.HttpClientWrapper;
import modules.shared.json.JsonUtils;

public class DecolectaService implements IDecolectaService {

    private final HttpClientWrapper httpClient;
    private final String token;

    public DecolectaService(HttpClientWrapper httpClient) {
        this.httpClient = httpClient;
        this.token = ConfigLoader.getProperty("decolecta.api.token");
    }


    @Override
    public String obtenerNombrePorDni(String dni) {

        String urlAccesoApi = "https://api.decolecta.com/v1/reniec/dni?numero=" + dni ;
        String jsonRespuesta = httpClient.get(urlAccesoApi, this.token);

        // Pareas JSON
        DecolectaReniecReponseDto response = JsonUtils.fromJson(jsonRespuesta, DecolectaReniecReponseDto.class);

        if (response != null && response.getFirstName() != null) {
            return response.getNombreCompleto();
        }

        return "DNI no existe";



    }
}
