package views.dashboard;

import modules.event.model.Event;
import modules.invitation.service.InvitationService;
import views.components.IndicatorRow;
import views.components.OutlineButton;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EventDetailView extends JFrame {

    private static final Color CYAN_TITULO   = new Color(0x6F, 0xD8, 0xE0);
    private static final Color AZUL_BADGE    = new Color(0x3E, 0x6B, 0x84);
    private static final Color AZUL_ENCABEZADO = new Color(0x9C, 0xD0, 0xF0);
    private static final Color TEXTO_OSCURO  = new Color(0x2A, 0x2A, 0x2A);
    private static final Color TEXTO_AZUL    = new Color(0x1C, 0x6F, 0xA0);
    private static final Color BORDE_TABLA   = new Color(0x2A, 0x2A, 0x2A);

    private static final DateTimeFormatter FORMATO_FECHA_HORA =
            DateTimeFormatter.ofPattern("d/M/yy – h:mm a");

    private final Event event;
    private final List<Object[]> colaboradores;
    private final int totalInvito;
    private final int totalAsistieron;

    public EventDetailView(Event event) {
        this.event = event;
        this.colaboradores = obtenerColaboradoresDeEjemplo(event);

        int sumaInvito = 0;
        int sumaAsistio = 0;
        for (Object[] fila : colaboradores) {
            sumaInvito += (int) fila[1];
            sumaAsistio += (int) fila[2];
        }
        this.totalInvito = sumaInvito;
        this.totalAsistieron = sumaAsistio;

        setTitle("Noctra - Detalle de Evento");
        setSize(1500, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contenido = new JPanel(new GridLayout(1, 2, 40, 0));
        contenido.setBackground(Color.WHITE);
        contenido.setBorder(BorderFactory.createEmptyBorder(35, 40, 35, 40));
        setContentPane(contenido);

        contenido.add(crearColumnaIzquierda());
        contenido.add(crearColumnaDerecha());
    }

    // TODO: Cristian - reemplazar esto por la consulta real, ej:
    // collaboratorService.findByEventId(event.getId())
    // Por ahora genero números de ejemplo basados en la capacidad del evento,
    // así al menos cada evento se ve distinto mientras no hay datos reales.
    private List<Object[]> obtenerColaboradoresDeEjemplo(Event event) {
        List<Object[]> lista = new ArrayList<>();
        int capacidad = (event.getCapacity() != null) ? event.getCapacity() : 50;

        int invito1 = Math.max(5, capacidad / 2);
        int invito2 = Math.max(3, capacidad / 4);
        int asistio1 = Math.round(invito1 * 0.55f);
        int asistio2 = Math.round(invito2 * 0.7f);

        lista.add(new Object[]{"Colaborador 1", invito1, asistio1});
        lista.add(new Object[]{"Colaborador 2", invito2, asistio2});
        return lista;
    }

    // ==================== COLUMNA IZQUIERDA ====================

    private JPanel crearColumnaIzquierda() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        panel.add(crearBotonVolver());
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(crearTitulo());
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(crearTablaColaboradores());
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(crearBotonesYAforo());

        return panel;
    }

    private JComponent crearBotonVolver() {
        JLabel volver = new JLabel("\u2190  Volver");
        volver.setFont(new Font("SansSerif", Font.PLAIN, 15));
        volver.setForeground(new Color(0x7A, 0x7A, 0x7A));
        volver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        volver.setAlignmentX(Component.LEFT_ALIGNMENT);

        volver.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                volver.setForeground(TEXTO_OSCURO);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                volver.setForeground(new Color(0x7A, 0x7A, 0x7A));
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose(); // el Dashboard ya sigue abierto detrás, solo cerramos esta ventana
            }
        });

        return volver;
    }

    private JComponent crearTitulo() {
        // El nombre del evento ya viene con comillas propias (ej: Cumpleaños "Mario Vargas").
        // Antes se le agregaban comillas “ ” extra por fuera, duplicando el efecto.
        // Ahora solo se inserta un guion antes de la parte entre comillas, sin comillas de más.
        String nombre = event.getName();
        int idx = nombre.indexOf('"');
        String textoTitulo = (idx > 0)
                ? nombre.substring(0, idx).trim() + " - " + nombre.substring(idx)
                : nombre;

        JLabel titulo = new JLabel("<html><div style='width:480px'>" + textoTitulo + "</div></html>");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 34));
        titulo.setForeground(CYAN_TITULO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        return titulo;
    }

    private JComponent crearTablaColaboradores() {
        JPanel tabla = new JPanel(new GridBagLayout());
        tabla.setOpaque(false);
        tabla.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabla.setMaximumSize(new Dimension(520, 220));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;

        String[] encabezados = {"Colaborador", "Invitó", "Asistieron"};
        for (int col = 0; col < 3; col++) {
            gbc.gridx = col;
            gbc.gridy = 0;
            tabla.add(crearCelda(encabezados[col], true, AZUL_ENCABEZADO), gbc);
        }

        int fila = 1;
        for (Object[] datos : colaboradores) {
            gbc.gridy = fila;
            gbc.gridx = 0;
            tabla.add(crearCelda(String.valueOf(datos[0]), false, Color.WHITE), gbc);
            gbc.gridx = 1;
            tabla.add(crearCelda(String.valueOf(datos[1]), false, Color.WHITE), gbc);
            gbc.gridx = 2;
            tabla.add(crearCelda(String.valueOf(datos[2]), false, Color.WHITE), gbc);
            fila++;
        }

        gbc.gridy = fila;
        gbc.gridx = 0;
        tabla.add(crearCelda("Total", true, Color.WHITE), gbc);
        gbc.gridx = 1;
        tabla.add(crearCelda(String.valueOf(totalInvito), true, Color.WHITE), gbc);
        gbc.gridx = 2;
        tabla.add(crearCelda(String.valueOf(totalAsistieron), true, Color.WHITE), gbc);

        return tabla;
    }

    private JLabel crearCelda(String texto, boolean negrita, Color fondo) {
        JLabel celda = new JLabel(texto, SwingConstants.CENTER);
        celda.setFont(new Font("SansSerif", negrita ? Font.BOLD : Font.PLAIN, 15));
        celda.setForeground(TEXTO_OSCURO);
        celda.setOpaque(true);
        celda.setBackground(fondo);
        celda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, BORDE_TABLA),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        celda.setPreferredSize(new Dimension(150, 44));
        return celda;
    }

    private JComponent crearBotonesYAforo() {
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setOpaque(false);
        contenedor.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Sin esto, BoxLayout le da a este contenedor todo el alto sobrante
        // de la columna, empujando todo lo demás hacia abajo.
        contenedor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        JPanel filaSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        filaSuperior.setOpaque(false);
        filaSuperior.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaSuperior.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        OutlineButton btnColaborador = new OutlineButton("Colaborador");
        OutlineButton btnStaff = new OutlineButton("STAFF");
        btnColaborador.setPreferredSize(new Dimension(200, 42));
        btnStaff.setPreferredSize(new Dimension(200, 42));
        filaSuperior.add(btnColaborador);
        filaSuperior.add(btnStaff);

        JPanel filaInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        filaInferior.setOpaque(false);
        filaInferior.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaInferior.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        OutlineButton btnInvitados = new OutlineButton("Invitados");
        btnInvitados.setPreferredSize(new Dimension(200, 42));

        // El total de asistencias ya sale de los datos de la tabla (totalAsistieron),
        // así que ya no está hardcodeado ni desincronizado de la tabla de arriba.
        // TODO: Cristian - cuando "totalAsistieron" venga de datos reales, este
        // número quedará automáticamente correcto también, sin tocar nada acá.
        JLabel aforo = new JLabel("Aforo: " + totalAsistieron + "/" + event.getCapacity());
        aforo.setFont(new Font("SansSerif", Font.PLAIN, 18));
        aforo.setForeground(TEXTO_OSCURO);

        filaInferior.add(btnInvitados);
        filaInferior.add(aforo);

        // Los 3 botones ya funcionan (abren un mensaje). La lógica real
        // (abrir el formulario de agregar colaborador/staff/invitado) se
        // conecta después - por ahora solo confirman que responden al click.
        btnColaborador.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Función en desarrollo: agregar colaborador."));
        btnStaff.addActionListener(e -> {
            AddStaffView staffView = new AddStaffView(event);
            staffView.setVisible(true);
        });
        btnInvitados.addActionListener(e -> {
            // Pasamos 'this' porque EventDetailView es un JFrame (una ventana válida)
            SendInvitationView modalInvitacion = new SendInvitationView(this);
            SendInvitationController controladorInvitacion = new SendInvitationController(modalInvitacion);
            controladorInvitacion.mostrar();
        });

        contenedor.add(filaSuperior);
        contenedor.add(Box.createRigidArea(new Dimension(0, 12)));
        contenedor.add(filaInferior);
        return contenedor;
    }

    // ==================== COLUMNA DERECHA ====================

    private JPanel crearColumnaDerecha() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        panel.add(crearEncabezadoIndicadores());
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        panel.add(crearGrupoIndicadores("Indicadores de Asistencia y Flujo",
                new String[]{"TCA (Tasa de conversión)", "Hora pico de afluencia", "Velocidad de entrada"}));
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        panel.add(crearGrupoIndicadores("Indicadores de Perfil (Segmentación)",
                new String[]{"Ratio de diversidad", "Cantidad de mujeres por hombres"}));
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        panel.add(crearGrupoIndicadores("Indicadores de Fidelización e Inferenciales",
                new String[]{"Índice de Retorno", "Probabilidad de Asistencia Tardía"}));
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        panel.add(crearUbicacion());

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JComponent crearEncabezadoIndicadores() {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel titulo = new JLabel("Indicadores Estadisticos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(TEXTO_OSCURO);
        fila.add(titulo, BorderLayout.WEST);

        JLabel badge = new JLabel(FORMATO_FECHA_HORA.format(event.getStartDate()), SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AZUL_BADGE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setOpaque(false);
        badge.setForeground(Color.WHITE);
        badge.setFont(new Font("SansSerif", Font.PLAIN, 15));
        badge.setPreferredSize(new Dimension(190, 34));
        fila.add(badge, BorderLayout.EAST);

        return fila;
    }

    private JComponent crearGrupoIndicadores(String tituloGrupo, String[] etiquetas) {
        JPanel grupo = new JPanel();
        grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
        grupo.setOpaque(false);
        grupo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titulo = new JLabel(tituloGrupo);
        titulo.setFont(new Font("SansSerif", Font.PLAIN, 18));
        titulo.setForeground(TEXTO_AZUL);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        grupo.add(titulo);

        for (String etiqueta : etiquetas) {
            IndicatorRow fila = new IndicatorRow(etiqueta);
            fila.setAlignmentX(Component.LEFT_ALIGNMENT);
            // TODO: Cristian - llamar fila.setValores(...) cuando exista el
            // cálculo real de este indicador. Por ahora se queda vacío (como en el mockup).
            grupo.add(fila);
        }

        return grupo;
    }

    private JComponent crearUbicacion() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel pin = new JLabel("\uD83D\uDCCD"); // 📍
        pin.setFont(new Font("SansSerif", Font.PLAIN, 20));
        pin.setForeground(AZUL_BADGE);

        JLabel direccion = new JLabel("Salón \u201C" + event.getAddress() + "\u201D");
        direccion.setFont(new Font("SansSerif", Font.PLAIN, 18));
        direccion.setForeground(TEXTO_OSCURO);

        panel.add(pin);
        panel.add(direccion);
        return panel;
    }
}