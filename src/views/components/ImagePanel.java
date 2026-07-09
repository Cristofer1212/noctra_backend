package views.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

public class ImagePanel extends JComponent {

    private final int size;
    private final Color color;

    public ImagePanel(int size, Color color) {
        this.size = size;
        this.color = color;
        setPreferredSize(new Dimension(size, size));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color hueco = getBackgroundHole();

        // ---- Cabeza (anillo) ----
        int headDiameter = (int) (size * 0.45);
        int headX = (size - headDiameter) / 2;
        int headY = (int) (size * 0.04);
        int grosorCabeza = (int) (headDiameter * 0.24);

        g2.setColor(color);
        g2.fill(new Ellipse2D.Float(headX, headY, headDiameter, headDiameter));
        g2.setColor(hueco);
        g2.fill(new Ellipse2D.Float(
                headX + grosorCabeza, headY + grosorCabeza,
                headDiameter - grosorCabeza * 2, headDiameter - grosorCabeza * 2));

        // ---- Cuerpo (arco/hombros) ----
        int bodyWidth = (int) (size * 0.88);
        int bodyHeight = (int) (size * 0.6);
        int bodyX = (size - bodyWidth) / 2;
        int bodyY = (int) (size * 0.48);
        int grosorCuerpo = (int) (bodyWidth * 0.20);

        g2.setColor(color);
        g2.fill(new Arc2D.Float(bodyX, bodyY, bodyWidth, bodyHeight, 0, 180, Arc2D.PIE));
        g2.setColor(hueco);
        g2.fill(new Arc2D.Float(
                bodyX + grosorCuerpo, bodyY + (grosorCuerpo / 2f),
                bodyWidth - grosorCuerpo * 2, bodyHeight - grosorCuerpo,
                0, 180, Arc2D.PIE));

        g2.dispose();
    }

    // El "hueco" del ícono toma el color del fondo del panel padre,
    // para que se vea hueco de verdad y no solo una silueta sólida.
    private Color getBackgroundHole() {
        Container parent = getParent();
        return (parent != null) ? parent.getBackground() : Color.WHITE;
    }
}