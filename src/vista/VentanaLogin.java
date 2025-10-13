package vista;

import modelo.Logueo;
import modelo.Registrar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaLogin {
    private final Logueo logueo;
    private final JFrame frame;
    private final Registrar registroUsuarios;
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JButton botonLogin;
    private JButton botonRegistro;

    public VentanaLogin(Registrar registroUsuarios, Logueo logueo) {
        this.registroUsuarios = registroUsuarios;
        this.logueo = logueo;

        this.frame = new JFrame("Iniciar Sesion - ETICO");

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

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        campoUsuario = new JTextField(20);
        campoContrasena = new JPasswordField(20);

        panel.add(new JLabel("Correo Electronico:"));
        panel.add(campoUsuario);

        panel.add(new JLabel("Contraseña:"));
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

        KeyListener enterKeyListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    botonLogin.doClick();
                }
            }
        };

        campoUsuario.addKeyListener(enterKeyListener);
        campoContrasena.addKeyListener(enterKeyListener);

        botonLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String usuario = campoUsuario.getText();
                String password = new String(campoContrasena.getPassword());

                System.out.println("Intento de Login para: " + usuario);

                verificarLogin(usuario, password);

                campoUsuario.setText("");
                campoContrasena.setText("");

            }
        });

        botonRegistro.addActionListener(e -> {
            this.ocultar();
            SwingUtilities.invokeLater(() -> {
                new VentanaRegistro(registroUsuarios, logueo).mostrar();
            });
        });
    }

    private void verificarLogin(String usuario, String contrasena) {
        String rol = logueo.obtenerRol(usuario, contrasena);

        if (rol != null) {
            if  (rol.equals("Trabajador")) {
                VentanaTrabajador ventanatrabajador = new VentanaTrabajador(registroUsuarios, logueo, usuario, contrasena);
                ventanatrabajador.mostrar();
                this.frame.dispose();
            }else if  (rol.equals("Jefe")) {
                VentanaJefe ventanajefe = new VentanaJefe(usuario, registroUsuarios, logueo, contrasena);
                ventanajefe.mostrar();
                this.frame.dispose();
            }
            this.ocultar();
        } else {
            JOptionPane.showMessageDialog(frame, "Login incorrecto.");
        }
    }

        public void mostrar() {
            frame.setVisible(true);
        }

        public void ocultar() {
            frame.dispose();
        }

}
