package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaLogin {

    private final JFrame frame;

    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JButton botonLogin;
    private JButton botonRegistro;

    public VentanaLogin() {

        this.frame = new JFrame("Iniciar Sesion");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        campoUsuario = new JTextField(20);
        campoContrasena = new JPasswordField(20);

        panel.add(new JLabel("Correo Electronico:"));
        panel.add(campoUsuario);
        panel.add(campoContrasena);

        panel.add(new JLabel());
        panel.add(new JLabel());


        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        botonLogin = new JButton("Iniciar Sesion");
        botonRegistro = new JButton("Registrarse");

        panelBotones.add(botonLogin);
        panelBotones.add(botonRegistro);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(panelBotones, BorderLayout.SOUTH);

        agregarListeners();
    }

        private void agregarListeners() {
            botonLogin.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String usuario = campoUsuario.getText();
                    char[] password = campoContrasena.getPassword();

                    System.out.println("Intento de Login para: " + usuario);

                    simularLogin(usuario);

                    for (int i = 0; i < password.length; i++) {
                        password[i] = 0;
                    }
                }
            });

            botonRegistro.addActionListener(e -> {
                System.out.println("Hacia ventana de Registro");
            });

        }

        private void simularLogin(String usuario) {

            if (usuario.toLowerCase().contains("jefe")) {
                JOptionPane.showMessageDialog(frame, "Login exitoso como Jefe.");
            } else {
                JOptionPane.showMessageDialog(frame, "Login exitoso como Trabajador.");
            }
        }
    }
