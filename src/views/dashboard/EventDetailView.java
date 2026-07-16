package views.dashboard;

import modules.event.model.Event;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class EventDetailView extends JFrame {

    private static final Color CYAN_ACCENT       = new Color(6, 182, 212); // #06B6D4 (Cian NoCTRA)
    private static final Color CYAN_HOVER        = new Color(8, 145, 178);  // #0891B2
    private static final Color AZUL_FONDO        = new Color(248, 250, 252); // #F8FAFC (Entorno general gris muy claro)
    private static final Color TEXTO_OSCURO      = new Color(0x1A, 0x20, 0x2C); // #1A202C (Negro/Gris oscuro)
    private static final Color TEXTO_MUTED       = new Color(0x4A, 0x55, 0x68); // #4A5568 (Gris oscuro/medio)
    private static final Color COLOR_CARD_BORDER = new Color(0xE2, 0xE8, 0xF0); // #E2E8F0 (Borde suave)
    private static final Color GRIS_CLARO_BOX    = new Color(241, 245, 249); // #F1F5F9 (Fondo gris muy claro para recuadros)

    private static final DateTimeFormatter FORMATO_FECHA_HORA =
            DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy · h:mm a", new java.util.Locale("es", "ES"));

    private final Event event;
    private EventStats stats;
    private JPanel contentPane;

    private static final java.util.List<EventDetailView> activeViews = new java.util.concurrent.CopyOnWriteArrayList<>();

    public static void refreshAll(int eventId) {
        for (EventDetailView view : activeViews) {
            if (view.event.getId() == eventId) {
                SwingUtilities.invokeLater(view::refreshData);
            }
        }
    }

    public EventDetailView(Event event) {
        this.event = event;
        this.stats = obtenerEstadisticas(event.getId());
        activeViews.add(this);

        setTitle("Noctra - Panel de Indicadores Estadísticos");
        setSize(1300, 850);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel de contenido principal con fondo gris claro
        this.contentPane = new JPanel(new BorderLayout(0, 20));
        this.contentPane.setBackground(AZUL_FONDO);
        this.contentPane.setBorder(BorderFactory.createEmptyBorder(24, 32, 0, 32)); // Sin padding abajo para pegar el banner
        setContentPane(this.contentPane);

        buildUI();
    }

    private void buildUI() {
        // 1. Cabecera (Parte superior)
        this.contentPane.add(crearCabeceraPanel(), BorderLayout.NORTH);

        // 2. Cuerpo Central (Dividido en dos columnas mediante sub-paneles)
        JPanel gridPanel = new JPanel(new GridLayout(1, 2, 35, 0));
        gridPanel.setOpaque(false);

        // Columna Izquierda: Tarjeta de Resumen + Botón "+ Invitados" abajo
        JPanel panelColumnaIzquierda = new JPanel(new BorderLayout(0, 20));
        panelColumnaIzquierda.setOpaque(false);
        panelColumnaIzquierda.add(crearTarjetaResumen(), BorderLayout.CENTER);

        JPanel panelBotonWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelBotonWrapper.setOpaque(false);
        SolidCianButton btnInvitados = new SolidCianButton("+ Invitados");
        btnInvitados.setPreferredSize(new Dimension(200, 48));
        btnInvitados.addActionListener(e -> {
            SendInvitationView modalInvitacion = new SendInvitationView(this);
            SendInvitationController controladorInvitacion = new SendInvitationController(modalInvitacion, event.getId());
            controladorInvitacion.mostrar();
        });
        panelBotonWrapper.add(btnInvitados);
        panelColumnaIzquierda.add(panelBotonWrapper, BorderLayout.SOUTH);

        gridPanel.add(panelColumnaIzquierda);

        // Columna Derecha: Tarjeta de Asistentes
        gridPanel.add(crearTarjetaAsistentes());

        this.contentPane.add(gridPanel, BorderLayout.CENTER);

        // 3. Banner Inferior (Pegado al borde inferior)
        this.contentPane.add(new BottomBanner(), BorderLayout.SOUTH);
    }

    public void refreshData() {
        this.stats = obtenerEstadisticas(event.getId());
        this.contentPane.removeAll();
        buildUI();
        this.contentPane.revalidate();
        this.contentPane.repaint();
    }

    @Override
    public void dispose() {
        activeViews.remove(this);
        super.dispose();
    }

    // ==================== LÓGICA DE ESTADÍSTICAS Y CONSULTA A BD ====================

    private static class EventStats {
        String momentoMayorGente = "N/A";
        double personasPorMinuto = 0.0;
        double hombresCadaUnaMujer = 0.0;
        int reingresoMujeres = 0;
        int reingresoHombres = 0;
        int totalAsistentes = 0;
        int nuevos = 0;
        int recurrentes = 0;
    }

    private EventStats obtenerEstadisticas(int eventId) {
        EventStats eventStats = new EventStats();
        boolean hasData = false;

        String sql = "{CALL sp_get_event_stats(?)}";

        try (Connection conn = config.DbConnection.getConnection();
             java.sql.CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, eventId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int totalAsistentes = rs.getInt("total_asistentes");
                    int recurrentes = rs.getInt("recurrentes");
                    int nuevos = rs.getInt("nuevos");
                    int reingresoMujeres = rs.getInt("reingreso_mujeres");
                    int reingresoHombres = rs.getInt("reingreso_hombres");
                    int hombres = rs.getInt("hombres");
                    int mujeres = rs.getInt("mujeres");
                    int horaPico = rs.getInt("hora_pico");
                    int entrando15m = rs.getInt("entrando_15m");
                    int totalEntradasPpm = rs.getInt("total_entradas_ppm");
                    Timestamp primero = rs.getTimestamp("primero_scan_at");
                    Timestamp ultimo = rs.getTimestamp("ultimo_scan_at");

                    // Rellenar objeto stats
                    eventStats.totalAsistentes = totalAsistentes;
                    eventStats.recurrentes = recurrentes;
                    eventStats.nuevos = nuevos;
                    eventStats.reingresoMujeres = reingresoMujeres;
                    eventStats.reingresoHombres = reingresoHombres;

                    if (totalAsistentes > 0) {
                        hasData = true;
                    }

                    // Calcular proporción
                    if (mujeres > 0) {
                        eventStats.hombresCadaUnaMujer = Math.round((double) hombres / mujeres * 10.0) / 10.0;
                    } else if (hombres > 0) {
                        eventStats.hombresCadaUnaMujer = hombres;
                    } else {
                        eventStats.hombresCadaUnaMujer = 0.0;
                    }

                    // Formatear Hora Pico
                    if (horaPico >= 0) {
                        String ampm = (horaPico >= 12) ? "PM" : "AM";
                        int displayHour = (horaPico % 12 == 0) ? 12 : (horaPico % 12);
                        eventStats.momentoMayorGente = String.format("%02d:00 %s", displayHour, ampm);
                    } else {
                        eventStats.momentoMayorGente = "N/A";
                    }

                    // Calcular personas por minuto
                    if (entrando15m > 0) {
                        eventStats.personasPorMinuto = Math.round((entrando15m / 15.0) * 10.0) / 10.0;
                    } else if (totalEntradasPpm > 0 && primero != null && ultimo != null) {
                        long diffMs = ultimo.getTime() - primero.getTime();
                        double mins = diffMs / 60000.0;
                        if (mins > 1.0) {
                            eventStats.personasPorMinuto = Math.round((totalEntradasPpm / mins) * 10.0) / 10.0;
                        } else {
                            eventStats.personasPorMinuto = totalEntradasPpm;
                        }
                    } else {
                        eventStats.personasPorMinuto = 0.0;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al ejecutar procedimiento sp_get_event_stats: " + e.getMessage());
            e.printStackTrace();
        }

        // Si no se encuentran datos en la BD, dejamos los valores por defecto (0 y N/A)

        return eventStats;
    }


    // ==================== MÉTODOS DE CREACIÓN DE INTERFAZ ====================

    private JPanel crearCabeceraPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        // 1. Botón de retroceso "< Indicadores Estadísticos"
        JLabel volver = new JLabel("< Indicadores Estadísticos");
        volver.setFont(new Font("SansSerif", Font.PLAIN, 15));
        volver.setForeground(CYAN_ACCENT);
        volver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        volver.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                volver.setForeground(CYAN_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                volver.setForeground(CYAN_ACCENT);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });
        panel.add(volver, BorderLayout.NORTH);

        // 2. Fila de Título y Metadata
        JPanel infoPanel = new JPanel(new BorderLayout(20, 0));
        infoPanel.setOpaque(false);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        // Título del evento
        String eventName = event.getName() != null ? event.getName() : "Fiesta Universitaria 2025";
        JLabel lblTitle = new JLabel(eventName);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 30));
        lblTitle.setForeground(TEXTO_OSCURO);
        textPanel.add(lblTitle);

        textPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        // Ubicación y Fecha del evento con iconos simples
        JPanel subPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        subPanel.setOpaque(false);

        JLabel lblPin = new JLabel("📍");
        lblPin.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        String address = event.getAddress() != null ? event.getAddress() : "Lima, Perú";
        JLabel lblLoc = new JLabel(address);
        lblLoc.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblLoc.setForeground(TEXTO_MUTED);

        JLabel lblSep = new JLabel("  ·  ");
        lblSep.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSep.setForeground(new Color(0xA0, 0xAE, 0xC0));

        JLabel lblCal = new JLabel("📅");
        lblCal.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));

        String formattedDate = FORMATO_FECHA_HORA.format(event.getStartDate());
        JLabel lblDateText = new JLabel(formattedDate);
        lblDateText.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblDateText.setForeground(TEXTO_MUTED);

        subPanel.add(lblPin);
        subPanel.add(lblLoc);
        subPanel.add(lblSep);
        subPanel.add(lblCal);
        subPanel.add(lblDateText);
        textPanel.add(subPanel);

        infoPanel.add(textPanel, BorderLayout.WEST);

        // Botón en forma de píldora a la derecha
        JPanel pill = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(236, 254, 255)); // Cian muy tenue (cyan 50)
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
            }
        };
        pill.setOpaque(false);

        JLabel pillCal = new JLabel("📅");
        pillCal.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        pillCal.setForeground(CYAN_ACCENT);

        JLabel pillText = new JLabel(formattedDate);
        pillText.setFont(new Font("SansSerif", Font.BOLD, 14));
        pillText.setForeground(CYAN_ACCENT);

        pill.add(pillCal);
        pill.add(pillText);

        JPanel pillWrapper = new JPanel(new GridBagLayout());
        pillWrapper.setOpaque(false);
        pillWrapper.add(pill);
        infoPanel.add(pillWrapper, BorderLayout.EAST);

        panel.add(infoPanel, BorderLayout.CENTER);
        return panel;
    }

    private CardPanel crearTarjetaResumen() {
        CardPanel panel = new CardPanel();
        panel.setLayout(new BorderLayout(0, 15));

        // Cabecera de la tarjeta
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        header.setOpaque(false);

        JLabel pulseIcon = new JLabel("📈"); // Icono de pulso / gráfico
        pulseIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        pulseIcon.setForeground(CYAN_ACCENT);

        JLabel title = new JLabel("Resumen General");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(TEXTO_OSCURO);

        header.add(pulseIcon);
        header.add(title);
        panel.add(header, BorderLayout.NORTH);

        // Lista de métricas
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        String picoVal = stats.momentoMayorGente;
        String ppmVal = String.valueOf((int) stats.personasPorMinuto);
        String ratioVal = stats.hombresCadaUnaMujer > 0 ? formatRatio(stats.hombresCadaUnaMujer) : "N/A";
        String reFemVal = String.valueOf(stats.reingresoMujeres);
        String reMascVal = String.valueOf(stats.reingresoHombres);

        listPanel.add(new StatRow("🕒", "Momento de mayor gente", "Hora de máxima afluencia", picoVal, "Hora pico"));
        listPanel.add(crearLineaDivisora());
        listPanel.add(new StatRow("🚶", "Personas entrando por minuto", "Flujo de entrada reciente", ppmVal, "personas/min"));
        listPanel.add(crearLineaDivisora());
        listPanel.add(new StatRow("👫", "Hombres cada una mujer", "Proporción por género", ratioVal, "H:M"));
        listPanel.add(crearLineaDivisora());
        listPanel.add(new StatRow("🔄", "Reingreso Mujeres", "Retornos femeninos", reFemVal, "reingresos"));
        listPanel.add(crearLineaDivisora());
        listPanel.add(new StatRow("🔄", "Reingreso Hombres", "Retornos masculinos", reMascVal, "reingresos"));

        panel.add(listPanel, BorderLayout.CENTER);
        return panel;
    }

    private String formatRatio(double val) {
        if (val == (long) val) {
            return String.format("%d:1", (long) val);
        } else {
            return String.format("%.1f:1", val);
        }
    }

    private JComponent crearLineaDivisora() {
        JPanel line = new JPanel();
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        line.setPreferredSize(new Dimension(0, 1));
        line.setBackground(COLOR_CARD_BORDER);
        return line;
    }

    private CardPanel crearTarjetaAsistentes() {
        CardPanel panel = new CardPanel();
        panel.setLayout(new BorderLayout(0, 20));

        // Cabecera de la tarjeta
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        header.setOpaque(false);

        JLabel groupIcon = new JLabel("👥"); // Icono de grupo
        groupIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        groupIcon.setForeground(CYAN_ACCENT);

        JLabel title = new JLabel("Asistentes");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(TEXTO_OSCURO);

        header.add(groupIcon);
        header.add(title);
        panel.add(header, BorderLayout.NORTH);

        // Sección Central (Dos columnas)
        JPanel centerSection = new JPanel(new GridLayout(1, 2, 20, 0));
        centerSection.setOpaque(false);

        int totalVal = stats.totalAsistentes;
        int nuevosVal = stats.nuevos;
        int recurrentesVal = stats.recurrentes;
        int nuevosPct = (totalVal > 0) ? (nuevosVal * 100 / totalVal) : 0;
        int recurrentesPct = (totalVal > 0) ? (recurrentesVal * 100 / totalVal) : 0;

        // Columna Izquierda: Gráfico de dona grueso
        DonutChart chart = new DonutChart(totalVal, nuevosVal, recurrentesVal);
        centerSection.add(chart);

        // Columna Derecha: Leyenda vertical
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
        legendPanel.setOpaque(false);
        legendPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Fila Nuevos (Cian oscuro)
        JPanel rowNuevos = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        rowNuevos.setOpaque(false);
        JLabel dotNuevos = new JLabel("●");
        dotNuevos.setFont(new Font("SansSerif", Font.BOLD, 18));
        dotNuevos.setForeground(new Color(8, 145, 178)); // Cian oscuro
        JPanel textNuevosPanel = new JPanel();
        textNuevosPanel.setLayout(new BoxLayout(textNuevosPanel, BoxLayout.Y_AXIS));
        textNuevosPanel.setOpaque(false);
        JLabel lblNuevosTitle = new JLabel("Nuevos");
        lblNuevosTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblNuevosTitle.setForeground(TEXTO_OSCURO);
        JLabel lblNuevosVal = new JLabel(nuevosVal + " (" + nuevosPct + "%)");
        lblNuevosVal.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblNuevosVal.setForeground(TEXTO_MUTED);
        textNuevosPanel.add(lblNuevosTitle);
        textNuevosPanel.add(lblNuevosVal);
        rowNuevos.add(dotNuevos);
        rowNuevos.add(textNuevosPanel);

        // Fila Recurrentes (Cian claro)
        JPanel rowRecurrentes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        rowRecurrentes.setOpaque(false);
        JLabel dotRecurrentes = new JLabel("●");
        dotRecurrentes.setFont(new Font("SansSerif", Font.BOLD, 18));
        dotRecurrentes.setForeground(new Color(207, 250, 254)); // Cian claro
        JPanel textRecurrentesPanel = new JPanel();
        textRecurrentesPanel.setLayout(new BoxLayout(textRecurrentesPanel, BoxLayout.Y_AXIS));
        textRecurrentesPanel.setOpaque(false);
        JLabel lblRecurrentesTitle = new JLabel("Recurrentes");
        lblRecurrentesTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblRecurrentesTitle.setForeground(TEXTO_OSCURO);
        JLabel lblRecurrentesVal = new JLabel(recurrentesVal + " (" + recurrentesPct + "%)");
        lblRecurrentesVal.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblRecurrentesVal.setForeground(TEXTO_MUTED);
        textRecurrentesPanel.add(lblRecurrentesTitle);
        textRecurrentesPanel.add(lblRecurrentesVal);
        rowRecurrentes.add(dotRecurrentes);
        rowRecurrentes.add(textRecurrentesPanel);

        // Fila Total
        JPanel rowTotal = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        rowTotal.setOpaque(false);
        JPanel textTotalPanel = new JPanel();
        textTotalPanel.setLayout(new BoxLayout(textTotalPanel, BoxLayout.Y_AXIS));
        textTotalPanel.setOpaque(false);
        JLabel lblTotalTitle = new JLabel("Total Asistentes");
        lblTotalTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblTotalTitle.setForeground(TEXTO_MUTED);
        JLabel lblTotalVal = new JLabel(totalVal + " (100%)");
        lblTotalVal.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTotalVal.setForeground(CYAN_ACCENT); // Destacado en cian
        textTotalPanel.add(lblTotalTitle);
        textTotalPanel.add(lblTotalVal);
        rowTotal.add(Box.createRigidArea(new Dimension(16, 0))); // Alineación
        rowTotal.add(textTotalPanel);

        legendPanel.add(rowNuevos);
        legendPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        legendPanel.add(rowRecurrentes);
        legendPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        legendPanel.add(rowTotal);

        centerSection.add(legendPanel);
        panel.add(centerSection, BorderLayout.CENTER);

        // Recuadro Inferior (Distribución de asistentes)
        JPanel bottomBox = new JPanel(new BorderLayout(15, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GRIS_CLARO_BOX);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        bottomBox.setOpaque(false);
        bottomBox.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        // Izquierda del recuadro (Icono de barras, título y subtítulo)
        JPanel boxLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        boxLeft.setOpaque(false);
        JLabel barIcon = new JLabel("📊");
        barIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        barIcon.setForeground(CYAN_ACCENT);

        JPanel boxLeftText = new JPanel();
        boxLeftText.setLayout(new BoxLayout(boxLeftText, BoxLayout.Y_AXIS));
        boxLeftText.setOpaque(false);
        JLabel boxTitle = new JLabel("Distribución de asistentes");
        boxTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        boxTitle.setForeground(TEXTO_OSCURO);
        JLabel boxSub = new JLabel("Muestreo en tiempo real");
        boxSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        boxSub.setForeground(TEXTO_MUTED);
        boxLeftText.add(boxTitle);
        boxLeftText.add(boxSub);
        boxLeft.add(barIcon);
        boxLeft.add(boxLeftText);
        bottomBox.add(boxLeft, BorderLayout.WEST);

        // Derecha del recuadro (100% Cobertura total)
        JPanel boxRight = new JPanel();
        boxRight.setLayout(new BoxLayout(boxRight, BoxLayout.Y_AXIS));
        boxRight.setOpaque(false);
        boxRight.setAlignmentX(Component.RIGHT_ALIGNMENT);
        JLabel pctLabel = new JLabel("100%", SwingConstants.RIGHT);
        pctLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        pctLabel.setForeground(CYAN_ACCENT);
        pctLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        JLabel covLabel = new JLabel("Cobertura total", SwingConstants.RIGHT);
        covLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        covLabel.setForeground(TEXTO_MUTED);
        covLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        boxRight.add(pctLabel);
        boxRight.add(covLabel);
        bottomBox.add(boxRight, BorderLayout.EAST);

        panel.add(bottomBox, BorderLayout.SOUTH);
        return panel;
    }

    // ==================== COMPONENTES PERSONALIZADOS ====================

    public static class CardPanel extends JPanel {
        public CardPanel() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Fondo blanco redondeado
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

            // Borde sutil que simula la sombra suave
            g2.setColor(COLOR_CARD_BORDER);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class StatRow extends JPanel {
        public StatRow(String icon, String titleText, String subtitleText, String valueText, String labelText) {
            setLayout(new BorderLayout(15, 0));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

            // Recuadro sólido cian a la extrema izquierda
            JPanel iconBox = new JPanel(new GridBagLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(CYAN_ACCENT);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.dispose();
                }
            };
            iconBox.setOpaque(false);
            iconBox.setPreferredSize(new Dimension(42, 42));
            JLabel lblIcon = new JLabel(icon);
            lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
            lblIcon.setForeground(Color.WHITE); // Icono en blanco
            iconBox.add(lblIcon);

            // Textos centrales (Título negro, subtítulo gris oscuro)
            JPanel centerText = new JPanel();
            centerText.setLayout(new BoxLayout(centerText, BoxLayout.Y_AXIS));
            centerText.setOpaque(false);

            JLabel lblTitle = new JLabel(titleText);
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
            lblTitle.setForeground(TEXTO_OSCURO);

            JLabel lblSub = new JLabel(subtitleText);
            lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lblSub.setForeground(TEXTO_MUTED);

            centerText.add(lblTitle);
            centerText.add(Box.createRigidArea(new Dimension(0, 2)));
            centerText.add(lblSub);

            // Datos a la extrema derecha (Valor grande en cian, etiqueta gris abajo)
            JPanel rightText = new JPanel();
            rightText.setLayout(new BoxLayout(rightText, BoxLayout.Y_AXIS));
            rightText.setOpaque(false);
            rightText.setAlignmentX(Component.RIGHT_ALIGNMENT);

            JLabel lblVal = new JLabel(valueText, SwingConstants.RIGHT);
            lblVal.setFont(new Font("SansSerif", Font.BOLD, 20));
            lblVal.setForeground(CYAN_ACCENT);
            lblVal.setAlignmentX(Component.RIGHT_ALIGNMENT);

            JLabel lblLbl = new JLabel(labelText, SwingConstants.RIGHT);
            lblLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
            lblLbl.setForeground(TEXTO_MUTED);
            lblLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);

            rightText.add(lblVal);
            rightText.add(lblLbl);

            add(iconBox, BorderLayout.WEST);
            add(centerText, BorderLayout.CENTER);
            add(rightText, BorderLayout.EAST);
        }
    }

    public static class DonutChart extends JComponent {
        private final int total;
        private final int nuevos;
        private final int recurrentes;

        public DonutChart(int total, int nuevos, int recurrentes) {
            this.total = total;
            this.nuevos = nuevos;
            this.recurrentes = recurrentes;
            setPreferredSize(new Dimension(200, 200));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight()) - 30;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;
            int strokeWidth = 28; // Anillo grueso

            g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));

            if (total == 0) {
                // Dibujar un anillo gris sutil si no hay asistentes
                g2.setColor(new Color(226, 232, 240));
                g2.drawArc(x + strokeWidth / 2, y + strokeWidth / 2, size - strokeWidth, size - strokeWidth, 0, 360);
            } else {
                double angleNuevos = (360.0 * nuevos) / total;
                double angleRecurrentes = (360.0 * recurrentes) / total;

                // Arco de Nuevos (Cian oscuro)
                g2.setColor(new Color(8, 145, 178));
                g2.drawArc(x + strokeWidth / 2, y + strokeWidth / 2, size - strokeWidth, size - strokeWidth, 90, (int) -angleNuevos);

                // Arco de Recurrentes (Cian claro)
                g2.setColor(new Color(207, 250, 254));
                g2.drawArc(x + strokeWidth / 2, y + strokeWidth / 2, size - strokeWidth, size - strokeWidth, (int) (90 - angleNuevos), (int) -angleRecurrentes);
            }

            // Texto Central: Número Grande
            g2.setColor(TEXTO_OSCURO);
            g2.setFont(new Font("SansSerif", Font.BOLD, 32));
            String totalStr = String.valueOf(total);
            FontMetrics fmTotal = g2.getFontMetrics();
            int totalWidth = fmTotal.stringWidth(totalStr);
            g2.drawString(totalStr, getWidth() / 2 - totalWidth / 2, getHeight() / 2 + 10);

            // Texto Central: "Total Asistentes"
            g2.setColor(TEXTO_MUTED);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            String labelStr = "Total Asistentes";
            FontMetrics fmLabel = g2.getFontMetrics();
            int labelWidth = fmLabel.stringWidth(labelStr);
            g2.drawString(labelStr, getWidth() / 2 - labelWidth / 2, getHeight() / 2 - 20);

            g2.dispose();
        }
    }

    public static class SolidCianButton extends JButton {
        public SolidCianButton(String texto) {
            super(texto);
            setFont(new Font("SansSerif", Font.BOLD, 16));
            setForeground(Color.WHITE);
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

            if (getModel().isRollover()) {
                g2.setColor(CYAN_HOVER);
            } else {
                g2.setColor(CYAN_ACCENT);
            }
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class BottomBanner extends JPanel {
        public BottomBanner() {
            setLayout(new BorderLayout());
            setBackground(GRIS_CLARO_BOX);
            setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));

            // Izquierda: icono de gráfico de líneas, título e info
            JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
            leftPanel.setOpaque(false);

            JPanel iconBox = new JPanel(new GridBagLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(CYAN_ACCENT);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.dispose();
                }
            };
            iconBox.setOpaque(false);
            iconBox.setPreferredSize(new Dimension(36, 36));
            JLabel lblIcon = new JLabel("📈");
            lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
            lblIcon.setForeground(Color.WHITE);
            iconBox.add(lblIcon);

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setOpaque(false);

            JLabel lblTitle = new JLabel("Información en tiempo real");
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
            lblTitle.setForeground(TEXTO_OSCURO);

            JLabel lblDesc = new JLabel("Los datos se actualizan automáticamente al registrar nuevos escaneos en la base de datos.");
            lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lblDesc.setForeground(TEXTO_MUTED);

            textPanel.add(lblTitle);
            textPanel.add(lblDesc);

            leftPanel.add(iconBox);
            leftPanel.add(textPanel);
            add(leftPanel, BorderLayout.WEST);

            // Derecha: píldora "En vivo"
            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            rightPanel.setOpaque(false);

            JPanel pill = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(224, 242, 254)); // Fondo azul celeste tenue
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.dispose();
                }
            };
            pill.setOpaque(false);

            JLabel dot = new JLabel("●");
            dot.setFont(new Font("SansSerif", Font.BOLD, 14));
            dot.setForeground(CYAN_ACCENT);

            JLabel text = new JLabel("En vivo");
            text.setFont(new Font("SansSerif", Font.BOLD, 13));
            text.setForeground(CYAN_ACCENT);

            pill.add(dot);
            pill.add(text);
            rightPanel.add(pill);

            add(rightPanel, BorderLayout.EAST);
        }
    }
}