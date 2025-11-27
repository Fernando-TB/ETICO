package modelo;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.FreeBusyResponse;
import com.google.api.services.calendar.model.TimePeriod;
import controlador.ConversorDisponibilidadTXT;
import controlador.ConversorTXT;
import vista.VentanaAvisoError;
import javax.swing.SwingUtilities;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;

import java.util.List;


public class APICalendar {

    private static final String FREEBUSY_OUTPUT_FILE = "disponibilidad_para_reunion.txt";

    private static List<String> TRABAJADORES;

    private static String nombre;
    private static boolean freeBusySuccess = false;

    public void setTRABAJADORES(List<String> TRABAJADORES) {
        APICalendar.TRABAJADORES = TRABAJADORES;
    }

    public void setNombreEvento(String nombreEvento) {
        APICalendar.nombre = nombreEvento;
    }

    public void emitirEvento(int duracion){

        for (String trabajador : TRABAJADORES) {
            try {
                System.out.printf("\n           INICIANDO TAREAS PARA %s          \n", trabajador);

                Calendar service = PedirPermisosCalendar.getCalendarService(trabajador);

                if (trabajador.equals(TRABAJADORES.get(0))) {
                    runFreeBusyModule(service, duracion);
                }

                if (APICalendar.freeBusySuccess) {
                    runReadModule(service, trabajador);
                    runWriteModule(service, trabajador);
                } else if (!trabajador.equals(TRABAJADORES.get(0))) {
                    System.out.printf("\n%s: No se procede a la lectura/escritura porque la búsqueda de huecos falló.\n", trabajador);
                }

            } catch (IOException | GeneralSecurityException e) {
                System.err.printf("\nERROR CRÍTICO CON EL TRABAJADOR %s. Revisar credenciales/permisos.\n", trabajador);
                System.err.println("Detalles: " + e.getMessage());
            } catch (ParseException e) {
                System.err.printf("\nERROR DE FORMATO DE FECHA para %s: %s\n", trabajador, e.getMessage());
            } catch (RuntimeException e) {
                if (e.getMessage().contains("ERROR_NODISPONIBLE")) {


                    SwingUtilities.invokeLater(() -> {
                        VentanaAvisoError aviso = new VentanaAvisoError(null, e.getMessage().replace("ERROR_NODISPONIBLE: ", ""));
                        aviso.mostrar();
                    });

                    break;
                }
            }
        }
        System.out.println("\n PROCESO COMPLETADO PARA TODOS LOS TRABAJADORES ");
    }

    private static void runFreeBusyModule(Calendar service, int duracion) throws IOException {
        System.out.println("\n Iniciando consulta de disponibilidad para todos los trabajadores");

        APIConsultarDisponibilidad apiConsulta = new APIConsultarDisponibilidad();

        FreeBusyResponse response = apiConsulta.consultarDisponibilidad(service, TRABAJADORES);

        BuscadorDeHuecosComunes buscador = new BuscadorDeHuecosComunes();

        List<TimePeriod> huecosComunes = buscador.encontrarHuecosLibres(response, duracion);

        try {
            ConversorDisponibilidadTXT conversor = new ConversorDisponibilidadTXT();
            conversor.guardarDisponibilidad(huecosComunes, FREEBUSY_OUTPUT_FILE, nombre);
            APICalendar.freeBusySuccess = true;
            System.out.println("Tiempos guardados en " + FREEBUSY_OUTPUT_FILE);

        } catch (RuntimeException e) {
            APICalendar.freeBusySuccess = false;
            throw e;
        }
    }

    public static void runReadModule(Calendar service, String trabajador) throws IOException {
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