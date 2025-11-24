package launcher;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.FreeBusyResponse;
import com.google.api.services.calendar.model.TimePeriod;

import controlador.ConversorTXT;
import controlador.ConversorDisponibilidadTXT;
import modelo.APILeerCalendar;
import modelo.APIEscribirCalendar;
import modelo.PedirPermisosCalendar;
import modelo.APIConsultarDisponibilidad;
import modelo.BuscadorDeHuecosComunes;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;


public class Main {

    private static final String[] TRABAJADORES = {
            "fuentesgonzalo630@gmail.com",
            "j.pizarro02@ufromail.cl",
            "f.torres17@ufromail.cl"
    };

    private static final String FREEBUSY_OUTPUT_FILE = "disponibilidad_para_reunion.txt";


    public static void main(String... args) {

        for (String trabajador : TRABAJADORES) {
            try {
                System.out.printf("\n           INICIANDO TAREAS PARA %s          \n", trabajador);

                Calendar service = PedirPermisosCalendar.getCalendarService(trabajador);

                if (trabajador.equals(TRABAJADORES[0])) {
                    runFreeBusyModule(service);
                }


                runReadModule(service, trabajador);

                runWriteModule(service, trabajador);

            } catch (IOException | GeneralSecurityException e) {
                System.err.printf("\nERROR CRÍTICO CON EL TRABAJADOR %s. Revisar credenciales/permisos.\n", trabajador);
                System.err.println("Detalles: " + e.getMessage());
            } catch (ParseException e) {
                System.err.printf("\nERROR DE FORMATO DE FECHA para %s: %s\n", trabajador, e.getMessage());
            }
        }
        System.out.println("\n PROCESO COMPLETADO PARA TODOS LOS TRABAJADORES ");
    }

    private static void runFreeBusyModule(Calendar service) throws IOException {
        System.out.println("\n Iniciando consulta de disponibilidad para todos los trabajadores");

        List<String> workerEmails = Arrays.asList(TRABAJADORES);

        APIConsultarDisponibilidad apiConsulta = new APIConsultarDisponibilidad();
        FreeBusyResponse response = apiConsulta.consultarDisponibilidad(service, workerEmails);

        BuscadorDeHuecosComunes buscador = new BuscadorDeHuecosComunes();

        List<TimePeriod> huecosComunes = buscador.encontrarHuecosLibres(response, 60);

        ConversorDisponibilidadTXT conversor = new ConversorDisponibilidadTXT();
        conversor.guardarDisponibilidad(huecosComunes, FREEBUSY_OUTPUT_FILE);

        System.out.println("Tiempos guardados en " + FREEBUSY_OUTPUT_FILE);
    }


    private static void runReadModule(Calendar service, String trabajador) throws IOException {
        System.out.printf("\nDescargando eventos de Calendar para %s...\n", trabajador);

        APILeerCalendar apiReader = new APILeerCalendar();
        List<Event> items = apiReader.obtenerProximosEventos(service);

        String outputFileName = String.format("eventos_%s.txt", trabajador);

        ConversorTXT conversor = new ConversorTXT();
        conversor.guardarEventosEnTXT(items, outputFileName);
    }

    private static void runWriteModule(Calendar service, String trabajador) throws IOException, ParseException {
        System.out.printf("\n Creando evento en Calendar para %s...\n", trabajador);

        APIEscribirCalendar apiWriter = new APIEscribirCalendar();
        apiWriter.createEventFromFile(service, FREEBUSY_OUTPUT_FILE);

        System.out.printf("Evento de '%s' creado con éxito.\n", FREEBUSY_OUTPUT_FILE);
    }
}