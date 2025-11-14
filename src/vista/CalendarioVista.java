package vista;

import javax.swing.*;
import java.awt.*;
import java.time.*;

import controlador.IControladorNavegacion;
import controlador.IControladorAgendamiento;

public class CalendarioVista {

    private final String usuario;
    private final String contrasena;
    private final String rol;

    private JFrame frame;
    private final IControladorAgendamiento agendador;
    private IControladorNavegacion navegador;
    private JPanel panelCalendario;
    private JLabel labelMesAnio;
    private JButton botonAnterior, botonSiguiente, botonVolver;
    private YearMonth mesActual;

    public CalendarioVista(String usuario, String contrasena, String rol, IControladorNavegacion navegador, IControladorAgendamiento agendador) {

        this.navegador = navegador;
        this.agendador = agendador;

        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;



        frame = new JFrame("Calendario de Citas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        mesActual = YearMonth.now();

        JPanel panelNavegacion = new JPanel(new BorderLayout());
        botonAnterior = new JButton("<");
        botonSiguiente = new JButton(">");
        labelMesAnio = new JLabel("", SwingConstants.CENTER);
        labelMesAnio.setFont(new Font("Arial", Font.BOLD, 18));

        panelNavegacion.add(botonAnterior, BorderLayout.WEST);
        panelNavegacion.add(labelMesAnio, BorderLayout.CENTER);
        panelNavegacion.add(botonSiguiente, BorderLayout.EAST);
        frame.add(panelNavegacion, BorderLayout.NORTH);

        panelCalendario = new JPanel(new GridLayout(0, 7, 5, 5));
        panelCalendario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(panelCalendario, BorderLayout.CENTER);

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

    private void actualizarCalendario() {
        panelCalendario.removeAll();


        labelMesAnio.setText(mesActual.getMonth().getDisplayName(
                java.time.format.TextStyle.FULL,
                java.util.Locale.forLanguageTag("es-ES")).toUpperCase()
                + " " + mesActual.getYear());


        String[] diasSemana = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        for (String dia : diasSemana) {
            JLabel lbl = new JLabel(dia, SwingConstants.CENTER);
            lbl.setFont(new Font("Arial", Font.BOLD, 12));
            lbl.setForeground(new Color(0, 90, 160));
            panelCalendario.add(lbl);
        }

        LocalDate primerDia = mesActual.atDay(1);
        int diasEnMes = mesActual.lengthOfMonth();
        int diaInicio = primerDia.getDayOfWeek().getValue(); // 1=Lun ... 7=Dom

        int espacios = (diaInicio == 7) ? 0 : diaInicio - 1;
        for (int i = 0; i < espacios; i++) {
            panelCalendario.add(new JLabel(""));
        }


        LocalDate hoy = LocalDate.now();
        for (int dia = 1; dia <= diasEnMes; dia++) {
            LocalDate fecha = mesActual.atDay(dia);
            JButton botonDia = new JButton(String.valueOf(dia));
            botonDia.setFocusPainted(false);
            botonDia.setBackground(Color.WHITE);

            if (fecha.equals(hoy)) {
                botonDia.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            } else {
                botonDia.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            }
            panelCalendario.add(botonDia);
        }

        panelCalendario.revalidate();
        panelCalendario.repaint();

    }


    private void btnVolver(String rol, String usuario, String contrasena) {
        botonVolver.addActionListener(e -> {
            navegador.cerrarVentanaActual(this.frame);
            if (rol.equals("Jefe")){
                navegador.navegarAVentanaJefe(this.usuario, this.contrasena);
            } else {
                navegador.navegarAVentanaTrabajador(this.usuario, this.contrasena);
            }
            this.frame.dispose();
        });
    }


    public void mostrar() {
        this.frame.setVisible(true);
    }
}
