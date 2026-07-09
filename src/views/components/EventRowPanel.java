package views.components;

import modules.event.model.Event;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class EventRowPanel extends JPanel {

    private static final Color TEXTO_HORA = new Color(0x2A, 0x2A, 0x2A);
    private static final Color TEXTO_TITULO = new Color(0x2A, 0x2A, 0x2A);
    private static final Color LINEA = new Color(0x6E, 0x9C, 0xB5);

    private static final DateTimeFormatter FORMATO_HORA =
            DateTimeFormatter.ofPattern("h:mm a", new Locale("es", "ES"));

    /**
     * @param event         el evento a mostrar
     * @param esActivo      true -> muestra hora de inicio | false -> muestra fecha
     * @param mostrarLinea  true si no es la última fila de la lista (para la línea divisora)
     */
    public EventRowPanel(Event event, boolean esActivo, boolean mostrarLinea) {
        setLayout(new BorderLayout(20, 0));
        setOpaque(false);

        JLabel etiquetaFechaHora = new JLabel(formatearFechaHora(event, esActivo));
        etiquetaFechaHora.setFont(new Font("SansSerif", Font.BOLD, 15));
        etiquetaFechaHora.setForeground(TEXTO_HORA);
        etiquetaFechaHora.setPreferredSize(new Dimension(95, 20));
        add(etiquetaFechaHora, BorderLayout.WEST);

        JLabel etiquetaTitulo = new JLabel(event.getName());
        etiquetaTitulo.setFont(new Font("SansSerif", Font.PLAIN, 18));
        etiquetaTitulo.setForeground(TEXTO_TITULO);
        add(etiquetaTitulo, BorderLayout.CENTER);

        Border padding = BorderFactory.createEmptyBorder(10, 20, 10, 20);
        if (mostrarLinea) {
            setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 0, 1, 0, LINEA), padding));
        } else {
            setBorder(padding);
        }
    }

    private String formatearFechaHora(Event event, boolean esActivo) {
        if (esActivo) {
            return FORMATO_HORA.format(event.getStartDate()).toUpperCase();
        }
        String dia = capitalizar(event.getStartDate().getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, new Locale("es", "ES")));
        String mes = capitalizar(event.getStartDate().getMonth()
                .getDisplayName(TextStyle.SHORT, new Locale("es", "ES")));
        int diaMes = event.getStartDate().getDayOfMonth();
        return dia + " " + diaMes + " " + mes;
    }

    private String capitalizar(String texto) {
        if (texto.isEmpty()) return texto;
        return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }
}