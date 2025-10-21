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
import java.util.Scanner;

public class Main {

    public static void main(String... args) {
        try {
            System.out.println("   Iniciando la descarga de los datos");

            Calendar service = PedirPermisosCalendar.getCalendarService();

            runReadModule(service);

            runWriteModule(service);

        } catch (IOException | GeneralSecurityException e) {
            System.err.println("\n ERROR FATAL DE CONEXIÓN O AUTENTICACIÓN:");
            System.err.println("Asegúrate de que la API de Calendar esté habilitada y de haber autorizado la aplicación en el navegador.");
            System.err.println("Detalles: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("\n ERROR DE FORMATO DE FECHA: " + e.getMessage());
        }
    }

    private static void runReadModule(Calendar service) throws IOException {
        APILeerCalendar apiReader = new APILeerCalendar();

        System.out.println("\n==================================================");
        System.out.println("      MÓDULO DE LECTURA (Eventos Lunes-Sábado)     ");
        System.out.println("==================================================");

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

        System.out.print("¿Deseas crear un nuevo evento en tu Google Calendar? (s/n): ");

        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("s")) {
            apiWriter.createEventFromConsole(service);
        } else {
            System.out.println("Finalizando aplicación. ¡Hasta pronto!");
        }
    }
}
