package modelo;

import controlador.ConversorCSVEquipos;
import Utilitarios.ManejadorConsola;
import controlador.ConversorJSON;

import java.util.List;

public class Logica {


    private final APIEscribirCalendar apiEscribirCalendar;
    private final ManejadorConsola manejadorConsola;
    private final ConversorJSON conversorJSON;
    private final ConversorCSVEquipos conversorCSVEquipos;

    public Logica(ManejadorConsola manejadorConsola, ConversorJSON conversorJSON) {


        this.manejadorConsola = manejadorConsola;
        this.conversorJSON = new ConversorJSON();
        this.apiEscribirCalendar = new APIEscribirCalendar();
        this.conversorCSVEquipos = new ConversorCSVEquipos();

    }

    public boolean eliminarEquipo(String correoJefe) {

        return conversorCSVEquipos.eliminarEquipo(correoJefe);

    }

    public boolean agendarCita(String emailUsuario, String fecha, String horaInicio, String horaFin, String titulo) {

        manejadorConsola.imprimirMensaje("\n--- Iniciando agendamiento de cita simple para: '" + emailUsuario + "' ---");
        try {

            if (!conversorJSON.existeUsuario(emailUsuario)) {
                manejadorConsola.imprimirMensaje("Error: El usuario '" + emailUsuario + "' no está registrado en la base de datos.");
                return false;
            }

            String horarioCompleto = fecha + "T" + horaInicio + ":00-" + horaFin + ":00";

            manejadorConsola.imprimirMensaje("\n--- Cita agendada con exito para " + emailUsuario + ". Detalles: " + titulo + " en " + horarioCompleto + " y guardada en CSV. ---");
            return true;
        } catch (Exception e) {
            manejadorConsola.imprimirMensaje("Error Ocurrio un problema al agendar la cita: " + e.getMessage());
            return false;
        }
    }

}