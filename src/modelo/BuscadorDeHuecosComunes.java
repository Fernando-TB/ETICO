package modelo;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.FreeBusyResponse;
import com.google.api.services.calendar.model.TimePeriod;

import java.util.*;

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

        List<TimePeriod> segmentosFiltrados = new ArrayList<>();

        for (TimePeriod periodoCompleto : huecosLibres) {
            List<TimePeriod> segmentosDiarios = segmentarPeriodoPorReglaLaboral(periodoCompleto, duracionMinMillis);
            segmentosFiltrados.addAll(segmentosDiarios);
        }

        long duracionMaximaMillis = duracionMinMillis;
        List<TimePeriod> huecosFinales = new ArrayList<>();

        for (TimePeriod periodoCortado : segmentosFiltrados) {

            long duracionActual = periodoCortado.getEnd().getValue() - periodoCortado.getStart().getValue();

            if (duracionActual >= duracionMinMillis) {

                long nuevoFinMillis = periodoCortado.getStart().getValue() + duracionMaximaMillis;

                if (nuevoFinMillis > periodoCortado.getEnd().getValue()) {
                    nuevoFinMillis = periodoCortado.getEnd().getValue();
                }

                TimePeriod huecoFinal = new TimePeriod()
                        .setStart(periodoCortado.getStart())
                        .setEnd(new DateTime(nuevoFinMillis));

                huecosFinales.add(huecoFinal);
            }
        }

        return huecosFinales.stream().findFirst().map(List::of).orElse(Collections.emptyList());
    }


    private List<TimePeriod> segmentarPeriodoPorReglaLaboral(TimePeriod periodoCompleto, long duracionMinMillis) {

        List<TimePeriod> segmentosValidos = new ArrayList<>();

        Calendar calIter = Calendar.getInstance();
        calIter.setTimeInMillis(periodoCompleto.getStart().getValue());

        long finCompleto = periodoCompleto.getEnd().getValue();

        while (calIter.getTimeInMillis() < finCompleto) {

            Calendar calDiaStart = (Calendar) calIter.clone();
            calDiaStart.set(Calendar.HOUR_OF_DAY, 6);
            calDiaStart.set(Calendar.MINUTE, 0);
            calDiaStart.set(Calendar.SECOND, 0);
            calDiaStart.set(Calendar.MILLISECOND, 0);

            Calendar calDiaEnd = (Calendar) calIter.clone();
            calDiaEnd.set(Calendar.HOUR_OF_DAY, 23);
            calDiaEnd.set(Calendar.MINUTE, 0);
            calDiaEnd.set(Calendar.SECOND, 0);
            calDiaEnd.set(Calendar.MILLISECOND, 0);

            int dayOfWeek = calIter.get(Calendar.DAY_OF_WEEK);

            if (dayOfWeek >= Calendar.MONDAY && dayOfWeek <= Calendar.FRIDAY) {

                long segmentoStart = Math.max(calDiaStart.getTimeInMillis(), periodoCompleto.getStart().getValue());
                long segmentoEnd = Math.min(calDiaEnd.getTimeInMillis(), finCompleto);

                if (segmentoEnd - segmentoStart >= duracionMinMillis) {

                    TimePeriod segmento = new TimePeriod()
                            .setStart(new DateTime(segmentoStart))
                            .setEnd(new DateTime(segmentoEnd));

                    segmentosValidos.add(segmento);
                }
            }

            calIter.add(Calendar.DAY_OF_YEAR, 1);
            calIter.set(Calendar.HOUR_OF_DAY, 0);
        }

        return segmentosValidos;
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