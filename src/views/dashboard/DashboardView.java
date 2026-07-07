package views.dashboard;

import modules.event.model.Event;
import views.components.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DashboardView extends JFrame {

    private static final Color BLANCO = Color.WHITE;
    private static final Color AZUL_FONDO = new Color(0xEA, 0xF3, 0xFB);
    private static final Color CYAN_LOGO = new Color(0x6F, 0xD8, 0xE0);
    private static final Color TEXTO_OSCURO = new Color(0x2A, 0x2A, 0x2A);
    private static final Color ROJO_BADGE = new Color(0xE5, 0x3E, 0x3E);
    private static final Color AVATAR_FONDO = new Color(0x3E, 0x6B, 0x84);

    private final String nombreUsuario;
    private final int cantidadNotificaciones;

    public DashboardView(String nombreUsuario, int cantidadNotificaciones) {
        this.nombreUsuario = nombreUsuario;
        this.cantidadNotificaciones = cantidadNotificaciones;

        setTitle("Noctra - Eventos");
        setSize(1500, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(crearBarraSuperior(), BorderLayout.NORTH);
        add(crearBarraLateral(), BorderLayout.WEST);
        add(crearPanelContenido(), BorderLayout.CENTER);
    }

    // ---------- BARRA SUPERIOR ----------

    private JPanel crearBarraSuperior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(BLANCO);
        barra.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        barra.setPreferredSize(new Dimension(0, 80));

        JLabel logo = new JLabel("NOCTRA");
        logo.setFont(new Font("SansSerif", Font.BOLD, 30));
        logo.setForeground(CYAN_LOGO);
        barra.add(logo, BorderLayout.WEST);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        derecha.setOpaque(false);
        derecha.add(crearNotificacion());
        derecha.add(crearSeparador());

        JLabel saludo = new JLabel("<html>Hola, <b>" + nombreUsuario + "</b></html>");
        saludo.setFont(new Font("SansSerif", Font.PLAIN, 18));
        saludo.setForeground(TEXTO_OSCURO);
        derecha.add(saludo);
        derecha.add(crearAvatar());

        barra.add(derecha, BorderLayout.EAST);
        return barra;
    }

    private JComponent crearNotificacion() {
        JPanel contenedor = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 22));
                g2.setColor(TEXTO_OSCURO);
                g2.drawString("\uD83D\uDD14", 0, 24);

                if (cantidadNotificaciones > 0) {
                    g2.setColor(ROJO_BADGE);
                    g2.fillOval(16, 0, 18, 18);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                    g2.drawString(String.valueOf(cantidadNotificaciones), 21, 13);
                }
                g2.dispose();
            }
        };
        contenedor.setOpaque(false);
        contenedor.setPreferredSize(new Dimension(34, 30));
        return contenedor;
    }

    private JComponent crearSeparador() {
        JLabel linea = new JLabel("|");
        linea.setFont(new Font("SansSerif", Font.PLAIN, 22));
        linea.setForeground(new Color(0xC0, 0xC0, 0xC0));
        return linea;
    }

    private JComponent crearAvatar() {
        JPanel avatar = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AVATAR_FONDO);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.fillOval(getWidth() / 2 - 6, 8, 12, 12);
                g2.fillOval(getWidth() / 2 - 11, 24, 22, 14);
                g2.dispose();
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(46, 46));
        return avatar;
    }

    // ---------- BARRA LATERAL ----------

    private JPanel crearBarraLateral() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(AZUL_FONDO);
        panel.setPreferredSize(new Dimension(330, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        SidebarItem eventos = new SidebarItem("\uD83C\uDF77", "Eventos", true);
        SidebarItem colaboradores = new SidebarItem("\uD83D\uDC65", "Colaboradores", false);

        eventos.setAlignmentX(Component.LEFT_ALIGNMENT);
        colaboradores.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(eventos);
        panel.add(colaboradores);

        // TODO: agregar MouseListener a cada SidebarItem para cambiar entre
        // la vista de Eventos y la vista de Colaboradores (CardLayout, por ejemplo)

        return panel;
    }

    // ---------- CONTENIDO ----------

    private JPanel crearPanelContenido() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(AZUL_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        panel.add(crearBotonesAccion());
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(crearSeccionEventos());

        return panel;
    }

    private JPanel crearBotonesAccion() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        OutlineButton nuevoEvento = new OutlineButton("Nuevo evento");
        nuevoEvento.setPreferredSize(new Dimension(220, 45));

        OutlineButton colaborar = new OutlineButton("Colaborar");
        colaborar.setPreferredSize(new Dimension(220, 45));

        // TODO: nuevoEvento.addActionListener(e -> ...) -> abrir formulario de creación de evento
        // TODO: colaborar.addActionListener(e -> ...) -> abrir flujo para unirse como colaborador

        panel.add(nuevoEvento);
        panel.add(colaborar);
        return panel;
    }

    private JPanel crearSeccionEventos() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        List<Event> eventos = obtenerEventosDeEjemplo(); // TODO: reemplazar por EventService real
        LocalDate hoy = LocalDate.now();

        List<Event> activos = new ArrayList<>();
        List<Event> proximos = new ArrayList<>();
        for (Event evento : eventos) {
            if (evento.isActive(hoy)) {
                activos.add(evento);
            } else if (evento.isUpcoming(hoy)) {
                proximos.add(evento);
            }
        }

        List<JComponent> filasActivos = new ArrayList<>();
        for (int i = 0; i < activos.size(); i++) {
            filasActivos.add(new EventRowPanel(activos.get(i), true, i < activos.size() - 1));
        }

        List<JComponent> filasProximos = new ArrayList<>();
        for (int i = 0; i < proximos.size(); i++) {
            filasProximos.add(new EventRowPanel(proximos.get(i), false, i < proximos.size() - 1));
        }

        CollapsibleSectionPanel seccionActivos = new CollapsibleSectionPanel("Eventos Activos", filasActivos);
        seccionActivos.setAlignmentX(Component.LEFT_ALIGNMENT);

        CollapsibleSectionPanel seccionProximos = new CollapsibleSectionPanel("Próximos Eventos", filasProximos);
        seccionProximos.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(seccionActivos);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(seccionProximos);

        return panel;
    }

    // ---------- DATOS DE EJEMPLO (solo para maquetar, sacar cuando exista EventService) ----------

    private List<Event> obtenerEventosDeEjemplo() {
        List<Event> lista = new ArrayList<>();
        LocalDate hoy = LocalDate.now();

        lista.add(new Event(1, "Gala benéfica \"Buena Ventura\"", "Salón Central",
                LocalDateTime.of(hoy, LocalTime.of(18, 30)),
                LocalDateTime.of(hoy, LocalTime.of(23, 0)), 120));

        lista.add(new Event(1, "Cumpleaños \"Mario Vargas\"", "Quinta Los Álamos",
                LocalDateTime.of(hoy, LocalTime.of(20, 0)),
                LocalDateTime.of(hoy, LocalTime.of(23, 59)), 60));

        lista.add(new Event(1, "Firma de Libros \"Agatha Christie\"", "Librería Central",
                LocalDateTime.of(hoy.plusDays(2), LocalTime.of(10, 0)),
                LocalDateTime.of(hoy.plusDays(2), LocalTime.of(13, 0)), 40));

        lista.add(new Event(1, "Cumpleaños \"Fernando Trujillo\"", "Casa de eventos Trujillo",
                LocalDateTime.of(hoy.plusDays(3), LocalTime.of(20, 0)),
                LocalDateTime.of(hoy.plusDays(3), LocalTime.of(23, 59)), 50));

        lista.add(new Event(1, "Cumpleaños \"Jane Austen\"", "Jardín Botánico",
                LocalDateTime.of(hoy.plusDays(5), LocalTime.of(20, 0)),
                LocalDateTime.of(hoy.plusDays(5), LocalTime.of(23, 59)), 80));

        lista.add(new Event(1, "Graduación \"Unidos\"", "Auditorio Municipal",
                LocalDateTime.of(hoy.plusDays(7), LocalTime.of(20, 0)),
                LocalDateTime.of(hoy.plusDays(7), LocalTime.of(23, 59)), 200));

        lista.add(new Event(1, "Cena Empresarial \"Seven Seas\"", "Restaurante Seven Seas",
                LocalDateTime.of(hoy.plusDays(9), LocalTime.of(20, 0)),
                LocalDateTime.of(hoy.plusDays(9), LocalTime.of(23, 59)), 30));

        return lista;
    }
}