
package vista;

import modelo.Logueo;
import modelo.Registrar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import controlador.IControladorAutenticacion;
import controlador.IControladorNavegacion;

public class VentanaLogin {

    private final IControladorAutenticacion autenticador;
    private final IControladorNavegacion navegador;

    private final JFrame frame;
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JButton botonLogin;
    private JButton botonRegistro;


    public VentanaLogin(IControladorAutenticacion autenticador, IControladorNavegacion navegador) {
        this.autenticador = autenticador;
        this.navegador = navegador;

        this.frame = new JFrame("Iniciar Sesion - ETICO");

        //ICONO
        try {
            ImageIcon Logo = new ImageIcon(getClass().getResource("/LOGO.png"));
            frame.setIconImage(Logo.getImage());

        } catch (Exception e) {
            System.err.println("Error al abrir el LOGO");
        }

        Color colorFondo = new Color(41, 49, 51);

        Color colorTexto = Color.WHITE;

        Color colorPanelBotones = new Color(56, 65, 69);

        Color colorBotones = new Color(34, 39, 41);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(0, 0));
        frame.setBackground(colorFondo);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        campoUsuario = new JTextField(20);
        campoContrasena = new JPasswordField(20);

        JLabel labelcorreo = new JLabel("Correo Electronico:");
        panel.add(labelcorreo);
        labelcorreo.setForeground(colorTexto);


        panel.add(campoUsuario);

        JLabel labelContrasena = new JLabel("Contraseña");
        panel.add(labelContrasena);
        panel.add(campoContrasena);
        labelContrasena.setForeground(colorTexto);


        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.setBackground(colorFondo);


        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(colorPanelBotones);

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

        botonLogin.addActionListener(e ->  {
            String usuario = campoUsuario.getText();
            String password = new String(campoContrasena.getPassword());
            verificarLogin(usuario, password);

        });

        botonRegistro.addActionListener(e -> {
            this.ocultar();
            SwingUtilities.invokeLater(() -> {
                navegador.navegarARegistro();
            });
        });
    }

    private void verificarLogin(String usuario, String contrasena) {
        String rol = autenticador.intentarLogin(usuario, contrasena);

        if (rol != null) {

            if  (rol.equals("Trabajador")) {
                navegador.navegarAVentanaTrabajador(usuario, contrasena, rol, frame);
                navegador.cerrarVentanaActual(this.frame);
            }else if  (rol.equals("Jefe")) {
                navegador.navegarAVentanaJefe(usuario, contrasena, rol, frame);
                navegador.cerrarVentanaActual(this.frame);
            }

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
