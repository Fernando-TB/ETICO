package controlador;

import java.util.List;
import modelo.*;
import vista.*;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GestorAplicacion implements IControladorAgendamiento, IControladorAutenticacion, IControladorNavegacion {

    private final Registrar registroUsuarios;
    private final Logueo logueo;
    private final Logica logica;

    public void solicitarAgendarReunion(List<String> trabajadores, String titulo, int duracion) {
        logica.agendarReunion(trabajadores, titulo, duracion);
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

    public void navegarAVentanaJefe(String usuario, String contrasena) {
        SwingUtilities.invokeLater(() -> new VentanaJefe(usuario, this, this).mostrar());
    }

    public void navegarAVentanaTrabajador(String usuario, String contrasena) {

        SwingUtilities.invokeLater(() -> new VentanaTrabajador(this, this, usuario, contrasena).mostrar());
    }

    public void navegarACalendarioVista(String usuario, String contrasena, String rol) {

        SwingUtilities.invokeLater(() -> new CalendarioVista(usuario, contrasena, rol, this, this).mostrar());
    }

    public void cerrarVentanaActual(JFrame frameActual) {
        if (frameActual != null) {
            SwingUtilities.invokeLater(() -> frameActual.dispose());
        }

    }
}
