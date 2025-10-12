package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaRegistro {

    private final JFrame frame;
    private JTextField campoCorreo;
    private JPasswordField campoContrasena;
    private JPasswordField campoConfirmar;
    private JComboBox<String> selectorRol;
    private JButton botonVolver;
    private JButton botonRegistrar;

    public VentanaRegistro() {

        this.frame = new JFrame("Registro");

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
            System.out.println("Registrando Usuario");
        });

        botonVolver.addActionListener(e -> {

            irALogin();
        });
    }

    private void irALogin() {
        this.ocultar();
        SwingUtilities.invokeLater(() -> {
            new VentanaLogin().mostrar();
        });
    }

    public void ocultar() {
        frame.dispose();
    }

    public void mostrar() {
        frame.setVisible(true);
    }

}
