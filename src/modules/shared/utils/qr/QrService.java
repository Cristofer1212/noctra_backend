package modules.shared.utils.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.Color; // NUEVO
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class QrService implements IQrService {

    @Override
    public String generateQrCodeBase64(String text) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 250, 250);

            // 1. Convertir en imagen original
            BufferedImage originalImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // 2. CAMBIO CRÍTICO: Crear una nueva imagen tipo RGB (sin transparencia)
            BufferedImage rgbImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

            // 3. Dibujar la original sobre el fondo blanco sólido
            rgbImage.createGraphics().drawImage(originalImage, 0, 0, Color.WHITE, null);

            // 4. Convertir a bytes usando la imagen RGB
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(rgbImage, "jpg", baos); // Cambiado a "jpg" para mayor compatibilidad con Meta

            // retorna en formato base 64
            return Base64.getEncoder().encodeToString(baos.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Error al generar QR: " + e.getMessage(), e);
        }
    }
}