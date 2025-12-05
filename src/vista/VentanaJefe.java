
package vista;
import controlador.IControladorAgendamiento;
import controlador.IControladorAutenticacion;
import controlador.IControladorNavegacion;

import javax.swing.*;
import java.awt.*;

public class VentanaJefe {

    private final JFrame frame;

    private JButton botonVerHorario;
    private JButton botonVolverLogin;
    private JButton botonAgregarEquipo;
    private JButton botonIniciarEvento;

    private final IControladorNavegacion navegador;
    private final IControladorAgendamiento agendador;
    private final String contrasena;


    public VentanaJefe(IControladorNavegacion navegador, IControladorAutenticacion autenticador, String usuario, String contrasena, IControladorAgendamiento agendador) {

        this.navegador = navegador;
        this.agendador = agendador;
        this.contrasena = contrasena;

        Color colorFondo = new Color(41, 49, 51);

        Color colorTexto = Color.WHITE;

        Color colorPanelBotones = new Color(56, 65, 69);

        this.frame = new JFrame("Ventana de Jefe - ETICO");

        //ICONO
        try {
            ImageIcon Logo = new ImageIcon(getClass().getResource("/LOGO.png"));
            frame.setIconImage(Logo.getImage());

        } catch (Exception e) {
            System.err.println("Error al abrir el LOGO");
        }


        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 350);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(0, 0));

        JPanel panelBotones = new JPanel();

        panelBotones.setLayout(new GridLayout(3, 1, 20, 20));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        botonVerHorario = new JButton("Ver Horario");
        botonAgregarEquipo = new JButton("Ver Equipo");
        botonIniciarEvento = new JButton("Iniciar Evento");

        JPanel panelNavegacion = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelNavegacion.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));


        botonVolverLogin = new JButton("Volver al Login");
        panelNavegacion.add(botonVolverLogin);

        panelBotones.setBackground(colorFondo);
        panelNavegacion.setBackground(colorPanelBotones);

        panelBotones.add(botonVerHorario);
        panelBotones.add(botonAgregarEquipo);

        panelBotones.add(botonIniciarEvento);

        frame.add(panelBotones, BorderLayout.CENTER);
        frame.add(panelNavegacion, BorderLayout.SOUTH);
        String rol = "Jefe";
        agregarListeners(usuario, rol);

    }

    private void agregarListeners(String usuario, String rol) {



        botonVolverLogin.addActionListener(e -> {
            irALogin();
            navegador.cerrarVentanaActual(this.frame);
        });

        botonVerHorario.addActionListener(e -> {
            navegador.navegarACalendarioVista(usuario, this.contrasena, rol, this.frame);
            navegador.cerrarVentanaActual(this.frame);
        });


        botonAgregarEquipo.addActionListener(e -> {
            navegador.navegarAAgregarEquipo(usuario, this.contrasena, rol, this.frame);
        });

        botonIniciarEvento.addActionListener(e -> {
            navegador.cerrarVentanaActual(this.frame);
            navegador.navegarAIniciarEvento(usuario, contrasena, rol, this.frame);
        });
    }

    private void irALogin() {
        navegador.cerrarVentanaActual(this.frame);
        navegador.navegarALogin();
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}
