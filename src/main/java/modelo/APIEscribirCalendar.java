package modelo;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.List;
import java.util.ArrayList;

public class APIEscribirCalendar {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    private static final SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat(DATE_TIME_FORMAT);

    public void createEventFromFile(Calendar service, String filePath) throws IOException, ParseException {
        System.out.println("\n Creacion oblitaria del evento.");

        List<String> eventData = readEventDataFromFile(filePath);

        if (eventData.size() < 3) {
            System.err.println("Error de archivo: El archivo debe contener al menos 3 líneas (Título, Inicio, Fin) en el formato correcto.");
            return;
        }

        String summary = eventData.get(0);
        String startDateTimeStr = eventData.get(1);
        String endDateTimeStr = eventData.get(2);

        System.out.println("Datos leídos del archivo:");
        System.out.println("- Título: " + summary);
        System.out.println("- Inicio: " + startDateTimeStr);
        System.out.println("- Fin:    " + endDateTimeStr);

        createEvent(service, summary, startDateTimeStr, endDateTimeStr);
    }

    private void createEvent(Calendar service, String summary, String startDateTimeStr, String endDateTimeStr) throws IOException, ParseException {
        Date startDate = FULL_DATE_FORMAT.parse(startDateTimeStr);
        Date endDate = FULL_DATE_FORMAT.parse(endDateTimeStr);

        if (endDate.before(startDate)) {
            System.err.println("Error: La hora de fin es anterior a la hora de inicio. No se creará el evento.");
            return;
        }

        Event event = new Event().setSummary(summary);
        String timeZoneId = TimeZone.getDefault().getID();

        DateTime start = new DateTime(startDate);
        event.setStart(new EventDateTime().setDateTime(start).setTimeZone(timeZoneId));

        DateTime end = new DateTime(endDate);
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
            String line;
            while ((line = reader.readLine()) != null && lines.size() < 3) {
                if (!line.trim().isEmpty()) {
                    lines.add(line.trim());
                }
            }
        }
        return lines;
    }
}