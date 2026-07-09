package views.components;

import javax.swing.*;
import java.awt.*;

/**
 * Fila de indicador estadístico: una etiqueta a la izquierda + dos casillas
 * vacías a la derecha. Las casillas empiezan vacías (visual, como en el mockup)
 * y se rellenan luego con setValores(...) cuando el backend calcule los datos reales.
 */
public class IndicatorRow extends JPanel {

    private static final Color BORDE = new Color(0xB0, 0xB0, 0xB0);
    private static final Color CAJA_FONDO = new Color(0xD9, 0xE9, 0xF7);
    private static final Color TEXTO = new Color(0x2A, 0x2A, 0x2A);

    private final JLabel valor1;
    private final JLabel valor2;

    public IndicatorRow(String etiqueta) {
        setLayout(new BorderLayout(0, 0));
        setOpaque(false);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        setPreferredSize(new Dimension(0, 42));

        JLabel label = new JLabel("  " + etiqueta);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        label.setForeground(TEXTO);
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, BORDE));
        add(label, BorderLayout.CENTER);

        JPanel cajas = new JPanel(new GridLayout(1, 2, 2, 0));
        cajas.setOpaque(false);
        cajas.setPreferredSize(new Dimension(220, 42));

        valor1 = crearCaja();
        valor2 = crearCaja();
        cajas.add(valor1);
        cajas.add(valor2);

        add(cajas, BorderLayout.EAST);
    }

    private JLabel crearCaja() {
        JLabel caja = new JLabel("", SwingConstants.CENTER);
        caja.setOpaque(true);
        caja.setBackground(CAJA_FONDO);
        caja.setFont(new Font("SansSerif", Font.PLAIN, 15));
        caja.setForeground(TEXTO);
        caja.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, BORDE));
        return caja;
    }

    // TODO: Cristian llama esto cuando el backend tenga el cálculo real del indicador.
    public void setValores(String valor1Texto, String valor2Texto) {
        valor1.setText(valor1Texto);
        valor2.setText(valor2Texto);
    }
}