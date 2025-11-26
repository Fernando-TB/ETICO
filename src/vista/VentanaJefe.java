
package vista;
import controlador.IControladorAgendamiento;
import controlador.IControladorAutenticacion;
import controlador.IControladorNavegacion;

import javax.swing.*;
import java.awt.*;

public class VentanaJefe {

    private final JFrame frame;

    private JButton botonVerHorario;
    private JButton botonReunionObligatoria;
    private JButton botonVolverLogin;

    private final IControladorNavegacion navegador;
    private final IControladorAgendamiento agendador;
    private final String contrasena;


    public VentanaJefe(IControladorNavegacion navegador, IControladorAutenticacion autenticador, String usuario, String contrasena, IControladorAgendamiento agendador) {

        this.navegador = navegador;
        this.agendador = agendador;
        this.contrasena = contrasena;

        this.frame = new JFrame("Ventana de Jefe - ETICO");

        //ICONO
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

        JPanel panelBotones = new JPanel();

        panelBotones.setLayout(new GridLayout(2, 1, 20, 20));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        botonVerHorario = new JButton("Ver Horario");
        botonReunionObligatoria = new JButton("Reunión Obligatoria");

        JPanel panelNavegacion = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelNavegacion.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));


        botonVolverLogin = new JButton("Volver al Login");
        panelNavegacion.add(botonVolverLogin);


        panelBotones.add(botonVerHorario);
        panelBotones.add(botonReunionObligatoria);

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

        botonReunionObligatoria.addActionListener(e -> {
            navegador.cerrarVentanaActual(this.frame);
            navegador.navegarAAgendarReunion(usuario, this.contrasena, rol, this.frame);
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
