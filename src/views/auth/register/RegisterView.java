package views.auth.register;

import modules.user.service.UserService;
import views.auth.login.LoginView;
import views.components.CustomButton;
import views.components.CustomTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterView extends JFrame {

    private final Color CELESTE_FONDO = new Color(0x74, 0xC7, 0xF0);
    private final Color TEXTO_OSCURO  = new Color(0x2A, 0x2A, 0x2A);
    private final Color AZUL_BOTON    = new Color(0x3E, 0x6B, 0x84);

    private final UserService userService;
    private final RegisterController controller;

    public RegisterView(UserService userService) {
        this.userService = userService;
        this.controller = new RegisterController(userService);

        setTitle("Registro - Noctra");
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

        JLabel nicknameLabel = crearEtiqueta("Nickname"); // Nuevo label
        nicknameLabel.setBounds(140, 540, 300, 22);
        panel.add(nicknameLabel);

        JTextField nicknameField = new JTextField(); // Nuevo campo
        CustomTextField.estilizarConBorde(nicknameField);
        nicknameField.setBounds(140, 565, 460, 42);
        panel.add(nicknameField);

        // Botón discreto de Volver al Login
        JLabel volver = crearBotonVolver();
        volver.setBounds(140, 20, 150, 25);
        panel.add(volver);

        JLabel titulo = new JLabel("Registrarse");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 42));
        titulo.setForeground(TEXTO_OSCURO);
        titulo.setBounds(140, 50, 400, 55);
        panel.add(titulo);

        JLabel nombresLabel = crearEtiqueta("Nombres");
        nombresLabel.setBounds(140, 125, 300, 22);
        panel.add(nombresLabel);

        JTextField nombresField = new JTextField();
        CustomTextField.estilizarConBorde(nombresField);
        nombresField.setBounds(140, 150, 460, 42);
        panel.add(nombresField);

        JLabel apellidosLabel = crearEtiqueta("Apellidos");
        apellidosLabel.setBounds(140, 208, 300, 22);
        panel.add(apellidosLabel);

        JTextField apellidosField = new JTextField();
        CustomTextField.estilizarConBorde(apellidosField);
        apellidosField.setBounds(140, 233, 460, 42);
        panel.add(apellidosField);

        JLabel dniLabel = crearEtiqueta("DNI");
        dniLabel.setBounds(140, 291, 300, 22);
        panel.add(dniLabel);

        JTextField dniField = new JTextField();
        CustomTextField.estilizarConBorde(dniField);
        dniField.setBounds(140, 316, 460, 42);
        panel.add(dniField);

        JLabel pinLabel = crearEtiqueta("Crear PIN");
        pinLabel.setBounds(140, 374, 300, 22);
        panel.add(pinLabel);

        JPasswordField pinField = new JPasswordField();
        CustomTextField.estilizarConBorde(pinField);
        pinField.setBounds(140, 399, 460, 42);
        panel.add(pinField);

        JLabel repetirPinLabel = crearEtiqueta("Repetir PIN");
        repetirPinLabel.setBounds(140, 457, 300, 22);
        panel.add(repetirPinLabel);

        JPasswordField repetirPinField = new JPasswordField();
        CustomTextField.estilizarConBorde(repetirPinField);
        repetirPinField.setBounds(140, 482, 460, 42);
        panel.add(repetirPinField);

        CustomButton registrarseBtn = new CustomButton("Registrarse", AZUL_BOTON);
        registrarseBtn.setBounds(250, 620, 240, 50);
        panel.add(registrarseBtn);

        registrarseBtn.addActionListener(e -> {
            String nombres = nombresField.getText().trim();
            String apellidos = apellidosField.getText().trim();
            String dni = dniField.getText().trim();
            String nickname = nicknameField.getText().trim(); // Captura el nuevo campo
            String pin = new String(pinField.getPassword());
            String repetirPin = new String(repetirPinField.getPassword());

            String error = controller.registrar(nombres, apellidos, dni, nickname, pin, repetirPin);
            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Error de registro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "¡Registro exitoso! Ahora inicia sesión.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                LoginView loginView = new LoginView(userService);
                loginView.setVisible(true);
                dispose();
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
                LoginView loginView = new LoginView(userService);
                loginView.setVisible(true);
                dispose();
            }
        });

        return volver;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        label.setForeground(TEXTO_OSCURO);
        return label;
    }

    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CELESTE_FONDO);

        JPanel textoContenedor = new JPanel();
        textoContenedor.setOpaque(false);
        textoContenedor.setLayout(new BoxLayout(textoContenedor, BoxLayout.Y_AXIS));

        JLabel subtitulo = new JLabel("¡FORMA PARTE DE");
        subtitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo2 = new JLabel("NUESTRA COMUNIDAD!");
        subtitulo2.setFont(new Font("SansSerif", Font.BOLD, 26));
        subtitulo2.setForeground(Color.WHITE);
        subtitulo2.setAlignmentX(Component.CENTER_ALIGNMENT);

        textoContenedor.add(subtitulo);
        textoContenedor.add(subtitulo2);
        textoContenedor.add(Box.createVerticalStrut(15));

        JLabel logo = new JLabel("NOCTRA");
        logo.setFont(new Font("Impact", Font.PLAIN, 110));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        textoContenedor.add(logo);

        panel.add(textoContenedor);
        return panel;
    }
}