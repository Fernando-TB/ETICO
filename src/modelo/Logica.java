package modelo;

import controlador.ManejadorConsola;
import controlador.ConversorCSV;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Logica {


    private final APIEscribirCalendar apiEscribirCalendar;
    private final ManejadorConsola manejadorConsola;
    private final ConversorCSV conversorCSV;

    public Logica(ManejadorConsola manejadorConsola, ConversorCSV conversorCSV) {


        this.manejadorConsola = manejadorConsola;
        this.conversorCSV = new ConversorCSV();
        this.apiEscribirCalendar = new APIEscribirCalendar();

    }

    public void agendarCita(String emailUsuario, String fecha, String horaInicio, String horaFin, String titulo) {

        manejadorConsola.imprimirMensaje("\n--- Iniciando agendamiento de cita simple para: '" + emailUsuario + "' ---");
        try {

            String horarioCompleto = fecha + "T" + horaInicio + ":00-" + horaFin + ":00";


            apiEscribirCalendar.crearEvento(emailUsuario, titulo, horarioCompleto);


            Cita nuevaCita = new Cita(titulo, horarioCompleto, List.of(emailUsuario));
            conversorCSV.guardarNuevaCita(nuevaCita);

            manejadorConsola.imprimirMensaje("\n--- Cita agendada con exito para " + emailUsuario + ". Detalles: " + titulo + " en " + horarioCompleto + " y guardada en CSV. ---");
        } catch (Exception e) {
            manejadorConsola.imprimirMensaje("Error Ocurrio un problema al agendar la cita: " + e.getMessage());
        }
    }

}




