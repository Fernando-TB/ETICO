package modelo;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.FreeBusyResponse;
import com.google.api.services.calendar.model.TimePeriod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BuscadorDeHuecosComunes {

    /**
     * Calcula los bloques de tiempo en los que TODOS los calendarios están libres.
     * Si un hueco es más largo que la duración mínima, lo recorta a la duración exacta (2 horas).
     * @param response La respuesta FreeBusyResponse con los tiempos ocupados de todos.
     * @param durationMinutos La duración mínima/exacta que debe tener el hueco libre (120 minutos).
     * @return Una lista de TimePeriod que representan los tiempos libres comunes.
     */
    public List<TimePeriod> encontrarHuecosLibres(FreeBusyResponse response, int durationMinutos) {

        // 1. Recolectar TODOS los tiempos ocupados
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

        // Si nadie está ocupado en el rango, todo el rango es libre (caso límite)
        if (todosLosOcupados.isEmpty()) {
            return List.of(new TimePeriod().setStart(response.getTimeMin()).setEnd(response.getTimeMax()));
        }

        // 2. Simplificar y fusionar los bloques de tiempo ocupado
        List<TimePeriod> bloquesOcupadosFusionados = fusionarIntervalosOcupados(todosLosOcupados);

        // 3. Determinar los bloques LIBRES (el complemento)
        List<TimePeriod> huecosLibres = calcularComplemento(bloquesOcupadosFusionados, response.getTimeMin(), response.getTimeMax());

        // 4. Filtrar por duración mínima y CORTAR al tamaño de la reunión (2 horas)
        long duracionMinMillis = (long) durationMinutos * 60 * 1000;
        long duracionMaximaMillis = duracionMinMillis; // 2 horas exactas

        List<TimePeriod> huecosValidos = huecosLibres.stream()
                // Filtrar huecos que duren al menos 2 horas
                .filter(period -> (period.getEnd().getValue() - period.getStart().getValue()) >= duracionMinMillis)
                .map(period -> {
                    // Cortar el hueco a un máximo de 2 horas desde el inicio
                    long nuevoFinMillis = period.getStart().getValue() + duracionMaximaMillis;

                    if (nuevoFinMillis < period.getEnd().getValue()) {
                        // Si es más largo, ajustamos el fin para que dure solo 2 horas
                        return period.setEnd(new DateTime(nuevoFinMillis));
                    }
                    // Si es de 2 horas o menos, lo dejamos como está
                    return period;
                })
                .collect(Collectors.toList());

        return huecosValidos;
    }

    // --- Métodos de Algoritmo de Intervalos (Se mantienen iguales) ---

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