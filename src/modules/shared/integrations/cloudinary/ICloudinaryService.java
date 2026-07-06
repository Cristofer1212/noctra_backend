package modules.shared.integrations.cloudinary;

public interface ICloudinaryService {
    String uploadBase64(String base64Image, String publicId);
}
