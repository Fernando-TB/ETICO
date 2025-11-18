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

    // CORREOS REALES
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

                // PedirPermisosCalendar gestiona tokens individuales para cada trabajador
                Calendar service = PedirPermisosCalendar.getCalendarService(trabajador);

                // === MODULO FREE/BUSY (Solo se ejecuta UNA vez, con el token del primer trabajador) ===
                if (trabajador.equals(TRABAJADORES[0])) {
                    runFreeBusyModule(service);
                }
                // ===================================================================================

                runReadModule(service, trabajador);

                // El módulo de escritura usa el primer hueco de 60 minutos guardado en el archivo común
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

    private static void runFreeBusyModule(Calendar service) throws IOException {
        System.out.println("\n[FREE/BUSY] Iniciando consulta de disponibilidad para todos los trabajadores...");

        List<String> workerEmails = Arrays.asList(TRABAJADORES);

        // 1. Consulta la API
        APIConsultarDisponibilidad apiConsulta = new APIConsultarDisponibilidad();
        FreeBusyResponse response = apiConsulta.consultarDisponibilidad(service, workerEmails);

        // 2. BUSCADOR: Encuentra los HUECOS LIBRES COMUNES
        BuscadorDeHuecosComunes buscador = new BuscadorDeHuecosComunes();

        // ¡AJUSTE CLAVE! Usamos 60 minutos (1 hora) como duración.
        List<TimePeriod> huecosComunes = buscador.encontrarHuecosLibres(response, 60);

        // 3. CONVERSOR: Convierte y guarda la lista de huecos LIBRES
        ConversorDisponibilidadTXT conversor = new ConversorDisponibilidadTXT();
        conversor.guardarDisponibilidad(huecosComunes, FREEBUSY_OUTPUT_FILE);

        System.out.println("[FREE/BUSY] Tiempos guardados en " + FREEBUSY_OUTPUT_FILE);
    }


    private static void runReadModule(Calendar service, String trabajador) throws IOException {
        System.out.printf("\n[LECTURA] Descargando eventos de Calendar para %s...\n", trabajador);

        APILeerCalendar apiReader = new APILeerCalendar();
        List<Event> items = apiReader.obtenerProximosEventos(service);

        String outputFileName = String.format("eventos_%s.txt", trabajador);

        ConversorTXT conversor = new ConversorTXT();
        conversor.guardarEventosEnTXT(items, outputFileName);
    }

    private static void runWriteModule(Calendar service, String trabajador) throws IOException, ParseException {
        System.out.printf("\n[ESCRITURA] Creando evento en Calendar para %s...\n", trabajador);

        APIEscribirCalendar apiWriter = new APIEscribirCalendar();
        // El archivo contiene el primer hueco de 60 minutos. La clase APIEscribirCalendar lee la primera línea.
        apiWriter.createEventFromFile(service, FREEBUSY_OUTPUT_FILE);

        System.out.printf("Evento de '%s' creado con éxito.\n", FREEBUSY_OUTPUT_FILE);
    }
}