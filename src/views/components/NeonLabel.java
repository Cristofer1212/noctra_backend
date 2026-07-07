package views.components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Arrays;
import java.util.Random;

public class NeonLabel extends JComponent {

    private final String texto;
    private final Color colorNeon;
    private final Font fuente;

    private BufferedImage glowImage;
    private float brillo = 1.0f;
    private final Random random = new Random();

    public NeonLabel(String texto, Color colorNeon, int tamanoFuente) {
        this.texto = texto;
        this.colorNeon = colorNeon;
        this.fuente = new Font("SansSerif", Font.BOLD, tamanoFuente);
        setOpaque(false);
        iniciarParpadeo();
    }

    private void generarGlow() {
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) return;

        BufferedImage base = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gBase = base.createGraphics();
        gBase.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gBase.setFont(fuente);
        gBase.setColor(colorNeon);
        FontMetrics fm = gBase.getFontMetrics();
        int x = (w - fm.stringWidth(texto)) / 2;
        int y = (h - fm.getHeight()) / 2 + fm.getAscent();
        gBase.drawString(texto, x, y);
        gBase.dispose();

        glowImage = aplicarBlur(base, 9, 3);
    }

    private BufferedImage aplicarBlur(BufferedImage src, int radio, int pasadas) {
        int size = radio * 2 + 1;
        float valor = 1.0f / (size * size);
        float[] datos = new float[size * size];
        Arrays.fill(datos, valor);
        Kernel kernel = new Kernel(size, size, datos);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

        BufferedImage resultado = src;
        for (int i = 0; i < pasadas; i++) {
            BufferedImage extendido = new BufferedImage(
                    resultado.getWidth(), resultado.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = extendido.createGraphics();
            g.drawImage(resultado, 0, 0, null);
            g.dispose();
            resultado = op.filter(extendido, null);
        }
        return resultado;
    }

    private void iniciarParpadeo() {
        Timer timer = new Timer(60, e -> {
            double chance = random.nextDouble();
            if (chance < 0.04) {
                brillo = 0.15f + random.nextFloat() * 0.2f;
            } else if (chance < 0.10) {
                brillo = 0.6f + random.nextFloat() * 0.3f;
            } else {
                brillo = 0.92f + random.nextFloat() * 0.08f;
            }
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (glowImage == null) generarGlow();
        if (glowImage == null) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, brillo));
        g2.drawImage(glowImage, 0, 0, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1f, brillo * 1.3f)));
        g2.drawImage(glowImage, 0, 0, null);

        g2.setFont(fuente);
        g2.setColor(mezclarConBlanco(colorNeon, brillo));
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(texto)) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(texto, x, y);

        g2.dispose();
    }

    private Color mezclarConBlanco(Color base, float brillo) {
        float mezcla = 0.3f + brillo * 0.5f;
        int r = (int) (base.getRed() + (255 - base.getRed()) * mezcla);
        int g = (int) (base.getGreen() + (255 - base.getGreen()) * mezcla);
        int b = (int) (base.getBlue() + (255 - base.getBlue()) * mezcla);
        return new Color(Math.min(r, 255), Math.min(g, 255), Math.min(b, 255));
    }
}