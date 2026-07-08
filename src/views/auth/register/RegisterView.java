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

    // Variables para acceder a los datos desde el Controller
    private CustomTextField txtNickname, txtNombres, txtApellidos, txtDni, txtPhone, txtPin;

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

        // Título principal
        JLabel titulo = new JLabel("Registrarse");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 40));
        titulo.setForeground(TEXTO_OSCURO);
        titulo.setBounds(650, 50, 300, 60);
        panel.add(titulo);

        // Configuración de posiciones para los campos
        int x = 550; // Posición X centrada
        int y = 130; // Posición inicial Y
        int gap = 75; // Espacio entre campos

        txtNickname  = crearCampo(panel, "NickName", x, y);
        txtNombres   = crearCampo(panel, "Nombres", x, y + gap);
        txtApellidos = crearCampo(panel, "Apellidos", x, y + gap * 2);
        txtDni       = crearCampo(panel, "DNI", x, y + gap * 3);
        txtPhone     = crearCampo(panel, "Phone", x, y + gap * 4);
        txtPin       = crearCampo(panel, "Pin", x, y + gap * 5);

        // Botón Registrarse
        CustomButton btnRegistrar = new CustomButton("Registrarse", AZUL_BOTON);
        btnRegistrar.setBounds(x + 100, y + gap * 6, 200, 45);
        panel.add(btnRegistrar);

        // Botón Volver (lo mantuve según tu diseño original)
        CustomButton volverBtn = new CustomButton("Volver al login", AZUL_BOTON);
        volverBtn.setBounds(50, 50, 200, 45);
        panel.add(volverBtn);

        volverBtn.addActionListener(e -> dispose());
    }

    /**
     * Método auxiliar para crear etiquetas y campos de texto de forma ordenada
     */
    private CustomTextField crearCampo(JPanel panel, String labelText, int x, int y) {
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setForeground(TEXTO_OSCURO);
        lbl.setBounds(x, y - 25, 200, 20);
        panel.add(lbl);

        CustomTextField field = new CustomTextField();
        field.setBounds(x, y, 400, 40);
        panel.add(field);
        return field;
    }
}