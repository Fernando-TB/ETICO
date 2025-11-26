package vista;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.Map;
import java.time.format.TextStyle;
import java.util.Locale;


import controlador.IControladorNavegacion;
import controlador.IControladorAgendamiento;
import controlador.IControladorCitas;

public class CalendarioVista {

    private final String usuario;
    private final String contrasena;
    private final String rol;

    private final java.util.Map<LocalDate, String> citasDePrueba = new java.util.HashMap<>();

    private JFrame frame;
    private final IControladorAgendamiento agendador;
    private IControladorNavegacion navegador;
    private JPanel panelCalendario;
    private JLabel labelMesAnio;
    private JButton botonVolver;
    private LocalDate semanaActual;
    private final IControladorCitas controladorCitas;

    public CalendarioVista(String usuario, String contrasena, String rol, IControladorNavegacion navegador, IControladorAgendamiento agendador, IControladorCitas controladorCitas) {

        this.semanaActual = LocalDate.now();
        this.navegador = navegador;
        this.agendador = agendador;
        this.controladorCitas = controladorCitas;



        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;



        frame = new JFrame("Calendario de Citas");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 480);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        //ICONO
        try {
            ImageIcon Logo = new ImageIcon(getClass().getResource("/LOGO.png"));
            frame.setIconImage(Logo.getImage());

        } catch (Exception e) {
            System.err.println("Error al abrir el LOGO");
        }



        JPanel panelNavegacion = new JPanel(new BorderLayout());

        labelMesAnio = new JLabel("", SwingConstants.CENTER);
        labelMesAnio.setFont(new Font("Arial", Font.BOLD, 18));

        panelNavegacion.add(labelMesAnio, BorderLayout.CENTER);


        panelCalendario = new JPanel(new GridLayout(0, 1));
        panelCalendario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));

        panelContenido.add(crearPanelDiasSemana());

        JPanel contenedorCentro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contenedorCentro.add(panelCalendario);

        panelCalendario.setPreferredSize(new Dimension(1000, 200));

        panelContenido.add(contenedorCentro);


        JPanel panelPrincipal = new JPanel(new BorderLayout());

        panelPrincipal.add(panelNavegacion, BorderLayout.NORTH);

        panelPrincipal.add(panelContenido, BorderLayout.CENTER);

        frame.add(panelPrincipal, BorderLayout.CENTER);


        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER));
        botonVolver = new JButton("Volver");
        botonVolver.setPreferredSize(new Dimension(120, 35));
        botonVolver.setFocusPainted(false);
        botonVolver.setFont(new Font("Arial", Font.BOLD, 13));
        panelInferior.add(botonVolver);
        frame.add(panelInferior, BorderLayout.SOUTH);


        actualizarCalendario();
        frame.setVisible(true);
        btnVolver(rol, usuario, contrasena);
    }

    private JPanel crearPanelDiasSemana() {
        JPanel panel = new JPanel(new GridLayout(1, 7));
        return panel;
    }

    private void actualizarCalendario() {




        LocalDate lunes = semanaActual.with(DayOfWeek.MONDAY);
        LocalDate domingo = lunes.plusDays(6);

        labelMesAnio.setText("Semana actual");

        panelCalendario.removeAll();
        panelCalendario.setLayout(new GridLayout(1, 7, 5, 5));


        Map<LocalDate, String> citasSemana = controladorCitas.obtenerCitasEntreFechas(lunes, domingo, usuario);

        LocalDate hoy = LocalDate.now();

        for (int i = 0; i < 7; i++) {
            LocalDate fecha = lunes.plusDays(i);



            JPanel panelDia = new JPanel(new BorderLayout());
            panelDia.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            panelDia.setBackground(Color.WHITE);

            String nombreDia = fecha.getDayOfWeek()
                    .getDisplayName(TextStyle.SHORT, new Locale("es", "ES"));
            nombreDia = nombreDia.substring(0, 1).toUpperCase() + nombreDia.substring(1);

            JLabel labelDia = new JLabel(
                    nombreDia + " " + fecha.getDayOfMonth(),
                    SwingConstants.CENTER
            );
            labelDia.setFont(labelDia.getFont().deriveFont(Font.BOLD, 12f));
            panelDia.add(labelDia, BorderLayout.NORTH);

            if (fecha.getDayOfWeek() == DayOfWeek.SUNDAY) {
                panelDia.setBackground(new Color(255, 150, 150));
            }

            if (citasSemana.containsKey(fecha)) {
                String cita = citasSemana.get(fecha);

                JPanel panelContenido = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 2));
                panelContenido.setBackground(Color.WHITE);

                JLabel labelCita = new JLabel("<html><center>" + cita + "</center></html>", SwingConstants.CENTER);
                labelCita.setFont(labelCita.getFont().deriveFont(10f));
                labelCita.setForeground(new Color(0, 102, 102));

                panelContenido.add(labelCita);
                panelDia.add(panelContenido, BorderLayout.CENTER);

                panelDia.setToolTipText("<html><p style='width:150px;'>" + cita + "</p></html>");
            }else {
                panelDia.setToolTipText("Sin citas");
            }

            if (fecha.getDayOfWeek() == DayOfWeek.SUNDAY) {
                panelDia.setToolTipText("Día no laboral");
            }


            if (fecha.equals(hoy)) {
                panelDia.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            }

            panelCalendario.add(panelDia);
        }

        panelCalendario.revalidate();
        panelCalendario.repaint();
    }


    private void btnVolver(String rol, String usuario, String contrasena) {
        botonVolver.addActionListener(e -> {
            navegador.cerrarVentanaActual(this.frame);
            if (rol.equals("Jefe")){
                navegador.navegarAVentanaJefe(this.usuario, this.contrasena, this.rol, this.frame);
            } else {
                navegador.navegarAVentanaTrabajador(this.usuario, this.contrasena, this.rol, this.frame);
            }
            this.frame.dispose();
        });
    }


    public void mostrar() {
        this.frame.setVisible(true);
    }
}
