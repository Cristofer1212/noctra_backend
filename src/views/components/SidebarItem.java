package views.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SidebarItem extends JPanel {

    private static final Color COLOR_SELECCIONADO = new Color(0xD3, 0xE7, 0xF7);
    private static final Color TEXTO = new Color(0x2A, 0x2A, 0x2A);
    private static final Color BARRA_LATERAL = new Color(0x3E, 0x6B, 0x84);

    private final boolean seleccionado;

    public SidebarItem(String icono, String texto, boolean seleccionado) {
        this.seleccionado = seleccionado;

        setOpaque(false);
        setLayout(new BorderLayout(12, 0));
        setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(300, 50));
        setMaximumSize(new Dimension(300, 50));

        JLabel etiquetaIcono = new JLabel(icono);
        etiquetaIcono.setFont(new Font("SansSerif", Font.PLAIN, 20));
        add(etiquetaIcono, BorderLayout.WEST);

        JLabel etiquetaTexto = new JLabel(texto);
        etiquetaTexto.setFont(new Font("SansSerif", Font.PLAIN, 18));
        etiquetaTexto.setForeground(TEXTO);
        add(etiquetaTexto, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (seleccionado) {
            g2.setColor(COLOR_SELECCIONADO);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
            g2.setColor(BARRA_LATERAL);
            g2.fillRoundRect(0, 6, 5, getHeight() - 12, 4, 4);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}