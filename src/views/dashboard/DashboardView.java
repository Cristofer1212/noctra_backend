package views.dashboard;

import modules.event.model.Event;
import views.auth.login.LoginView;
import views.components.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelTarjetas = new JPanel(cardLayout);
    private JScrollPane panelEventosActual;
    private SidebarItem itemEventos;

    public DashboardView(String nombreUsuario, int cantidadNotificaciones) {
        this.nombreUsuario = nombreUsuario;
        this.cantidadNotificaciones = cantidadNotificaciones;

        setTitle("Noctra - Eventos");
        setSize(1500, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        panelTarjetas.setOpaque(false);
        panelEventosActual = crearPanelEventos();
        panelTarjetas.add(panelEventosActual, "EVENTOS");

        add(crearBarraSuperior(), BorderLayout.NORTH);
        add(crearBarraLateral(), BorderLayout.WEST);
        add(panelTarjetas, BorderLayout.CENTER);

        // Carga la data real justo al iniciar
        SwingUtilities.invokeLater(() -> {
            new DashboardController().cargarEventosEnVista(this);
        });
    }

    // --- LÓGICA DE ACTUALIZACIÓN ---
    public void actualizarEventos() {
        new DashboardController().cargarEventosEnVista(this);

        if (itemEventos != null) {
            itemEventos.setSeleccionado(true);
        }
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

        JPopupMenu menuUsuario = crearMenuCerrarSesion();
        JLabel saludo = new JLabel("<html>Hola, <b>" + nombreUsuario + "</b></html>");
        saludo.setFont(new Font("SansSerif", Font.PLAIN, 18));
        saludo.setForeground(TEXTO_OSCURO);
        saludo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saludo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                menuUsuario.show(saludo, 0, saludo.getHeight() + 10);
            }
        });
        derecha.add(saludo);

        JComponent avatar = crearAvatar();
        avatar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        avatar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                menuUsuario.show(avatar, avatar.getWidth() - menuUsuario.getPreferredSize().width, avatar.getHeight() + 10);
            }
        });
        derecha.add(avatar);

        barra.add(derecha, BorderLayout.EAST);
        return barra;
    }

    private JPopupMenu crearMenuCerrarSesion() {
        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(BLANCO);
        menu.setBorder(new LineBorder(AVATAR_FONDO, 1, true));

        JMenuItem cerrarSesionItem = new JMenuItem("⊗   Cerrar sesión");
        cerrarSesionItem.setFont(new Font("SansSerif", Font.PLAIN, 16));
        cerrarSesionItem.setForeground(TEXTO_OSCURO);
        cerrarSesionItem.setBackground(BLANCO);
        cerrarSesionItem.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        cerrarSesionItem.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cerrarSesionItem.addActionListener(e -> {
            LoginView loginView = new LoginView(null);
            loginView.setVisible(true);
            dispose();
        });

        menu.add(cerrarSesionItem);
        return menu;
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

        SidebarItem eventos = new SidebarItem("🍷", "Eventos", true);
        itemEventos = eventos;

        eventos.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(eventos);

        eventos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(panelTarjetas, "EVENTOS");
                itemEventos.setSeleccionado(true);
            }
        });

        return panel;
    }

    // ---------- CONTENIDO ----------
    private JScrollPane crearPanelEventos() {
        return crearPanelEventosConDatos(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    private JPanel crearBotonesAccion() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        OutlineButton nuevoEvento = new OutlineButton("Nuevo evento");
        nuevoEvento.setPreferredSize(new Dimension(220, 45));

        nuevoEvento.addActionListener(e -> {
            NuevoEventoView nuevoEventoView = new NuevoEventoView(this);
            nuevoEventoView.setVisible(true);
        });

        panel.add(nuevoEvento);
        return panel;
    }

    private JComponent crearFilaClickeable(Event evento, boolean esActivo, boolean mostrarLinea) {
        EventRowPanel fila = new EventRowPanel(this, evento, esActivo, mostrarLinea);
        fila.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fila.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EventDetailView detalle = new EventDetailView(evento);
                detalle.setVisible(true);
            }
        });
        return fila;
    }

    public void renderizarEventos(List<Event> activos, List<Event> proximos, List<Event> pasados) {
        panelTarjetas.remove(panelEventosActual);

        panelEventosActual = crearPanelEventosConDatos(activos, proximos, pasados);

        panelTarjetas.add(panelEventosActual, "EVENTOS");
        cardLayout.show(panelTarjetas, "EVENTOS");

        panelTarjetas.revalidate();
        panelTarjetas.repaint();
    }

    private JScrollPane crearPanelEventosConDatos(List<Event> activos, List<Event> proximos, List<Event> pasados) {
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(AZUL_FONDO);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // 1. Añadimos componentes fijos de arriba
        panelContenido.add(crearBotonesAccion());
        panelContenido.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2. CORREGIDO: Pasamos correctamente las 3 listas al método de empaquetado de secciones
        panelContenido.add(crearSeccionEventosConDatos(activos, proximos, pasados));

        // 3. Envoltorio elástico para obligar al ScrollPane a leer la altura real de las N filas
        JPanel elasticoWrapper = new JPanel(new BorderLayout());
        elasticoWrapper.setBackground(AZUL_FONDO);
        elasticoWrapper.add(panelContenido, BorderLayout.NORTH);

        return aplicarScroll(elasticoWrapper);
    }

    // CORREGIDO: Firma del método expandida a 3 argumentos para soportar los eventos pasados
    private JPanel crearSeccionEventosConDatos(List<Event> activos, List<Event> proximos, List<Event> pasados) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Renderizado de eventos activos
        List<JComponent> filasActivos = new ArrayList<>();
        for (int i = 0; i < activos.size(); i++) {
            filasActivos.add(crearFilaClickeable(activos.get(i), true, i < activos.size() - 1));
        }

        // Renderizado de eventos próximos
        List<JComponent> filasProximos = new ArrayList<>();
        for (int i = 0; i < proximos.size(); i++) {
            filasProximos.add(crearFilaClickeable(proximos.get(i), false, i < proximos.size() - 1));
        }

        // Renderizado de eventos pasados
        List<JComponent> filasPasados = new ArrayList<>();
        for (int i = 0; i < pasados.size(); i++) {
            filasPasados.add(crearFilaClickeable(pasados.get(i), false, i < pasados.size() - 1));
        }

        CollapsibleSectionPanel seccionActivos = new CollapsibleSectionPanel("Eventos Activos", filasActivos);
        seccionActivos.setAlignmentX(Component.LEFT_ALIGNMENT);

        CollapsibleSectionPanel seccionProximos = new CollapsibleSectionPanel("Próximos Eventos", filasProximos);
        seccionProximos.setAlignmentX(Component.LEFT_ALIGNMENT);

        CollapsibleSectionPanel seccionPasados = new CollapsibleSectionPanel("Eventos Pasados", filasPasados);
        seccionPasados.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(seccionActivos);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(seccionProximos);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(seccionPasados);

        // CORREGIDO: Retornamos la variable local correcta 'panel'
        return panel;
    }

    private JScrollPane aplicarScroll(JPanel panelConWrapper) {
        JScrollPane scroll = new JScrollPane(panelConWrapper);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(AZUL_FONDO);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scroll;
    }
}