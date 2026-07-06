package modules.shared.integrations.cloudinary;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import modules.shared.config.ConfigLoader;

import java.util.Map;

// Guatauba PLanB RadioEdit

public class CloudinaryService implements ICloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService() {
        // Aquí configuras tus credenciales.
        // TIP: Lo ideal es que estos datos vengan de tu archivo config.properties

        String name = ConfigLoader.getProperty("cloudinary.cloud_name"); // Fíjate en el nombre de la clave
        String key = ConfigLoader.getProperty("cloudinary.api_key");
        String secret = ConfigLoader.getProperty("cloudinary.api_secret");

        System.out.println("--- DEBUG CLOUDINARY ---");
        System.out.println("Cloud Name desde config: '" + name + "'");
        System.out.println("API Key desde config: '" + key + "'");
        System.out.println("API Secret desde config: '" + secret + "'");

        // Vamos a asignar los valores directamente a variables para limpiar cualquier residuo
        String cloudName = ConfigLoader.getProperty("cloudinary.cloud_name");
        String apiKey = ConfigLoader.getProperty("cloudinary.api_key");
        String apiSecret = ConfigLoader.getProperty("cloudinary.api_secret");

        // Construimos el mapa explícitamente
        java.util.Map<String, String> config = new java.util.HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);

        this.cloudinary = new Cloudinary(config);


    }

    @Override
    public String uploadBase64(String base64Image, String publicId) {
        try {
            // El prefijo es necesario para que Cloudinary entienda que es un Base64 de imagen
            String dataUri = "data:image/png;base64," + base64Image;

            Map uploadResult = cloudinary.uploader().upload(dataUri, ObjectUtils.asMap(
                    "public_id", publicId,
                    "folder", "invitations" // Opcional: organiza tus archivos en carpetas
            ));

            return (String) uploadResult.get("secure_url");

        } catch (Exception e) {
            throw new RuntimeException("Error al subir imagen a Cloudinary", e);
        }
    }
}



