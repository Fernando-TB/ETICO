package vista;

import modelo.Registrar;
import modelo.Logueo;

import javax.swing.*;
import java.awt.*;

public class VentanaJefe {

    private final JFrame frame;
    private final Registrar registroUsuarios;
    private final Logueo logueo;

    private JButton botonVerHorario;
    private JButton botonReunionObligatoria;
    private JButton botonVolverLogin;


    public VentanaJefe(Registrar registroUsuarios, Logueo logueo) {

        this.registroUsuarios = registroUsuarios;
        this.logueo = logueo;

        this.frame = new JFrame("Panel de Jefe - ETICO");

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

        agregarListeners();
    }

    private void agregarListeners() {

        botonVolverLogin.addActionListener(e -> {
            irALogin();
        });

        botonVerHorario.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Funcionalidad 'Ver Horario' no implementada.");
        });

        botonReunionObligatoria.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Funcionalidad 'Reunión Obligatoria' no implementada.");
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