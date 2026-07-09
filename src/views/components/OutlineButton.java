package views.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class OutlineButton extends JButton {

    private static final Color COLOR_BORDE = new Color(0x3E, 0x6B, 0x84);
    private static final Color COLOR_TEXTO = new Color(0x2A, 0x2A, 0x2A);

    public OutlineButton(String texto) {
        super("+   " + texto);
        setFont(new Font("SansSerif", Font.PLAIN, 16));
        setForeground(COLOR_TEXTO);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));

        g2.setStroke(new BasicStroke(1.5f));
        g2.setColor(COLOR_BORDE);
        g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 15, 15));

        g2.dispose();
        super.paintComponent(g);
    }
}