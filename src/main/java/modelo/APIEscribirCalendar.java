package modelo;//a

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;
public class
APIEscribirCalendar {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    public void createEventFromConsole(Calendar service) throws IOException, ParseException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("     CREACIÓN DE NUEVO EVENTO EN EL CALENDARIO    ");

        System.out.print("1. Introduce el Título/Asunto del evento: ");
        String summary = scanner.nextLine();


        System.out.printf("4. Introduce FECHA y HORA de INICIO (Formato: %s, ej: 2025-10-25 15:30): ", DATE_TIME_FORMAT);
        String startDateTimeStr = scanner.nextLine();

        System.out.printf("5. Introduce FECHA y HORA de FIN (Formato: %s, ej: 2025-10-25 16:30): ", DATE_TIME_FORMAT);
        String endDateTimeStr = scanner.nextLine();

        SimpleDateFormat fullDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        Date startDate, endDate;

        startDate = fullDateFormat.parse(startDateTimeStr);
        endDate = fullDateFormat.parse(endDateTimeStr);

        if (endDate.before(startDate)) {
            System.err.println("Error: La hora de fin es anterior a la hora de inicio. No se creará el evento.");
            return;
        }

        Event event = new Event()
                .setSummary(summary);

        String timeZoneId = TimeZone.getDefault().getID();

        DateTime start = new DateTime(startDate);
        event.setStart(new EventDateTime().setDateTime(start).setTimeZone(timeZoneId));

        DateTime end = new DateTime(endDate);
        event.setEnd(new EventDateTime().setDateTime(end).setTimeZone(timeZoneId));

        System.out.println("\nIntentando crear el evento en Google Calendar");

        String calendarId = "primary";
        event = service.events().insert(calendarId, event).execute();

        System.out.println(" ¡Evento creado con éxito!");
        System.out.println("Título: " + event.getSummary());
        System.out.println("Link: " + event.getHtmlLink());
    }
}
