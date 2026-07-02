package views.user;


import modules.user.service.UserService;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    public LoginView(UserService userService) {
        practicanto();

    }

    public void practicanto() {
        setTitle("Login de Usuario");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Panel principal
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new FlowLayout()); // "Flexbox" horizontal

        // 2. Componentes nuevos
        JLabel lblUsuario = new JLabel("Usuario:");
        JTextField txtUsuario = new JTextField(15); // 15 es el tamaño de columnas (ancho)
        JButton btnEntrar = new JButton("Entrar");

        // 3. ¡Ojo aquí! Estamos metiendo piezas en la caja (panel)
        panelCentro.add(lblUsuario);
        panelCentro.add(txtUsuario);
        panelCentro.add(btnEntrar);

        // 4. Conectamos la caja a la ventana
        add(panelCentro, BorderLayout.CENTER);

        setVisible(true);
    }

}