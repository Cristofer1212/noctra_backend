package views.dashboard;

import views.components.CustomButton;
import views.components.CustomTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NuevoEventoView extends JFrame {

    private static final Color CELESTE_FONDO = new Color(0x74, 0xC7, 0xF0);
    private static final Color TEXTO_OSCURO  = new Color(0x2A, 0x2A, 0x2A);
    private static final Color AZUL_BOTON    = new Color(0x3E, 0x6B, 0x84);
    private static final Color GRIS_PLACEHOLDER = new Color(0x9A, 0x9A, 0x9A);

    private static final String PLACEHOLDER_FECHA = "dd/mm/aaaa";
    private static final String PLACEHOLDER_HORA = "HH:mm";

    private final NuevoEventoController controller = new NuevoEventoController();

    public NuevoEventoView() {
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

        JLabel nombreLabel = crearEtiqueta("Nombre del Evento");
        nombreLabel.setBounds(60, 118, 300, 22);
        panel.add(nombreLabel);

        JTextField nombreField = new JTextField();
        CustomTextField.estilizarConBorde(nombreField);
        nombreField.setBounds(60, 148, 600, 42);
        panel.add(nombreField);

        JLabel inicioLabel = crearEtiqueta("Fecha y Hora inicial");
        inicioLabel.setBounds(60, 205, 300, 22);
        panel.add(inicioLabel);

        JTextField fechaInicioField = new JTextField();
        CustomTextField.estilizarConBorde(fechaInicioField);
        fechaInicioField.setBounds(60, 235, 280, 42);
        agregarPlaceholder(fechaInicioField, PLACEHOLDER_FECHA);
        panel.add(fechaInicioField);

        JTextField horaInicioField = new JTextField();
        CustomTextField.estilizarConBorde(horaInicioField);
        horaInicioField.setBounds(360, 235, 300, 42);
        agregarPlaceholder(horaInicioField, PLACEHOLDER_HORA);
        panel.add(horaInicioField);

        JLabel finLabel = crearEtiqueta("Fecha y Hora final");
        finLabel.setBounds(60, 292, 300, 22);
        panel.add(finLabel);

        JTextField fechaFinField = new JTextField();
        CustomTextField.estilizarConBorde(fechaFinField);
        fechaFinField.setBounds(60, 322, 280, 42);
        agregarPlaceholder(fechaFinField, PLACEHOLDER_FECHA);
        panel.add(fechaFinField);

        JTextField horaFinField = new JTextField();
        CustomTextField.estilizarConBorde(horaFinField);
        horaFinField.setBounds(360, 322, 300, 42);
        agregarPlaceholder(horaFinField, PLACEHOLDER_HORA);
        panel.add(horaFinField);

        JLabel direccionLabel = crearEtiqueta("Dirección");
        direccionLabel.setBounds(60, 379, 300, 22);
        panel.add(direccionLabel);

        JTextField direccionField = new JTextField();
        CustomTextField.estilizarConBorde(direccionField);
        direccionField.setBounds(60, 409, 600, 42);
        panel.add(direccionField);

        JLabel aforoLabel = crearEtiqueta("Aforo");
        aforoLabel.setBounds(60, 466, 300, 22);
        panel.add(aforoLabel);

        JTextField aforoField = new JTextField();
        CustomTextField.estilizarConBorde(aforoField);
        aforoField.setBounds(60, 496, 600, 42);
        panel.add(aforoField);

        CustomButton añadirBtn = new CustomButton("Añadir", AZUL_BOTON);
        añadirBtn.setBounds(280, 570, 220, 50);
        panel.add(añadirBtn);

        añadirBtn.addActionListener(e -> {
            String nombre = nombreField.getText().trim();
            String fechaInicio = valorReal(fechaInicioField, PLACEHOLDER_FECHA);
            String horaInicio = valorReal(horaInicioField, PLACEHOLDER_HORA);
            String fechaFin = valorReal(fechaFinField, PLACEHOLDER_FECHA);
            String horaFin = valorReal(horaFinField, PLACEHOLDER_HORA);
            String direccion = direccionField.getText().trim();
            String aforo = aforoField.getText().trim();

            String error = controller.crearEvento(nombre, fechaInicio, horaInicio, fechaFin, horaFin, direccion, aforo);
            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Evento validado correctamente.\n(Falta que Cristian conecte el guardado real en la base de datos.)",
                        "Listo", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // el Dashboard sigue abierto detrás
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
            public void mouseEntered(MouseEvent e) {
                volver.setForeground(TEXTO_OSCURO);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                volver.setForeground(new Color(0x7A, 0x7A, 0x7A));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                dispose(); // el Dashboard ya sigue abierto detrás
            }
        });

        return volver;
    }

    // ---- Placeholder simple para los campos de fecha/hora (sin librerías externas) ----

    private void agregarPlaceholder(JTextField field, String placeholder) {
        field.setForeground(GRIS_PLACEHOLDER);
        field.setText(placeholder);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXTO_OSCURO);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isBlank()) {
                    field.setForeground(GRIS_PLACEHOLDER);
                    field.setText(placeholder);
                }
            }
        });
    }

    private String valorReal(JTextField field, String placeholder) {
        String texto = field.getText().trim();
        return texto.equals(placeholder) ? "" : texto;
    }
}