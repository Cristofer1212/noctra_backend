package views.dashboard;

import views.components.CustomTextField;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NuevoColaboradorView extends JDialog {

    private static final Color CELESTE_FONDO = new Color(0x74, 0xC7, 0xF0);
    private static final Color TEXTO_OSCURO  = new Color(0x2A, 0x2A, 0x2A);
    private static final Color AZUL_BOTON    = new Color(0x3E, 0x6B, 0x84);
    private static final Color BLANCO        = Color.WHITE;

    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtDni;
    private JButton btnRegistrar;
    private JLabel btnVolver;

    public NuevoColaboradorView(Frame padre) {
        super(padre, "Registrar Colaborador", true);
        setSize(1500, 711); // Misma proporción que tu pantalla de NuevoEvento
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(padre);
        setResizable(false);
        setLayout(new GridLayout(1, 2));

        add(crearPanelIzquierdo());
        add(crearPanelDerecho());
    }

    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel(null);
        panel.setBackground(BLANCO);

        // Botón de retroceder "← Volver"
        btnVolver = new JLabel("←  Volver");
        btnVolver.setFont(new Font("SansSerif", Font.PLAIN, 15));
        btnVolver.setForeground(new Color(0x7A, 0x7A, 0x7A));
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.setBounds(30, 20, 150, 25);
        panel.add(btnVolver);

        // Título de la vista
        JLabel titulo = new JLabel("Registrar Colaborador");
        titulo.setFont(new Font("SansSerif", Font.PLAIN, 40));
        titulo.setForeground(TEXTO_OSCURO);
        titulo.setBounds(60, 45, 500, 55);
        panel.add(titulo);

        // Sección Nombres
        JLabel nombreLabel = new JLabel("Nombres");
        nombreLabel.setFont(new Font("SansSerif", Font.PLAIN, 17));
        nombreLabel.setForeground(TEXTO_OSCURO);
        nombreLabel.setBounds(60, 125, 300, 22);
        panel.add(nombreLabel);

        txtNombres = new JTextField();
        CustomTextField.estilizarConBorde(txtNombres);
        txtNombres.setBounds(60, 155, 600, 42);
        panel.add(txtNombres);

        // Sección Apellidos
        JLabel apellidosLabel = new JLabel("Apellidos");
        apellidosLabel.setFont(new Font("SansSerif", Font.PLAIN, 17));
        apellidosLabel.setForeground(TEXTO_OSCURO);
        apellidosLabel.setBounds(60, 220, 300, 22);
        panel.add(apellidosLabel);

        txtApellidos = new JTextField();
        CustomTextField.estilizarConBorde(txtApellidos);
        txtApellidos.setBounds(60, 250, 600, 42);
        panel.add(txtApellidos);

        // Sección DNI
        JLabel dniLabel = new JLabel("DNI");
        dniLabel.setFont(new Font("SansSerif", Font.PLAIN, 17));
        dniLabel.setForeground(TEXTO_OSCURO);
        dniLabel.setBounds(60, 315, 300, 22);
        panel.add(dniLabel);

        txtDni = new JTextField();
        CustomTextField.estilizarConBorde(txtDni);
        txtDni.setBounds(60, 345, 600, 42);
        panel.add(txtDni);

        // Botón Registrar
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btnRegistrar.setForeground(BLANCO);
        btnRegistrar.setBackground(AZUL_BOTON);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setBorder(BorderFactory.createEmptyBorder());
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrar.setBounds(250, 440, 220, 50);
        panel.add(btnRegistrar);

        return panel;
    }

    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CELESTE_FONDO);

        JLabel icono = new JLabel();
        java.net.URL url = getClass().getResource("/images/ColaboradorAgregar_icon.png");
        if (url != null) {
            ImageIcon original = new ImageIcon(url);
            // Escalado idéntico a tu vista de eventos para mantener proporciones óptimas
            Image escalado = original.getImage().getScaledInstance(290, -1, Image.SCALE_SMOOTH);
            icono.setIcon(new ImageIcon(escalado));
        } else {
            System.err.println("[NuevoColaboradorView] No se encontró /images/ColaboradorAgregar_icon.png");
            icono.setText("(Falta imagen)");
        }
        panel.add(icono);

        return panel;
    }

    // Getters para el controlador e integraciones de Cris
    public JButton getBtnRegistrar() { return btnRegistrar; }
    public JLabel getBtnVolver() { return btnVolver; }
    public String getTxtNombres() { return txtNombres.getText().trim(); }
    public String getTxtApellidos() { return txtApellidos.getText().trim(); }
    public String getTxtDni() { return txtDni.getText().trim(); }
}