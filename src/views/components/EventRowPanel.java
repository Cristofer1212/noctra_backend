package views.components;

import modules.event.model.Event;
import modules.shared.config.ConfigLoader;
import modules.shared.http.HttpClientWrapper;
import views.dashboard.DashboardView;

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

    private final DashboardView dashboardView;

    private static final DateTimeFormatter FORMATO_HORA =
            DateTimeFormatter.ofPattern("h:mm a", new Locale("es", "ES"));

    /**
     * @param event         el evento a mostrar
     * @param esActivo      true -> muestra hora de inicio | false -> muestra fecha
     * @param mostrarLinea  true si no es la última fila de la lista (para la línea divisora)
     */
    public EventRowPanel(DashboardView dashboardView, Event event, boolean esActivo, boolean mostrarLinea) {

        this.dashboardView = dashboardView;


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

        JButton btnDelete = new JButton("\uD83D\uDDD1"); // Icono de tacho de basura Unicode
        btnDelete.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14)); // Fuente que soporta emojis/iconos
        btnDelete.setPreferredSize(new Dimension(30, 25));
        btnDelete.setMargin(new Insets(0, 0, 0, 0)); // IMPORTANTE: Elimina el margen interno que corta el icono
        btnDelete.setBackground(new Color(0, 0, 0, 0)); // Color totalmente transparente
        btnDelete.setContentAreaFilled(false);          // Desactiva el relleno del botón        btnDelete.setForeground(new Color(180, 50, 50));
        btnDelete.setFocusPainted(false);
        btnDelete.setBorderPainted(false);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnDelete.addActionListener(e -> handleDelete(event.getId()));

        // Creamos un panel contenedor para alinear el botón a la derecha y que no se estire
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnDelete);

        add(buttonPanel, BorderLayout.EAST);

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



    private void handleDelete(Integer eventId) {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar?");
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            // 1. Obtener y VERIFICAR la propiedad
            String baseUrl = ConfigLoader.getProperty("app.base.url");

            // DEBUG: Imprime para ver qué está leyendo realmente
            System.out.println("DEBUG: El valor de backend.local.url es: " + baseUrl);

            if (baseUrl == null || baseUrl.isEmpty()) {
                throw new Exception("La configuración 'backend.local.url' no se encontró en config.properties");
            }

            // 2. Ejecutar la llamada
            HttpClientWrapper httpClient = new HttpClientWrapper();
            httpClient.delete(baseUrl + "/event/" + eventId);
            dashboardView.actualizarEventos();

            JOptionPane.showMessageDialog(this, "Evento eliminado correctamente");


            // ... resto de la lógica ...
        } catch (Exception ex) {
            ex.printStackTrace(); // Esto nos ayudará a ver si falló el ConfigLoader
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
        }
    }
}