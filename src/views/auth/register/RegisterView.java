package views.auth.register;

import modules.user.service.UserService;
import views.components.CustomButton;
import views.components.CustomTextField;

import javax.swing.*;
import java.awt.*;

public class RegisterView extends JFrame {

    private final Color CELESTE_FONDO = new Color(0xAD, 0xE0, 0xF7);
    private final Color TEXTO_OSCURO  = new Color(0x2A, 0x2A, 0x2A);
    private final Color AZUL_BOTON    = new Color(0x3E, 0x6B, 0x84);

    private final UserService userService;

    public RegisterView(UserService userService) {
        this.userService = userService;

        setTitle("Registro - Noctra");
        setSize(1500, 711);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(CELESTE_FONDO);
        setContentPane(panel);

        JLabel titulo = new JLabel("Registro");
        titulo.setFont(new Font("SansSerif", Font.PLAIN, 40));
        titulo.setForeground(TEXTO_OSCURO);
        titulo.setBounds(280, 130, 300, 60);
        panel.add(titulo);

        JLabel nota = new JLabel("(Pantalla de registro en construcción — Cristian completa esto)");
        nota.setFont(new Font("SansSerif", Font.ITALIC, 14));
        nota.setForeground(TEXTO_OSCURO);
        nota.setBounds(80, 200, 500, 25);
        panel.add(nota);

        CustomButton volverBtn = new CustomButton("Volver al login", AZUL_BOTON);
        volverBtn.setBounds(80, 300, 220, 45);
        panel.add(volverBtn);

        volverBtn.addActionListener(e -> dispose());
    }
}