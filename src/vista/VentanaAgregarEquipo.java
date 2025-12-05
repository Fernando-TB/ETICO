package vista;


import controlador.IControladorEquipos;
import controlador.IControladorNavegacion;

import java.awt.*;
import java.util.List;
import javax.swing.*;


public class VentanaAgregarEquipo extends JFrame {

    private final IControladorNavegacion navegador;

    private JTextField txtCorreo;
    private JButton btnAgregar;
    private JButton btnVolver;
    private JButton btnMostrar;
    private JButton btnEliminar;

    private final IControladorEquipos controlador;
    private final String correoJefe;

    public VentanaAgregarEquipo(IControladorEquipos controlador, String correoJefe, String contrasena, String rol, IControladorNavegacion navegador) {

        this.controlador = controlador;
        this.correoJefe = correoJefe;
        this.navegador = navegador;

        Color colorFondo = new Color(41, 49, 51);
        Color colorTexto = Color.WHITE;
        Color colorPanelBotones = new Color(56, 65, 69);
        Color colorBotones = new Color(34, 39, 41);

        setTitle("Agregar Equipo");
        setSize(350, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 0, 0)); // Más espacio vertical

        getContentPane().setBackground(colorFondo);


        // ICONO
        try {
            ImageIcon Logo = new ImageIcon(getClass().getResource("/LOGO.png"));
            setIconImage(Logo.getImage());

        } catch (Exception e) {
            System.err.println("Error al abrir el LOGO");
        }

        JPanel panelCorreo = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
        panelCorreo.setBackground(colorFondo);

        JLabel labelCorreo = new JLabel("Correo del Integrante: ");
        labelCorreo.setForeground(colorTexto);

        panelCorreo.add(labelCorreo);

        txtCorreo = new JTextField(18);
        panelCorreo.add(txtCorreo);



        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.setBackground(colorPanelBotones);


        btnAgregar = new JButton("Agregar");
        btnVolver = new JButton("Volver");
        btnMostrar = new JButton("Mostrar");
        btnEliminar = new JButton("Eliminar");

        JButton[] botones = {btnAgregar, btnVolver, btnMostrar, btnEliminar};
        for (JButton btn : botones) {
            btn.setFont(new Font("Arial", Font.BOLD, 12));
            btn.setPreferredSize(new Dimension(100, 30));
            panelBotones.add(btn);
        }


        add(panelCorreo);
        add(new JPanel() {{
            setBackground(colorFondo);
        }});
        add(panelBotones);


        setVisible(true);


        btnAgregar.addActionListener(e -> {
            String correoIntegrante = txtCorreo.getText().trim();

            if (correoIntegrante.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un correo.");
                return;
            }

            boolean exito = controlador.agregarPersonaAEquipo(correoJefe, correoIntegrante);

            if (exito) {
                JOptionPane.showMessageDialog(this, "Trabajador agregado al equipo");
                txtCorreo.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar en Equipos.csv");
            }
        });

        btnVolver.addActionListener(e -> {
            navegador.navegarAVentanaJefe(correoJefe, contrasena, rol, this);
            navegador.cerrarVentanaActual(this);
        });


        btnEliminar.addActionListener(e -> {

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de que desea eliminar TODO el equipo asociado a " + correoJefe + "? Esta acción es irreversible.",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);


            if (confirmacion == JOptionPane.YES_OPTION) {

                boolean exito = controlador.eliminarEquipo(correoJefe);


                if (exito) {
                    JOptionPane.showMessageDialog(this, "El equipo ha sido eliminado con éxito del sistema.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error grave al intentar eliminar el equipo del archivo CSV.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });


        btnMostrar.addActionListener(e -> {
            List<String> equipo = controlador.obtenerEquipo(correoJefe);

            if (equipo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay integrantes registrados");
                return;
            }

            StringBuilder listado = new StringBuilder("Integrantes:\n");
            for (String correo : equipo) {
                listado.append("- ").append(correo).append("\n");
            }

            JOptionPane.showMessageDialog(this, listado.toString());
        });
    }

}