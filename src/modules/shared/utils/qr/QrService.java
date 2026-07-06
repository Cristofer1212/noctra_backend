package modules.shared.utils.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class QrService implements IQrService {


    @Override
    public String generateQrCodeBase64(String text) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 250, 250);
            // Convertir en imagen
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            // Convertir a bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            // retorna en formato base 64
            return Base64.getEncoder().encodeToString(baos.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Erro al generar QR" + e);
        }
    }
}
