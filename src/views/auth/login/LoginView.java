package views.auth.login;

import modules.user.service.UserService;
import views.auth.register.RegisterView;
import views.components.CustomButton;
import views.components.CustomTextField;
import views.components.NeonLabel;
import views.dashboard.DashboardView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginView extends JFrame {

    private final Color FONDO_OSCURO  = new Color(0x2B, 0x2A, 0x2E);
    private final Color CELESTE_FONDO = new Color(0xAD, 0xE0, 0xF7);
    private final Color NEON_CYAN     = new Color(0x5F, 0xF7, 0xF0);
    private final Color TEXTO_OSCURO  = new Color(0x2A, 0x2A, 0x2A);
    private final Color AZUL_BOTON    = new Color(0x3E, 0x6B, 0x84);

    private final LoginController controller;
    private final UserService userService; // por si RegisterView también lo necesita

    public LoginView(UserService userService) {
        this.userService = userService;
        this.controller = new LoginController(userService);

        setTitle("Login - Noctra");
        setSize(1500, 711);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridLayout(1, 2));

        add(crearPanelIzquierdo());
        add(crearPanelDerecho());
    }

    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel(null);
        panel.setBackground(FONDO_OSCURO);

        JLabel bienvenido = new JLabel("BIENVENIDO A");
        bienvenido.setFont(new Font("SansSerif", Font.BOLD, 26));
        bienvenido.setForeground(Color.WHITE);
        bienvenido.setBounds(70, 270, 400, 40);
        panel.add(bienvenido);

        NeonLabel noctra = new NeonLabel("NOCTRA", NEON_CYAN, 90);
        noctra.setBounds(30, 300, 660, 130);
        panel.add(noctra);

        return panel;
    }

    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel(null);
        panel.setBackground(CELESTE_FONDO);

        JLabel login = new JLabel("Login");
        login.setFont(new Font("SansSerif", Font.PLAIN, 40));
        login.setForeground(TEXTO_OSCURO);
        login.setBounds(280, 130, 200, 60);
        panel.add(login);

        JLabel idLabel = new JLabel("Nombre Usuario");
        idLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        idLabel.setForeground(TEXTO_OSCURO);
        idLabel.setBounds(80, 260, 200, 25);
        panel.add(idLabel);

        CustomTextField idField = new CustomTextField();
        idField.setBounds(80, 290, 460, 45);
        panel.add(idField);

        JLabel passLabel = new JLabel("Contraseña");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        passLabel.setForeground(TEXTO_OSCURO);
        passLabel.setBounds(80, 370, 150, 25);
        panel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        CustomTextField.estilizar(passField);
        passField.setBounds(80, 400, 460, 45);
        panel.add(passField);

        CustomButton iniciarBtn = new CustomButton("Iniciar", AZUL_BOTON);
        iniciarBtn.setBounds(200, 490, 220, 45);
        panel.add(iniciarBtn);

        iniciarBtn.addActionListener(e -> {
            String id = idField.getText();
            String password = new String(passField.getPassword());
            boolean exito = controller.iniciarSesion(id, password);
            if (!exito) {
                JOptionPane.showMessageDialog(
                        this,
                        "ID o contraseña incorrectos.",
                        "Error de login",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Login exitoso -> abrimos el Dashboard y cerramos el login.
            // TODO: reemplazar "id" por el nombre real del usuario cuando
            // UserService/User expongan un campo de nombre (por ahora se
            // muestra el mismo ID/nickname con el que se logueó).
            // TODO: reemplazar el 0 por la cantidad real de notificaciones
            // cuando exista ese servicio.
            DashboardView dashboardView = new DashboardView(id, 0);
            dashboardView.setVisible(true);
            dispose();
        });

        JLabel registroLabel = crearTextoRegistro();
        registroLabel.setBounds(200, 545, 350, 25);
        panel.add(registroLabel);

        return panel;
    }

    private JLabel crearTextoRegistro() {
        JLabel label = new JLabel(
                "<html><span style='color:#2A2A2A;'>¿No tienes una cuenta? </span>"
                        + "<span style='color:#1A5FB4;'>Regístrate aquí</span></html>"
        );
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // OJO: si el constructor de RegisterView no recibe UserService,
                // cambia esta línea a "new RegisterView()"
                RegisterView registerView = new RegisterView(userService);
                registerView.setVisible(true);
                dispose();
            }
        });

        return label;
    }
}