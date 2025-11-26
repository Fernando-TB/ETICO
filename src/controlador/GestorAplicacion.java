package controlador;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;
import modelo.*;
import vista.*;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import modelo.Registrar;
import controlador.ConversorCSV;

public class GestorAplicacion implements IControladorAgendamiento, IControladorAutenticacion, IControladorNavegacion, IControladorCitas {

    private final Registrar registroUsuarios;
    private final Logueo logueo;
    private final Logica logica;
    private final ConversorCSV conversorCSV;


    public boolean agendarCita(String emailUsuario, String fecha, String horaInicio, String horaFin, String titulo) {
        return logica.agendarCita(emailUsuario, fecha, horaInicio, horaFin, titulo);
    }

    public Map<LocalDate, String> obtenerCitasEntreFechas(LocalDate inicio, LocalDate fin, String usuario) {
        Map<LocalDate, String> citasSemana = new HashMap<>();

        List<Cita> todasLasCitas = conversorCSV.cargarCitas();

        for (Cita cita : todasLasCitas) {

            if (cita.getTrabajadores().contains(usuario.trim())) {

                try {
                    String[] partes = cita.getHorario().split("T");
                    LocalDate fechaCita = LocalDate.parse(partes[0]);

                    if (!fechaCita.isBefore(inicio) && !fechaCita.isAfter(fin)) {

                        String tiempoStr = partes[1];
                        String horaInicio = tiempoStr.substring(0, 5);
                        String horaFin = tiempoStr.substring(tiempoStr.indexOf("-") + 1, tiempoStr.indexOf("-") + 6);

                        String descripcion = cita.getTitulo() + ", " + horaInicio + " - " + horaFin;

                        String citasPrevias = citasSemana.getOrDefault(fechaCita, "");
                        if (!citasPrevias.isEmpty()) citasPrevias += "<br>";

                        citasSemana.put(fechaCita, citasPrevias + descripcion);
                    }

                } catch (Exception e) {
                    System.err.println("Error procesando cita desde CSV: " + cita.getTitulo() + " - " + e.getMessage());
                }
            }
        }

        return citasSemana;
    }

    public void navegarAAgendarReunion(String usuario, String contrasena, String rol, JFrame ventanaActual) {
        cerrarVentanaActual(ventanaActual);
        SwingUtilities.invokeLater(() -> {
            new CalendarioAgendar(this, this, usuario, contrasena, rol).mostrar();
        });
    }

    public GestorAplicacion() {
            this.registroUsuarios = new Registrar();
            this.logueo = new Logueo(this.registroUsuarios);

            ManejadorConsola manejadorConsola = new ManejadorConsola();

            this.conversorCSV = new ConversorCSV();
            this.logica = new Logica(manejadorConsola, conversorCSV);
        }

    public String intentarLogin(String correo, String contrasena) {
        return logueo.obtenerRol(correo, contrasena);
    }

    public boolean registrarNuevoUsuario(String correo, String contrasena, String rol) {
        return registroUsuarios.guardarUsuario(correo, contrasena, rol);
    }

    public void navegarALogin() {
        SwingUtilities.invokeLater(() -> new VentanaLogin(this, this).mostrar());
    }

    public void navegarARegistro() {
        SwingUtilities.invokeLater(() -> new VentanaRegistro(this, this).mostrar());
    }

    public void navegarACalendarioVista(String usuario, String contrasena, String rol, JFrame ventanaActual) {

        cerrarVentanaActual(ventanaActual);

        CalendarioVista calendario = new CalendarioVista(usuario, contrasena, rol, this, this, this);
        calendario.mostrar();
    }

    public void navegarAVentanaJefe(String usuario, String contrasena, String rol, JFrame ventanaActual) {
        cerrarVentanaActual(ventanaActual);
        SwingUtilities.invokeLater(() -> {
            new VentanaJefe(this, this, usuario, contrasena, this).mostrar();
        });


    }

    public void navegarAVentanaTrabajador(String usuario, String contrasena, String rol, JFrame ventanaActual) {
        cerrarVentanaActual(ventanaActual);
        SwingUtilities.invokeLater(() -> {
            new VentanaTrabajador(this, this, usuario, contrasena).mostrar();
        });


    }

    public void cerrarVentanaActual(JFrame frameActual) {
        if (frameActual != null) {
            SwingUtilities.invokeLater(() -> frameActual.dispose());
        }

    }
}
