package modelo;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.TimeZone;
import java.util.List;
import java.util.ArrayList;

public class APIEscribirCalendar {

    // Eliminamos SimpleDateFormat ya que usaremos DateTime nativo.

    public void createEventFromFile(Calendar service, String filePath) throws IOException, ParseException {
        System.out.println("\n Creacion oblitaria del evento.");

        List<String> eventData = readEventDataFromFile(filePath);

        if (eventData.isEmpty() || eventData.size() < 3) {
            System.err.println("Advertencia: No hay datos válidos para crear el evento en el archivo: " + filePath);
            return;
        }

        String summary = eventData.get(0);
        String startDateTimeStr = eventData.get(1);
        String endDateTimeStr = eventData.get(2);

        System.out.println("Datos leídos del archivo:");
        System.out.println("- Título: " + summary);
        System.out.println("- Inicio: " + startDateTimeStr);
        System.out.println("- Fin:    " + endDateTimeStr);

        // ¡LLAMADA AL MÉTODO CORREGIDO!
        createEvent(service, summary, startDateTimeStr, endDateTimeStr);
    }

    // Método Modificado: Ya no usa SimpleDateFormat ni Date
    private void createEvent(Calendar service, String summary, String startDateTimeStr, String endDateTimeStr) throws IOException {

        // 1. Convertir la cadena RFC 3339 directamente a objeto DateTime de Google API
        DateTime start = new DateTime(startDateTimeStr);
        DateTime end = new DateTime(endDateTimeStr);

        // 2. Comprobación de orden (opcional, pero buena práctica)
        if (end.getValue() < start.getValue()) {
            System.err.println("Error: La hora de fin es anterior a la hora de inicio. No se creará el evento.");
            return;
        }

        Event event = new Event().setSummary(summary);
        String timeZoneId = TimeZone.getDefault().getID();

        // 3. Establecer el inicio y fin del evento usando los objetos DateTime
        event.setStart(new EventDateTime().setDateTime(start).setTimeZone(timeZoneId));
        event.setEnd(new EventDateTime().setDateTime(end).setTimeZone(timeZoneId));

        System.out.println("\nCreando el evento.");

        String calendarId = "primary";
        event = service.events().insert(calendarId, event).execute();

        System.out.println(" ¡Evento creado con éxito!");
        System.out.println("Título: " + event.getSummary());
        System.out.println("Link: " + event.getHtmlLink());
    }

    private List<String> readEventDataFromFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine(); // Ignorar la cabecera

            String line;
            if ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    lines.add(parts[0].trim()); // Título
                    lines.add(parts[1].trim()); // Inicio (RFC3339)
                    lines.add(parts[2].trim()); // Fin (RFC3339)
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("ADVERTENCIA: Archivo de datos de evento '" + filePath + "' no encontrado. Saltando la creación del evento.");
            return Collections.emptyList();
        }
        return lines;
    }
}