package vista;

import javax.swing.*;
import java.awt.*;

import controlador.IControladorNavegacion;
import controlador.IControladorAutenticacion;

public class VentanaTrabajador {

    private final JFrame frame;
    private final IControladorAutenticacion autenticador;
    private final IControladorNavegacion navegador;

    private JButton botonVerHorario;
    private JButton botonVolverLogin;

    public VentanaTrabajador(IControladorNavegacion navegador, IControladorAutenticacion autenticador,
                             String usuario, String contrasena) {

        this.navegador = navegador;
        this.autenticador = autenticador;

        this.frame = new JFrame("Ventana de Trabajador - ETICO");

        Color colorFondo = new Color(41, 49, 51);
        Color colorTexto = Color.WHITE;
        Color colorPanelBotones = new Color(56, 65, 69);
        Color colorBotones = new Color(34, 39, 41);

        try {
            ImageIcon Logo = new ImageIcon(getClass().getResource("/LOGO.png"));
            frame.setIconImage(Logo.getImage());

        } catch (Exception e) {
            System.err.println("Error al abrir el LOGO");
        }

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 250);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(colorFondo);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(1, 1, 20, 20));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panelBotones.setBackground(colorFondo);

        botonVerHorario = new JButton("Ver Horario");
        botonVerHorario.setBackground(colorBotones);
        botonVerHorario.setForeground(colorTexto);

        JPanel panelNavegacion = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelNavegacion.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        panelNavegacion.setBackground(colorPanelBotones);

        botonVolverLogin = new JButton("Volver al Login");
        botonVolverLogin.setBackground(colorBotones);
        botonVolverLogin.setForeground(colorTexto);

        panelNavegacion.add(botonVolverLogin);
        panelBotones.add(botonVerHorario);

        frame.add(panelBotones, BorderLayout.CENTER);
        frame.add(panelNavegacion, BorderLayout.SOUTH);

        agregarListeners(usuario, contrasena, "Trabajador");
    }

    private void agregarListeners(String usuario, String contrasena, String rol) {

        botonVolverLogin.addActionListener(e -> {
            navegador.cerrarVentanaActual(this.frame);
            irALogin();
        });

        botonVerHorario.addActionListener(e -> {
            navegador.cerrarVentanaActual(this.frame);
            navegador.navegarACalendarioVista(usuario, contrasena, rol, frame);
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
