package vista;

import controlador.IControladorAgendamiento;
import controlador.IControladorNavegacion;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class CalendarioAgendar {

    private final JFrame frame;
    private final IControladorNavegacion navegador;
    private final IControladorAgendamiento agendador;


    private JTextField campoTitulo;
    private JTextField campoFecha;
    private JTextField campoHoraInicio;
    private JTextField campoHoraFin;
    private JTextField campoCorreo;

    private JButton botonAgendar;
    private JButton botonVolver;


    public CalendarioAgendar(IControladorNavegacion navegador, IControladorAgendamiento agendador, String usuarioJefe, String contrasenaJefe, String rol) {

        this.navegador = navegador;
        this.agendador = agendador;

        this.frame = new JFrame("Agendar Reunión Obligatoria");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        //ICONO
        try {
            ImageIcon Logo = new ImageIcon(getClass().getResource("/LOGO.png"));
            frame.setIconImage(Logo.getImage());

        } catch (Exception e) {
            System.err.println("Error al abrir el LOGO");
        }


        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0; gbc.gridy = 0;
        panelPrincipal.add(new JLabel("Título de la Reunión:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        campoTitulo = new JTextField(20);
        panelPrincipal.add(campoTitulo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelPrincipal.add(new JLabel("Fecha (YYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        campoFecha = new JTextField(LocalDate.now().toString(), 20);
        panelPrincipal.add(campoFecha, gbc);


        gbc.gridx = 0; gbc.gridy = 3;
        panelPrincipal.add(new JLabel("Hora Inicio (HH:MM):"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        campoHoraInicio = new JTextField("10:00", 20);
        panelPrincipal.add(campoHoraInicio, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panelPrincipal.add(new JLabel("Hora Fin (HH:MM):"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        campoHoraFin = new JTextField("11:00", 20);
        panelPrincipal.add(campoHoraFin, gbc);

        campoCorreo = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 2;
        panelPrincipal.add(new JLabel("Correo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        panelPrincipal.add(campoCorreo, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        botonVolver = new JButton("Volver");
        botonAgendar = new JButton("Agendar Reunión");

        panelBotones.add(botonVolver);
        panelBotones.add(botonAgendar);

        frame.add(panelPrincipal, BorderLayout.CENTER);
        frame.add(panelBotones, BorderLayout.SOUTH);

        agregarListeners(usuarioJefe, contrasenaJefe, rol);
    }

    private void agregarListeners(String usuarioJefe, String contrasenaJefe, String rol) {

        botonVolver.addActionListener(e -> {

            navegador.cerrarVentanaActual(this.frame);
            navegador.navegarAVentanaJefe(usuarioJefe, contrasenaJefe, rol, this.frame);
        });

        botonAgendar.addActionListener(e -> {

            String titulo = campoTitulo.getText().trim();
            String fechaStr = campoFecha.getText().trim();
            String horaInicioStr = campoHoraInicio.getText().trim();
            String horaFinStr = campoHoraFin.getText().trim();
            String correoStr = campoCorreo.getText().trim();


            if (titulo.isEmpty() || fechaStr.isEmpty() || horaInicioStr.isEmpty() || horaFinStr.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Por favor complete todos los campos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                LocalTime inicio = LocalTime.parse(horaInicioStr, DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime fin = LocalTime.parse(horaFinStr, DateTimeFormatter.ofPattern("HH:mm"));

                if (inicio.isAfter(fin) || inicio.equals(fin)) {
                    JOptionPane.showMessageDialog(frame, "La hora de inicio debe ser anterior a la hora de fin.", "Error de Horario", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "El formato de hora no es válido. Use HH:MM.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (agendador.agendarCita(correoStr, fechaStr, horaInicioStr, horaFinStr, titulo)){

                JOptionPane.showMessageDialog(
                        frame,
                        "Cita agendada con éxito para " + correoStr,
                        "Agendamiento Exitoso",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(
                        frame,
                        "El correo ingresado no existe en el sistema.",
                        "Usuario no válido",
                        JOptionPane.ERROR_MESSAGE);
            }
            campoTitulo.setText("");
            campoHoraInicio.setText("10:00");
            campoHoraFin.setText("11:00");

        });
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}