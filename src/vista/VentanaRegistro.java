package vista;

import modelo.Registrar;
import modelo.Logueo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;


public class VentanaRegistro {


    private final Logueo logueo;
    private final JFrame frame;
    private final Registrar registroUsuarios;
    private JTextField campoCorreo;
    private JPasswordField campoContrasena;
    private JPasswordField campoConfirmar;
    private JComboBox<String> selectorRol;
    private JButton botonVolver;
    private JButton botonRegistrar;

    public VentanaRegistro(Registrar registroUsuarios, Logueo logueo) {

        this.logueo = logueo;

        this.registroUsuarios = registroUsuarios;


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

            String correo = campoCorreo.getText();
            String rol = (String) selectorRol.getSelectedItem();
            String contrasena = new String(campoContrasena.getPassword());
            String confirmar = new String(campoConfirmar.getPassword());



        //Comprobar que la contraseña confirmada sea igual
        if (Objects.equals(contrasena, confirmar)) {
            System.out.println("Registrando Usuario");
        } else {

            JOptionPane.showMessageDialog(frame, "Los campos de contraseñas no coinciden");
        }

        if (registroUsuarios.guardarUsuario(correo, contrasena, rol)) {
            JOptionPane.showMessageDialog(frame, "Usuario registrado con éxito. Vuelva al Login.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);

            campoCorreo.setText("");
            campoContrasena.setText("");
            campoConfirmar.setText("");

            irALogin();
        } else {
            JOptionPane.showMessageDialog(frame, "Error: El correo ya está registrado.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
        }

        });

        botonVolver.addActionListener(e -> {

            irALogin();
        });
    }

    private void irALogin() {
        this.ocultar();
        SwingUtilities.invokeLater(() -> {
            new VentanaLogin(registroUsuarios, logueo).mostrar();
        });
    }

    public void ocultar() {
        frame.dispose();
    }

    public void mostrar() {
        frame.setVisible(true);
    }

}
