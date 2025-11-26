
package vista;

import controlador.IControladorEquipos;
import controlador.IControladorNavegacion;
import modelo.Registrar;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class VentanaAgregarEquipo extends JFrame {

    private final Registrar registrar;
    private final IControladorNavegacion navegador;
    private JFrame frame;

    private JTextField txtCorreo;
    private JButton btnAgregar;
    private JButton btnVolver;
    private JButton btnMostrar;
    private final IControladorEquipos controlador;
    private final String correoJefe;

    public VentanaAgregarEquipo(IControladorEquipos controlador, Registrar registrar,String correoJefe, String contrasena, String rol, IControladorNavegacion navegador) {
        this.controlador = controlador;
        this.registrar = registrar;
        this.correoJefe = correoJefe;
        this.navegador = navegador;
        this.frame =  new JFrame("Agregar Equipo");

        //ICONO
        try {
            ImageIcon Logo = new ImageIcon(getClass().getResource("/LOGO.png"));
            setIconImage(Logo.getImage());

        } catch (Exception e) {
            System.err.println("Error al abrir el LOGO");
        }

        setTitle("Agregar Equipo");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 5, 5));




        JPanel panelCorreo = new JPanel();
        panelCorreo.add(new JLabel("Correo: "));
        txtCorreo = new JTextField(18);
        panelCorreo.add(txtCorreo);


        JPanel panelBotones = new JPanel();
        btnAgregar = new JButton("Agregar");
        btnVolver = new JButton("Volver");
        btnMostrar = new JButton("Mostrar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnVolver);
        panelBotones.add(btnMostrar);

        add(panelCorreo);
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
