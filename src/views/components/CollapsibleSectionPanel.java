package views.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class CollapsibleSectionPanel extends JPanel {

    private static final Color COLOR_BARRA = new Color(0xD8, 0xE9, 0xF7);
    private static final Color COLOR_CONTENIDO = Color.WHITE;
    private static final Color TEXTO_TITULO = new Color(0x1A, 0x1A, 0x1A);

    private boolean expandido = true;
    private final JPanel panelFilas;
    private final JScrollPane scrollFilas;
    private final JLabel botonToggle;

    public CollapsibleSectionPanel(String titulo, List<JComponent> filas) {
        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel barra = crearBarraTitulo(titulo);
        add(barra, BorderLayout.NORTH);

        botonToggle = (JLabel) barra.getClientProperty("botonToggle");

        panelFilas = new JPanel();
        panelFilas.setLayout(new BoxLayout(panelFilas, BoxLayout.Y_AXIS));
        panelFilas.setBackground(COLOR_CONTENIDO);
        panelFilas.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

        if (filas.isEmpty()) {
            JLabel vacio = new JLabel("  No hay eventos por aquí todavía.");
            vacio.setFont(new Font("SansSerif", Font.PLAIN, 15));
            vacio.setForeground(new Color(0x8A, 0x8A, 0x8A));
            vacio.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 0));
            panelFilas.add(vacio);
        } else {
            for (JComponent fila : filas) {
                fila.setAlignmentX(Component.LEFT_ALIGNMENT);
                panelFilas.add(fila);
            }
        }

        // Creamos el scroll dedicado ÚNICAMENTE para las filas de esta sección
        scrollFilas = new JScrollPane(panelFilas);
        scrollFilas.setBorder(null);
        scrollFilas.getViewport().setBackground(COLOR_CONTENIDO);
        scrollFilas.getVerticalScrollBar().setUnitIncrement(16); // Scroll fluido
        scrollFilas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollFilas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // SI EXCEEDE DE 4 FILAS: Le fijamos una altura máxima para activar el scrollbar interno
        if (filas.size() > 4) {
            scrollFilas.setPreferredSize(new Dimension(scrollFilas.getPreferredSize().width, 270));
        }

        add(scrollFilas, BorderLayout.CENTER);
    }

    private JPanel crearBarraTitulo(String titulo) {
        JPanel barra = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_BARRA);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
                g2.dispose();
            }
        };
        barra.setOpaque(false);
        barra.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        barra.setPreferredSize(new Dimension(0, 46));
        barra.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        JLabel etiquetaTitulo = new JLabel(titulo);
        etiquetaTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        etiquetaTitulo.setForeground(TEXTO_TITULO);
        barra.add(etiquetaTitulo, BorderLayout.WEST);

        JLabel toggle = new JLabel("\u2212"); // signo "menos"
        toggle.setFont(new Font("SansSerif", Font.BOLD, 20));
        toggle.setForeground(TEXTO_TITULO);
        barra.add(toggle, BorderLayout.EAST);
        barra.putClientProperty("botonToggle", toggle);

        barra.setCursor(new Cursor(Cursor.HAND_CURSOR));
        barra.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                alternarExpansion();
            }
        });

        return barra;
    }

    private void alternarExpansion() {
        expandido = !expandido;
        scrollFilas.setVisible(expandido); // Ocultamos el scroll completo al colapsar
        panelFilas.setVisible(expandido);
        botonToggle.setText(expandido ? "\u2212" : "+");
        revalidate();
        repaint();
    }
}