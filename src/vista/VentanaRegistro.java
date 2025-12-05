
package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import controlador.IControladorAutenticacion;
import controlador.IControladorNavegacion;


public class VentanaRegistro {



    private final JFrame frame;

    private JTextField campoCorreo;
    private JPasswordField campoContrasena;
    private JPasswordField campoConfirmar;
    private JComboBox<String> selectorRol;
    private JButton botonVolver;
    private JButton botonRegistrar;

    private final IControladorAutenticacion autenticador;
    private final IControladorNavegacion navegador;

    final String REGEX_CORREO = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";


    public VentanaRegistro(IControladorAutenticacion autenticador, IControladorNavegacion navegador) {

        this.autenticador = autenticador;
        this.navegador = navegador;


        this.frame = new JFrame("Registro - ETICO");

        //ICONO
        try {
            ImageIcon Logo = new ImageIcon(getClass().getResource("/LOGO.png"));
            frame.setIconImage(Logo.getImage());

        } catch (Exception e) {
            System.err.println("Error al abrir el LOGO");
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        campoCorreo = new JTextField(20);
        campoContrasena = new JPasswordField(20);
        campoConfirmar = new JPasswordField(20);
        selectorRol = new JComboBox<>(new String[]{"Trabajador", "Jefe"});

        panel.add(new JLabel("Correo Electronico:"));
        panel.add(campoCorreo);

        panel.add(new JLabel("Contraseña:"));
        panel.add(campoContrasena);

        panel.add(new JLabel("Confirmar contraseña:"));
        panel.add(campoConfirmar);

        panel.add(new JLabel("Rola:"));
        panel.add(selectorRol);

        panel.add(new JLabel());
        panel.add(new JLabel());


        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        botonRegistrar = new JButton("Registrarse");
        botonVolver = new JButton("Volver");

        panelBotones.add(botonRegistrar);
        panelBotones.add(botonVolver);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(panelBotones, BorderLayout.SOUTH);


        agregarListeners();

    }

    private void agregarListeners() {



        botonRegistrar.addActionListener(e -> {

            String correo = campoCorreo.getText().trim();
            String rol = (String) selectorRol.getSelectedItem();
            String contrasena = new String(campoContrasena.getPassword());
            String confirmar = new String(campoConfirmar.getPassword());


            if (!Objects.equals(contrasena, confirmar)) {
                JOptionPane.showMessageDialog(frame, "Los campos de contraseñas no coinciden", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (autenticador.registrarNuevoUsuario(correo, contrasena, rol)) {

                JOptionPane.showMessageDialog(frame, "Usuario registrado con éxito. Vuelva al Login.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);

                campoCorreo.setText("");
                campoContrasena.setText("");
                campoConfirmar.setText("");
                irALogin();
            } else {
                if (!Utilitarios.Validador.esCorreoValido(correo)) {
                    JOptionPane.showMessageDialog(frame, "Formato de correo electrónico inválido. Asegúrate de incluir un '@' y al menos un punto '.' después del '@'.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
                } else {

                    JOptionPane.showMessageDialog(frame, "Error: El correo ya está registrado.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        botonVolver.addActionListener(e -> {
            irALogin();
        });

        botonVolver.addActionListener(e -> {
            irALogin();
        });


        botonVolver.addActionListener(e -> {

            irALogin();
        });
    }

    private void irALogin() {
        navegador.cerrarVentanaActual(this.frame);
        navegador.navegarALogin();
    }

    public void ocultar() {
        frame.dispose();
    }

    public void mostrar() {
        frame.setVisible(true);
    }

}
