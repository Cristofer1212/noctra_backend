package views.dashboard;

import views.components.CustomButton;
import views.components.CustomTextField;
import com.toedter.calendar.JDateChooser; // Importación de la librería JCalendar

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

public class NuevoEventoView extends JFrame {

    private static final Color CELESTE_FONDO = new Color(0x74, 0xC7, 0xF0);
    private static final Color TEXTO_OSCURO  = new Color(0x2A, 0x2A, 0x2A);
    private static final Color AZUL_BOTON    = new Color(0x3E, 0x6B, 0x84);

    private final NuevoEventoController controller = new NuevoEventoController();
    private final DashboardView dashboard; // Referencia para actualizar la vista principal

    // --- CONSTRUCTOR MODIFICADO PARA RECIBIR DASHBOARD ---
    public NuevoEventoView(DashboardView dashboard) {
        this.dashboard = dashboard;
        setTitle("Noctra - Nuevo Evento");
        setSize(1500, 711);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridLayout(1, 2));

        add(crearPanelIzquierdo());
        add(crearPanelDerecho());
    }

    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CELESTE_FONDO);

        JLabel icono = new JLabel();
        java.net.URL url = getClass().getResource("/images/calendario_newEvento.png");
        if (url != null) {
            ImageIcon original = new ImageIcon(url);
            Image escalado = original.getImage().getScaledInstance(290, -1, Image.SCALE_SMOOTH);
            icono.setIcon(new ImageIcon(escalado));
        } else {
            System.err.println("[NuevoEventoView] No se encontró /images/calendario_newEvento.png");
            icono.setText("(falta imagen)");
        }
        panel.add(icono);

        return panel;
    }

    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel volver = crearBotonVolver();
        volver.setBounds(30, 20, 150, 25);
        panel.add(volver);

        JLabel titulo = new JLabel("Nuevo Evento");
        titulo.setFont(new Font("SansSerif", Font.PLAIN, 40));
        titulo.setForeground(TEXTO_OSCURO);
        titulo.setBounds(60, 45, 400, 55);
        panel.add(titulo);

        // --- NOMBRE ---
        JLabel nombreLabel = crearEtiqueta("Nombre del Evento");
        nombreLabel.setBounds(60, 118, 300, 22);
        panel.add(nombreLabel);

        JTextField nombreField = new JTextField();
        CustomTextField.estilizarConBorde(nombreField);
        nombreField.setBounds(60, 148, 600, 42);
        panel.add(nombreField);

        // --- FECHA Y HORA INICIO ---
        JLabel inicioLabel = crearEtiqueta("Fecha y Hora inicial");
        inicioLabel.setBounds(60, 205, 300, 22);
        panel.add(inicioLabel);

        JDateChooser fechaInicioChooser = new JDateChooser();
        fechaInicioChooser.setDateFormatString("dd/MM/yyyy");
        fechaInicioChooser.setBounds(60, 235, 280, 42);
        panel.add(fechaInicioChooser);

// Configuración para iniciar en 00:00
        java.util.Calendar calInicio = java.util.Calendar.getInstance();
        calInicio.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calInicio.set(java.util.Calendar.MINUTE, 0);
        calInicio.set(java.util.Calendar.SECOND, 0);

        SpinnerDateModel timeModelInicio = new SpinnerDateModel(calInicio.getTime(), null, null, java.util.Calendar.HOUR_OF_DAY);
        JSpinner horaInicioSpinner = new JSpinner(timeModelInicio);
        JSpinner.DateEditor editorHoraInicio = new JSpinner.DateEditor(horaInicioSpinner, "HH:mm");
        horaInicioSpinner.setEditor(editorHoraInicio);
        horaInicioSpinner.setBounds(360, 235, 300, 42);
        panel.add(horaInicioSpinner);

// --- FECHA Y HORA FIN ---
        JLabel finLabel = crearEtiqueta("Fecha y Hora final");
        finLabel.setBounds(60, 292, 300, 22);
        panel.add(finLabel);

        JDateChooser fechaFinChooser = new JDateChooser();
        fechaFinChooser.setDateFormatString("dd/MM/yyyy");
        fechaFinChooser.setBounds(60, 322, 280, 42);
        panel.add(fechaFinChooser);

// Configuración para iniciar en 00:00
        java.util.Calendar calFin = java.util.Calendar.getInstance();
        calFin.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calFin.set(java.util.Calendar.MINUTE, 0);
        calFin.set(java.util.Calendar.SECOND, 0);

        SpinnerDateModel timeModelFin = new SpinnerDateModel(calFin.getTime(), null, null, java.util.Calendar.HOUR_OF_DAY);
        JSpinner horaFinSpinner = new JSpinner(timeModelFin);
        JSpinner.DateEditor editorHoraFin = new JSpinner.DateEditor(horaFinSpinner, "HH:mm");
        horaFinSpinner.setEditor(editorHoraFin);
        horaFinSpinner.setBounds(360, 322, 300, 42);
        panel.add(horaFinSpinner);

        // --- DIRECCIÓN ---
        JLabel direccionLabel = crearEtiqueta("Dirección");
        direccionLabel.setBounds(60, 379, 300, 22);
        panel.add(direccionLabel);

        JTextField direccionField = new JTextField();
        CustomTextField.estilizarConBorde(direccionField);
        direccionField.setBounds(60, 409, 600, 42);
        panel.add(direccionField);

        // --- BOTÓN AÑADIR ---
        CustomButton añadirBtn = new CustomButton("Añadir", AZUL_BOTON);
        añadirBtn.setBounds(280, 490, 220, 50);
        panel.add(añadirBtn);

        añadirBtn.addActionListener(e -> {
            if (fechaInicioChooser.getDate() == null || fechaFinChooser.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona las fechas del evento usando el calendario.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

            String nombre = nombreField.getText().trim();
            String fechaInicio = sdfDate.format(fechaInicioChooser.getDate());
            String horaInicio = sdfTime.format(horaInicioSpinner.getValue());
            String fechaFin = sdfDate.format(fechaFinChooser.getDate());
            String horaFin = sdfTime.format(horaFinSpinner.getValue());

            String direccion = direccionField.getText().trim();

            String error = controller.crearEvento(nombre, fechaInicio, horaInicio, fechaFin, horaFin, direccion);

            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Evento creado exitosamente.", "Listo", JOptionPane.INFORMATION_MESSAGE);

                // --- AQUÍ LLAMAS A LA ACTUALIZACIÓN DEL DASHBOARD ---
                dashboard.actualizarEventos();

                dispose();
            }
        });
        return panel;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.PLAIN, 17));
        label.setForeground(TEXTO_OSCURO);
        return label;
    }

    private JLabel crearBotonVolver() {
        JLabel volver = new JLabel("\u2190  Volver");
        volver.setFont(new Font("SansSerif", Font.PLAIN, 15));
        volver.setForeground(new Color(0x7A, 0x7A, 0x7A));
        volver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        volver.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { volver.setForeground(TEXTO_OSCURO); }
            @Override
            public void mouseExited(MouseEvent e) { volver.setForeground(new Color(0x7A, 0x7A, 0x7A)); }
            @Override
            public void mouseClicked(MouseEvent e) { dispose(); }
        });
        return volver;
    }
}