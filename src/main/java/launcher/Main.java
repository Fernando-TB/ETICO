package launcher;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import controlador.ConversorCSV;
import modelo.APILeerCalendar;
import modelo.APIEscribirCalendar;
import modelo.PedirPermisosCalendar;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.List;


public class Main {


    private static final String EVENT_FILE_PATH = "evento.txt";

    public static void main(String... args) {
        try {
            System.out.println("   Iniciando la descarga de los datos");
            Calendar service = PedirPermisosCalendar.getCalendarService();
            runReadModule(service);
            runWriteModule(service);

        } catch (IOException | GeneralSecurityException e) {
            System.err.println("Asegúrate de que la API de Calendar esté habilitada y de haber autorizado la aplicación en el navegador.");
            System.err.println("Detalles: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("\n ERROR DE FORMATO DE FECHA: " + e.getMessage());
        }
    }

    private static void runReadModule(Calendar service) throws IOException {
        APILeerCalendar apiReader = new APILeerCalendar();
        List<Event> items = apiReader.obtenerProximosEventos(service);

        if (items.isEmpty()) {
            System.out.println("No se encontraron eventos próximos para exportar.");
            return;
        }

        ConversorCSV conversor = new ConversorCSV();
        conversor.guardarEventosEnCSV(items);
    }

    private static void runWriteModule(Calendar service) throws IOException, ParseException {
        APIEscribirCalendar apiWriter = new APIEscribirCalendar();
        apiWriter.createEventFromFile(service, EVENT_FILE_PATH);

    }
}