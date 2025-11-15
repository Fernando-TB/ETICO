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


    public void solicitarAgendarReunion(List<String> trabajadores, String titulo, int duracion) {
        logica.agendarReunion(trabajadores, titulo, duracion);
    }

    public Map<LocalDate, String> obtenerCitasParaMes(YearMonth mes, String usuario) {

        Map<LocalDate, String> citas = new HashMap<>();

        try{
            if (mes.lengthOfMonth() >= 6){
                citas.put(mes.atDay(6), "Reunión de Equipo, 10:00 - 11:00");
            }
            if (mes.lengthOfMonth() >= 15){
                citas.put(mes.atDay(15), "Terminar código, 14:00");
            }
        } catch (DateTimeException e) {

        }
        return citas;
    }

    public GestorAplicacion() {
        this.registroUsuarios = new Registrar();
        this.logueo = new Logueo(this.registroUsuarios);

        APIEscribirCalendar apiEscribirCalendar = new APIEscribirCalendar();
        APIGemini apiGemini = new APIGemini();
        APILeerCalendar apiLeerCalendar = new APILeerCalendar();
        ManejadorConsola manejadorConsola = new ManejadorConsola();


        this.logica = new Logica(apiLeerCalendar, apiGemini, apiEscribirCalendar, manejadorConsola);
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
