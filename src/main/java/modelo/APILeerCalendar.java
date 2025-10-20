package modelo;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.DayOfWeek;

public class APILeerCalendar {

    public List<Event> obtenerProximosEventos(Calendar client) throws IOException {
        System.out.println("Descargando datos desde GoogleCalendar");

        DateTime now = new DateTime(System.currentTimeMillis());

        Events events = client.events().list("primary")
                .setMaxResults(200)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        List<Event> listaEventos = events.getItems();

        if (listaEventos == null || listaEventos.isEmpty()) {
            System.out.println("No se encontraron mas datos");
            return Collections.emptyList();
        }

        ZoneId localZone = ZoneId.systemDefault();

        List<Event> eventosFiltrados = listaEventos.stream()
                .filter(evento -> {
                    if (evento.getStart() == null || evento.getStart().getDateTime() == null) {
                        return false;
                    }

                    long startTimeMillis = evento.getStart().getDateTime().getValue();
                    ZonedDateTime startZoned = ZonedDateTime.ofInstant(
                            java.time.Instant.ofEpochMilli(startTimeMillis), localZone);

                    DayOfWeek dayOfWeek = startZoned.getDayOfWeek();

                    return dayOfWeek.getValue() >= DayOfWeek.MONDAY.getValue() &&
                            dayOfWeek.getValue() <= DayOfWeek.SATURDAY.getValue();
                })
                .collect(Collectors.toList());

        System.out.printf("", eventosFiltrados.size(), listaEventos.size());

        return eventosFiltrados;
    }
}
 //a