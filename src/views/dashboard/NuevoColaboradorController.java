package views.dashboard;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NuevoColaboradorController {

    private final NuevoColaboradorView view;

    // TODO: Cris - Declara aquí tu servicio de base de datos cuando lo integres
    // Ej: private final CollaboratorService collaboratorService;

    public NuevoColaboradorController(NuevoColaboradorView view) {
        this.view = view;
        initListeners();
    }

    private void initListeners() {
        // Acción del botón de retroceder "← Volver"
        view.getBtnVolver().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                view.dispose(); // Cierra de forma segura el modal y regresa a la pantalla principal
            }
        });

        // Acción del botón Registrar
        view.getBtnRegistrar().addActionListener(e -> {
            String nombres = view.getTxtNombres();
            String apellidos = view.getTxtApellidos();
            String dni = view.getTxtDni();

            // Validación básica del Frontend antes de interactuar con el backend
            if (nombres.isEmpty() || apellidos.isEmpty() || dni.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Por favor, complete todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            } else {

                // =========================================================================
                // TODO: Cris - BLOQUE DE BACKEND (ZONA SEGURA PARA MODIFICAR)
                // Aquí tienes las variables limpias listas para persistir en la Base de Datos:
                // - nombres (String)
                // - apellidos (String)
                // - dni (String)
                //
                // Ej:
                // Collaborator nuevo = new Collaborator(nombres, apellidos, dni);
                // collaboratorService.save(nuevo);
                // =========================================================================

                JOptionPane.showMessageDialog(view, "¡Colaborador registrado con éxito!");
                view.dispose(); // Cierra la vista al finalizar la inserción
            }
        });
    }
}