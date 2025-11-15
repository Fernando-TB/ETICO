package vista;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.Map;

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
    private JButton botonAnterior, botonSiguiente, botonVolver;
    private YearMonth mesActual;
    private final IControladorCitas controladorCitas;

    public CalendarioVista(String usuario, String contrasena, String rol, IControladorNavegacion navegador, IControladorAgendamiento agendador, IControladorCitas controladorCitas) {

        this.navegador = navegador;
        this.agendador = agendador;
        this.controladorCitas = controladorCitas;

        this.mesActual = YearMonth.now();

        LocalDate diaDePrueba = mesActual.atDay(mesActual.atDay(1).getDayOfMonth() + 5);
        citasDePrueba.put(diaDePrueba, "Reunión de Equipo, 10:00 - 11:00");
        citasDePrueba.put(mesActual.atDay(15), "Terminar codigo, 14:00");



        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;



        frame = new JFrame("Calendario de Citas");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 480);
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
        botonAnterior = new JButton("<");
        botonSiguiente = new JButton(">");
        labelMesAnio = new JLabel("", SwingConstants.CENTER);
        labelMesAnio.setFont(new Font("Arial", Font.BOLD, 18));

        panelNavegacion.add(botonAnterior, BorderLayout.WEST);
        panelNavegacion.add(labelMesAnio, BorderLayout.CENTER);
        panelNavegacion.add(botonSiguiente, BorderLayout.EAST);

        panelCalendario = new JPanel(new GridLayout(0, 7));
        panelCalendario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));

        panelContenido.add(crearPanelDiasSemana());

        panelContenido.add(panelCalendario);

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

        botonAnterior.addActionListener(e -> {
            mesActual = mesActual.minusMonths(1);
            actualizarCalendario();
        });
        botonSiguiente.addActionListener(e -> {
            mesActual = mesActual.plusMonths(1);
            actualizarCalendario();
        });

        actualizarCalendario();
        frame.setVisible(true);
        btnVolver(rol, usuario, contrasena);
    }

    private JPanel crearPanelDiasSemana() {
        JPanel panel = new JPanel(new GridLayout(1, 7)); // 1 fila, 7 columnas
        String[] dias = {"Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"};

        for (String dia : dias) {
            JLabel label = new JLabel(dia, SwingConstants.CENTER);
            label.setFont(label.getFont().deriveFont(Font.BOLD));
            panel.add(label);
        }
        return panel;
    }

    private void actualizarCalendario() {

        labelMesAnio.setText(mesActual.getMonth().name() + " " + mesActual.getYear());

        panelCalendario.removeAll();
        panelCalendario.setLayout(new GridLayout(0, 7, 5, 5));

        Map<LocalDate, String> citasDelMes = controladorCitas.obtenerCitasParaMes(mesActual, usuario);


        int diaInicio = mesActual.atDay(1).getDayOfWeek().getValue();


        int espacios = diaInicio % 7;

        if (diaInicio == 7) {
            espacios = 0;
        }


        for (int i = 0; i < espacios; i++) {
            panelCalendario.add(new JLabel(""));
        }

        int diasEnMes = mesActual.lengthOfMonth();
        LocalDate hoy = LocalDate.now();


        for (int dia = 1; dia <= diasEnMes; dia++) {
            LocalDate fecha = mesActual.atDay(dia);


            JPanel panelDia = new JPanel(new BorderLayout());
            panelDia.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            panelDia.setBackground(Color.WHITE);


            JLabel labelDia = new JLabel(String.valueOf(dia), SwingConstants.CENTER);
            labelDia.setFont(labelDia.getFont().deriveFont(Font.BOLD, 10f));
            panelDia.add(labelDia, BorderLayout.NORTH);


            if (citasDelMes.containsKey(fecha)) {
                String cita = citasDelMes.get(fecha);


                JPanel panelContenido = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 2));
                panelContenido.setBackground(Color.WHITE);

                JLabel labelCita = new JLabel("<html><center>" + cita + "</center></html>", SwingConstants.CENTER);
                labelCita.setFont(labelCita.getFont().deriveFont(9f));
                labelCita.setForeground(new Color(0, 102, 102));

                panelContenido.add(labelCita);

                panelDia.add(panelContenido, BorderLayout.CENTER);
                panelDia.setToolTipText(cita);
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
