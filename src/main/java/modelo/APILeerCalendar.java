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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

public class APILeerCalendar {

    public List<Event> obtenerProximosEventos(Calendar client) throws IOException {
        System.out.println("Descargando datos desde GoogleCalendar");

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zoneId);

        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        LocalDate endOfNextWeek = startOfWeek.plusDays(12);

        ZonedDateTime startZoned = ZonedDateTime.of(startOfWeek, LocalTime.MIN, zoneId);
        DateTime timeMin = new DateTime(startZoned.toInstant().toEpochMilli());

        ZonedDateTime endZoned = ZonedDateTime.of(endOfNextWeek, LocalTime.MAX, zoneId);
        DateTime timeMax = new DateTime(endZoned.toInstant().toEpochMilli());


        Events events = client.events().list("primary")
                .setTimeMin(timeMin)
                .setTimeMax(timeMax)
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

                    ZonedDateTime eventZonedTime = ZonedDateTime.ofInstant(
                            java.time.Instant.ofEpochMilli(startTimeMillis), localZone);

                    DayOfWeek dayOfWeek = eventZonedTime.getDayOfWeek();

                    return dayOfWeek.getValue() >= DayOfWeek.MONDAY.getValue() &&
                            dayOfWeek.getValue() <= DayOfWeek.SATURDAY.getValue();
                })
                .collect(Collectors.toList());

        System.out.printf("Descargados %d eventos, %d filtrados de Lunes a Sábado.\n", listaEventos.size(), eventosFiltrados.size());

        return eventosFiltrados;
    }
}