
package modelo;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.FreeBusyResponse;
import com.google.api.services.calendar.model.TimePeriod;

import java.util.*;
import java.util.stream.Collectors;

public class BuscadorDeHuecosComunes {


    public List<TimePeriod> encontrarHuecosLibres(FreeBusyResponse response, int durationMinutos) {

        List<TimePeriod> todosLosOcupados = new ArrayList<>();
        var calendars = response.getCalendars();

        if (calendars == null || calendars.isEmpty()) {
            return Collections.emptyList();
        }

        for (var calendarBusy : calendars.values()) {
            if (calendarBusy.getBusy() != null) {
                todosLosOcupados.addAll(calendarBusy.getBusy());
            }
        }

        long duracionMinMillis = (long) durationMinutos * 60 * 1000;

        if (todosLosOcupados.isEmpty()) {
            DateTime start = response.getTimeMin();
            long endMillis = start.getValue() + duracionMinMillis;
            DateTime end = new DateTime(endMillis);
            return List.of(new TimePeriod().setStart(start).setEnd(end));
        }

        List<TimePeriod> bloquesOcupadosFusionados = fusionarIntervalosOcupados(todosLosOcupados);
        List<TimePeriod> huecosLibres = calcularComplemento(bloquesOcupadosFusionados, response.getTimeMin(), response.getTimeMax());

        long duracionMaximaMillis = duracionMinMillis;

        List<TimePeriod> huecosValidos = new ArrayList<>();

        for (TimePeriod periodo : huecosLibres) {

            TimePeriod periodoCortado = cortarPeriodoPorHorarioLaboral(periodo);

            if (periodoCortado != null) {
                long duracionActual = periodoCortado.getEnd().getValue() - periodoCortado.getStart().getValue();

                if (duracionActual >= duracionMinMillis) {

                    long nuevoFinMillis = periodoCortado.getStart().getValue() + duracionMaximaMillis;

                    if (nuevoFinMillis > periodoCortado.getEnd().getValue()) {
                        nuevoFinMillis = periodoCortado.getEnd().getValue();
                    }

                    TimePeriod huecoFinal = new TimePeriod()
                            .setStart(periodoCortado.getStart())
                            .setEnd(new DateTime(nuevoFinMillis));

                    huecosValidos.add(huecoFinal);
                }
            }
        }

        return huecosValidos;
    }

    private TimePeriod cortarPeriodoPorHorarioLaboral(TimePeriod periodo) {

        Calendar calStart = Calendar.getInstance();
        calStart.setTimeInMillis(periodo.getStart().getValue());

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTimeInMillis(periodo.getEnd().getValue());

        int dayOfWeekStart = calStart.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeekStart < Calendar.MONDAY || dayOfWeekStart > Calendar.FRIDAY) {
            return null;
        }

        if (calStart.get(Calendar.HOUR_OF_DAY) < 6) {
            calStart.set(Calendar.HOUR_OF_DAY, 6);
            calStart.set(Calendar.MINUTE, 0);
            calStart.set(Calendar.SECOND, 0);
        }

        Calendar limiteFin = (Calendar) calEnd.clone();
        limiteFin.set(Calendar.HOUR_OF_DAY, 23);
        limiteFin.set(Calendar.MINUTE, 0);
        limiteFin.set(Calendar.SECOND, 0);

        if (calEnd.after(limiteFin)) {
            calEnd = limiteFin;
        }

        if (calEnd.before(calStart) || calEnd.equals(calStart)) {
            return null;
        }

        return new TimePeriod()
                .setStart(new DateTime(calStart.getTimeInMillis()))
                .setEnd(new DateTime(calEnd.getTimeInMillis()));
    }


    private List<TimePeriod> fusionarIntervalosOcupados(List<TimePeriod> ocupados) {
        if (ocupados.isEmpty()) return Collections.emptyList();

        ocupados.sort((p1, p2) -> Long.compare(p1.getStart().getValue(), p2.getStart().getValue()));

        List<TimePeriod> fusionados = new ArrayList<>();
        TimePeriod actual = ocupados.get(0);

        for (int i = 1; i < ocupados.size(); i++) {
            TimePeriod siguiente = ocupados.get(i);

            if (siguiente.getStart().getValue() <= actual.getEnd().getValue()) {
                if (siguiente.getEnd().getValue() > actual.getEnd().getValue()) {
                    actual.setEnd(siguiente.getEnd());
                }
            } else {
                fusionados.add(actual);
                actual = siguiente;
            }
        }
        fusionados.add(actual);
        return fusionados;
    }

    private List<TimePeriod> calcularComplemento(List<TimePeriod> bloquesOcupadosFusionados, DateTime timeMin, DateTime timeMax) {
        List<TimePeriod> libres = new ArrayList<>();

        long inicioDelRango = timeMin.getValue();
        long finDelRango = timeMax.getValue();

        for (TimePeriod ocupado : bloquesOcupadosFusionados) {
            long inicioOcupado = ocupado.getStart().getValue();
            long finOcupado = ocupado.getEnd().getValue();

            if (inicioDelRango < inicioOcupado) {
                libres.add(new TimePeriod().setStart(new DateTime(inicioDelRango)).setEnd(new DateTime(inicioOcupado)));
            }

            inicioDelRango = Math.max(inicioDelRango, finOcupado);
        }

        if (inicioDelRango < finDelRango) {
            libres.add(new TimePeriod().setStart(new DateTime(inicioDelRango)).setEnd(new DateTime(finDelRango)));
        }

        return libres;
    }
}
