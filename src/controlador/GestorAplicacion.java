package controlador;

import java.time.DateTimeException;
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

public class GestorAplicacion implements IControladorAgendamiento, IControladorAutenticacion, IControladorNavegacion, IControladorCitas {

    private final Registrar registroUsuarios;
    private final Logueo logueo;
    private final Logica logica;

    private final Map<String, Map<Integer, String>> citasSimuladasPorUsuario;


    public void solicitarAgendarReunion(List<String> trabajadores, String titulo, int duracion) {
        logica.agendarReunion(trabajadores, titulo, duracion);
    }

    public Map<LocalDate, String> obtenerCitasParaMes(YearMonth mes, String usuario) {

        Map<LocalDate, String> citasDelMes = new HashMap<>();

        Map<Integer, String> citasDelUsuario = citasSimuladasPorUsuario.get(usuario);

        if (citasDelUsuario != null) {

            for (Map.Entry<Integer, String> entry : citasDelUsuario.entrySet()) {

                int diaDelMes = entry.getKey();
                String descripcion = entry.getValue();

                try {
                    if (mes.lengthOfMonth() >= diaDelMes) {
                        citasDelMes.put(mes.atDay(diaDelMes), descripcion);
                    }
                } catch (DateTimeException e) {
                    System.err.println("Error al obtener la fecha para el día: " + diaDelMes);
                }
            }
        }
        return citasDelMes;
    }



    public GestorAplicacion() {
            this.registroUsuarios = new Registrar();
            this.logueo = new Logueo(this.registroUsuarios);

            APIEscribirCalendar apiEscribirCalendar = new APIEscribirCalendar();
            APIGemini apiGemini = new APIGemini();
            APILeerCalendar apiLeerCalendar = new APILeerCalendar();
            ManejadorConsola manejadorConsola = new ManejadorConsola();
            this.logica = new Logica(apiLeerCalendar, apiGemini, apiEscribirCalendar, manejadorConsola);

            citasSimuladasPorUsuario = new HashMap<>();

            Map<Integer, String> citasJefe = new HashMap<>();
            citasJefe.put(6, "Reunión de Equipo, 10:00 - 11:00");
            citasJefe.put(20, "Preparar Presupuesto, 09:00 - 12:00");
            citasSimuladasPorUsuario.put("jefe@jefe.com", citasJefe);


            Map<Integer, String> citasTrabajador = new HashMap<>();
            citasTrabajador.put(15, "Terminar código, 14:00 - 16:00");
            citasSimuladasPorUsuario.put("trabajador@trabajador.com", citasTrabajador);

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
