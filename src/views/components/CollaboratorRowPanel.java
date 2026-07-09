package views.components;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class CollaboratorRowPanel extends JPanel {

    private static final Color FONDO_AVATAR = new Color(0xD3, 0xE7, 0xF7);
    private static final Color FONDO_STATS  = new Color(0xD3, 0xE7, 0xF7);
    private static final Color TEXTO_OSCURO = new Color(0x2A, 0x2A, 0x2A);
    private static final Color TEXTO_AZUL   = new Color(0x1C, 0x6F, 0xA0);
    private static final Color LINEA        = new Color(0xC9, 0xDC, 0xEE);

    public CollaboratorRowPanel(String nombre, int cantidadColaboraciones, int porcentajeExito, boolean mostrarLinea) {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(0, 90));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        if (mostrarLinea) {
            setBorder(new MatteBorder(0, 0, 1, 0, LINEA));
        }

        add(crearAvatar(), BorderLayout.WEST);

        JLabel etiquetaNombre = new JLabel("  " + nombre);
        etiquetaNombre.setFont(new Font("SansSerif", Font.PLAIN, 20));
        etiquetaNombre.setForeground(TEXTO_OSCURO);
        add(etiquetaNombre, BorderLayout.CENTER);

        add(crearStats(cantidadColaboraciones, porcentajeExito), BorderLayout.EAST);
    }

    private JComponent crearAvatar() {
        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setOpaque(true);
        contenedor.setBackground(FONDO_AVATAR);
        contenedor.setPreferredSize(new Dimension(110, 90));
        // El ícono toma el color del fondo del contenedor para el "hueco" del outline
        contenedor.add(new ImagePanel(46, Color.WHITE));
        return contenedor;
    }

    private JComponent crearStats(int cantidadColaboraciones, int porcentajeExito) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.setBackground(FONDO_STATS);
        panel.setPreferredSize(new Dimension(230, 90));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 20));

        JLabel colaboraciones = new JLabel(cantidadColaboraciones + " eventos colaborados", SwingConstants.CENTER);
        colaboraciones.setFont(new Font("SansSerif", Font.PLAIN, 14));
        colaboraciones.setForeground(TEXTO_AZUL);
        colaboraciones.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel porcentaje = new JLabel(porcentajeExito + "%", SwingConstants.CENTER);
        porcentaje.setFont(new Font("SansSerif", Font.BOLD, 22));
        porcentaje.setForeground(TEXTO_OSCURO);
        porcentaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(colaboraciones);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(porcentaje);
        panel.add(Box.createVerticalGlue());

        return panel;
    }
}