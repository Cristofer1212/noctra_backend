package views.dashboard;

import views.components.CustomButton;
import views.components.CustomTextField;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SendInvitationView extends JDialog {
    private static final Color AZUL_BOTON = new Color(0x3E, 0x6B, 0x84);
    private final String PLACEHOLDER = "+5193328924";

    // Componentes de la vista
    public JComboBox<String> cbGenero;
    public CustomTextField txtCelular;
    public CustomButton btnEnviar;

    public SendInvitationView(Window owner) {
        super(owner, "Invitando", ModalityType.APPLICATION_MODAL);

        setSize(320, 280);
        setLocationRelativeTo(owner);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 35, 15, 35));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;

        // --- 1. Label Género ---
        JLabel lblGenero = new JLabel("Género");
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 0);
        mainPanel.add(lblGenero, gbc);

        // --- 2. ComboBox Género ---
        cbGenero = new JComboBox<>(new String[]{"Seleccionar...", "Masculino", "Femenino", "Otro"});
        cbGenero.setPreferredSize(new Dimension(0, 35));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 15, 0);
        mainPanel.add(cbGenero, gbc);

        // --- 3. Label Celular ---
        JLabel lblCelular = new JLabel("Nro° Celular de Invitado");
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        mainPanel.add(lblCelular, gbc);

        // --- 4. TextField Celular con Placeholder ---
        txtCelular = new CustomTextField();
        txtCelular.setPreferredSize(new Dimension(0, 35));
        txtCelular.setText(PLACEHOLDER);
        txtCelular.setForeground(Color.GRAY);

        txtCelular.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtCelular.getText().equals(PLACEHOLDER)) {
                    txtCelular.setText("");
                    txtCelular.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtCelular.getText().isEmpty()) {
                    txtCelular.setText(PLACEHOLDER);
                    txtCelular.setForeground(Color.GRAY);
                }
            }
        });

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 25, 0);
        mainPanel.add(txtCelular, gbc);

        // --- 5. Botón Enviar ---
        btnEnviar = new CustomButton("Enviar Invitación", AZUL_BOTON);
        btnEnviar.setPreferredSize(new Dimension(0, 40));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(btnEnviar, gbc);

        gbc.gridy = 5;
        gbc.weighty = 1.0;
        JPanel resorte = new JPanel();
        resorte.setOpaque(false);
        mainPanel.add(resorte, gbc);

        add(mainPanel);
    }

    // Método auxiliar para que el controlador valide el texto
    public String getCelularText() {
        String texto = txtCelular.getText();
        return texto.equals(PLACEHOLDER) ? "" : texto;
    }
}