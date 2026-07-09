package views.dashboard;

import modules.event.model.Event;
import views.components.CustomButton;
import views.components.CustomTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddStaffView extends JFrame {

    private static final Color CELESTE_FONDO = new Color(0x74, 0xC7, 0xF0);
    private static final Color TEXTO_OSCURO  = new Color(0x2A, 0x2A, 0x2A);
    private static final Color AZUL_BOTON    = new Color(0x3E, 0x6B, 0x84);

    private final Event event;
    private final AddStaffController controller;

    public AddStaffView(Event event) {
        this.event = event;
        this.controller = new AddStaffController();

        setTitle("Noctra - Agregar Staff");
        setSize(1500, 711);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridLayout(1, 2));

        add(crearPanelIzquierdo());
        add(crearPanelDerecho());
    }

    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel volver = crearBotonVolver();
        volver.setBounds(30, 25, 150, 25);
        panel.add(volver);

        JLabel dniLabel = new JLabel("DNI del doorman");
        dniLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        dniLabel.setForeground(TEXTO_OSCURO);
        dniLabel.setBounds(140, 150, 300, 25);
        panel.add(dniLabel);

        JTextField dniField = new JTextField();
        CustomTextField.estilizarConBorde(dniField);
        dniField.setBounds(140, 180, 460, 45);
        panel.add(dniField);

        JLabel telLabel = new JLabel("Número telefónico del doorman");
        telLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        telLabel.setForeground(TEXTO_OSCURO);
        telLabel.setBounds(140, 260, 400, 25);
        panel.add(telLabel);

        JTextField telField = new JTextField();
        CustomTextField.estilizarConBorde(telField);
        telField.setBounds(140, 290, 460, 45);
        panel.add(telField);

        CustomButton enviarBtn = new CustomButton("Enviar Link de Invitación", AZUL_BOTON);
        enviarBtn.setBounds(200, 380, 340, 50);
        panel.add(enviarBtn);

        enviarBtn.addActionListener(e -> {
            String dni = dniField.getText();
            String telefono = telField.getText();

            String error = controller.enviarInvitacionStaff(event, dni, telefono);
            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Datos enviados. El link de invitación se generará cuando el backend esté conectado.",
                        "Listo", JOptionPane.INFORMATION_MESSAGE);
                dniField.setText("");
                telField.setText("");
            }
        });

        return panel;
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
                dispose(); // el EventDetailView ya sigue abierto detrás
            }
        });

        return volver;
    }

    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CELESTE_FONDO);

        JLabel icono = new JLabel();
        java.net.URL urlImagen = getClass().getResource("/images/staff_icon.png");
        if (urlImagen != null) {
            ImageIcon iconoOriginal = new ImageIcon(urlImagen);
            Image escalado = iconoOriginal.getImage().getScaledInstance(480, -1, Image.SCALE_SMOOTH);
            icono.setIcon(new ImageIcon(escalado));
        } else {
            // Si esto sale en consola, revisa que resources/images/staff_icon.png
            // esté en la carpeta correcta y que "resources" esté marcada como Resources Root.
            System.err.println("[AddStaffView] No se encontró /images/staff_icon.png");
            icono.setText("(falta imagen)");
        }
        panel.add(icono);

        return panel;
    }
}