package launcher;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import controlador.ConversorTXT;
import modelo.APILeerCalendar;
import modelo.APIEscribirCalendar;
import modelo.PedirPermisosCalendar;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.List;


public class Main {

    private static final String[] TRABAJADORES = {
            "Gonzalo_Fuentes",
            "Joaquin_Pizarro",
            "Fernando_Torres"
    };

    private static final String EVENT_FILE_PATH = "evento.txt";

    public static void main(String... args) {
        for (String trabajador : TRABAJADORES) {
            try {
                System.out.printf("\n           INICIANDO TAREAS PARA %s          \n", trabajador);

                Calendar service = PedirPermisosCalendar.getCalendarService(trabajador);

                runReadModule(service, trabajador);

                runWriteModule(service, trabajador);

            } catch (IOException | GeneralSecurityException e) {
                System.err.printf("\nERROR CRÍTICO CON EL TRABAJADOR %s. Revisar credenciales/permisos.\n", trabajador);
                System.err.println("Detalles: " + e.getMessage());
            } catch (ParseException e) {
                System.err.printf("\nERROR DE FORMATO DE FECHA para %s: %s\n", trabajador, e.getMessage());
            }
        }
        System.out.println("\n--- PROCESO COMPLETADO PARA TODOS LOS TRABAJADORES ---");
    }

    private static void runReadModule(Calendar service, String trabajador) throws IOException {
        System.out.printf("\n[LECTURA] Descargando eventos de Calendar para %s...\n", trabajador);

        APILeerCalendar apiReader = new APILeerCalendar();
        List<Event> items = apiReader.obtenerProximosEventos(service);

        String outputFileName = String.format("eventos_%s.txt", trabajador);

        ConversorTXT conversor = new ConversorTXT();
        // ¡Llamada al método actualizada!
        conversor.guardarEventosEnTXT(items, outputFileName);
    }

    private static void runWriteModule(Calendar service, String trabajador) throws IOException, ParseException {
        System.out.printf("\n[ESCRITURA] Creando evento en Calendar para %s...\n", trabajador);

        APIEscribirCalendar apiWriter = new APIEscribirCalendar();
        apiWriter.createEventFromFile(service, EVENT_FILE_PATH);

        System.out.printf("Evento de '%s' creado con éxito.\n", EVENT_FILE_PATH);
    }
}