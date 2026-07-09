package views.dashboard;

import views.components.CollaboratorRowPanel;
import views.components.OutlineButton;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class ColaboradoresPanel extends JPanel {

    private static final Color AZUL_FONDO = new Color(0xEA, 0xF3, 0xFB);
    private static final Color AZUL_ENCABEZADO = new Color(0xD3, 0xE7, 0xF7);
    private static final Color TEXTO_AZUL = new Color(0x1C, 0x6F, 0xA0);

    public ColaboradoresPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(AZUL_FONDO);
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        add(crearEncabezado());
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(crearListaColaboradores());
    }

    private JComponent crearEncabezado() {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        OutlineButton btnNuevoColaborador = new OutlineButton("Colaborador");
        btnNuevoColaborador.setPreferredSize(new Dimension(220, 45));

        // TODO: Cristian - reemplazar el JOptionPane por el formulario real de
        // registro de colaborador cuando lo armemos (probablemente similar a
        // AddStaffView, pero guardando en la tabla de colaboradores).
        btnNuevoColaborador.addActionListener(e -> {
            // Consigue la ventana principal para centrar el modal correctamente
            Frame framePadre = (Frame) SwingUtilities.getWindowAncestor(this);

            // Instancia la nueva vista en pantalla dividida y su controlador
            NuevoColaboradorView vistaNuevo = new NuevoColaboradorView(framePadre);
            NuevoColaboradorController controlador = new NuevoColaboradorController(vistaNuevo);

            // Muestra la ventana emergente
            vistaNuevo.setVisible(true);
        });

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        izquierda.setOpaque(false);
        izquierda.add(btnNuevoColaborador);
        fila.add(izquierda, BorderLayout.WEST);

        JLabel tituloColumna = new JLabel(
                "<html><div style='text-align:center;'>Éxito de los eventos<br>colaborados</div></html>",
                SwingConstants.CENTER);
        tituloColumna.setFont(new Font("SansSerif", Font.BOLD, 16));
        tituloColumna.setForeground(TEXTO_AZUL);
        tituloColumna.setOpaque(true);
        tituloColumna.setBackground(AZUL_ENCABEZADO);
        tituloColumna.setPreferredSize(new Dimension(230, 55));
        tituloColumna.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        fila.add(tituloColumna, BorderLayout.EAST);

        return fila;
    }

    private JComponent crearListaColaboradores() {
        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setOpaque(false);
        lista.setAlignmentX(Component.LEFT_ALIGNMENT);

        // TODO: Cristian - reemplazar esta lista de ejemplo por la consulta real,
        // algo como: collaboratorService.findAllForCurrentOrganizer(userId)
        // Cada colaborador necesita: nombre, cantidad de eventos en los que
        // colaboró, y su % de éxito promedio en esos eventos (ese cálculo
        // probablemente ya exista o se derive de los indicadores de EventDetailView).
        List<Object[]> colaboradores = obtenerColaboradoresDeEjemplo();

        for (int i = 0; i < colaboradores.size(); i++) {
            Object[] datos = colaboradores.get(i);
            String nombre = (String) datos[0];
            int cantidadColaboraciones = (int) datos[1];
            int porcentajeExito = (int) datos[2];
            boolean mostrarLinea = i < colaboradores.size() - 1;

            CollaboratorRowPanel fila = new CollaboratorRowPanel(nombre, cantidadColaboraciones, porcentajeExito, mostrarLinea);
            fila.setAlignmentX(Component.LEFT_ALIGNMENT);
            lista.add(fila);
        }

        return lista;
    }

    private List<Object[]> obtenerColaboradoresDeEjemplo() {
        List<Object[]> lista = new ArrayList<>();
        lista.add(new Object[]{"William Shakespeare", 14, 70});
        lista.add(new Object[]{"Emilia Laurent", 9, 45});
        lista.add(new Object[]{"Alessia Moretti", 6, 29});
        lista.add(new Object[]{"Dante Belmot", 18, 82});
        return lista;
    }
}