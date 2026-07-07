package views.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CustomTextField extends JTextField {

    public CustomTextField() {
        estilizar(this);
    }

    // Estático para poder usarlo también con JPasswordField desde afuera
    public static void estilizar(JTextField field) {
        field.setOpaque(false);
        field.setBorder(new EmptyBorder(0, 15, 0, 15));
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setUI(new javax.swing.plaf.basic.BasicTextFieldUI() {
            @Override
            protected void paintSafely(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, field.getWidth(), field.getHeight(), 20, 20));
                super.paintSafely(g);
            }
        });
    }
}