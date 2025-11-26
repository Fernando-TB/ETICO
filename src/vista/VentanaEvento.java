package vista;

import controlador.IControladorAgendamiento;
import controlador.IControladorNavegacion;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class VentanaEvento extends JFrame {

    private final JTextField txtTitulo;
    private final JTextField txtDuracion;
    private final JButton btnIniciar;
    private final JButton btnVolver;

    private final IControladorAgendamiento agendador;
    private final IControladorNavegacion navegador;

    private final String correoJefe;
    private final String contrasena;
    private final String rol;

    public VentanaEvento(IControladorAgendamiento agendador, IControladorNavegacion navegador, String correoJefe, String contrasena, String rol) {
        this.agendador = agendador;
        this.navegador = navegador;
        this.correoJefe = correoJefe;
        this.contrasena = contrasena;
        this.rol = rol;

        setTitle("Iniciar Evento");
        setSize(400, 220);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10));


        JPanel panelTitulo = new JPanel();
        panelTitulo.add(new JLabel("Título del Evento: "));
        txtTitulo = new JTextField(20);
        panelTitulo.add(txtTitulo);


        JPanel panelDuracion = new JPanel();
        panelDuracion.add(new JLabel("Duración (HH:mm): "));
        txtDuracion = new JTextField(10);
        panelDuracion.add(txtDuracion);


        JPanel panelBotones = new JPanel();
        btnIniciar = new JButton("Iniciar");
        btnVolver = new JButton("Volver");
        panelBotones.add(btnIniciar);
        panelBotones.add(btnVolver);

        add(panelTitulo);
        add(panelDuracion);
        add(panelBotones);

        agregarEventos();
        setVisible(true);

        try {
            ImageIcon Logo = new ImageIcon(getClass().getResource("/LOGO.png"));
            setIconImage(Logo.getImage());

        } catch (Exception e) {
            System.err.println("Error al abrir el LOGO");
        }
    }

    private void agregarEventos() {

        btnVolver.addActionListener(e -> {
            navegador.navegarAVentanaJefe(correoJefe, contrasena, rol, this);
        });

        btnIniciar.addActionListener(e -> {

            String titulo = txtTitulo.getText().trim();
            String duracionStr = txtDuracion.getText().trim();

            if (titulo.isEmpty() || duracionStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe completar ambos campos.");
                return;
            }

            LocalTime duracion;

            try {
                duracion = LocalTime.parse(duracionStr);
            }catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de duracion invalido (HH:mm)");
                return;
            }

            int minutosTotales = duracion.getHour() * 60 + duracion.getMinute();


            VentanaConfirmacionEvento confirmar = new VentanaConfirmacionEvento(this, "¿Esta seguro de que desea emitir el evento?");
            boolean continuuar = confirmar.mostrar();

            if (!continuuar) return;

            System.out.println("Duracion total: " + minutosTotales);

            JOptionPane.showMessageDialog(this, "Evento iniciado");

            txtTitulo.setText("");
            txtDuracion.setText("");
        });

    }
}
