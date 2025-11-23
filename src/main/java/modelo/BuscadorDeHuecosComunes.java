package modelo;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.FreeBusyResponse;
import com.google.api.services.calendar.model.TimePeriod;

import java.util.*;
import java.util.stream.Collectors;

public class BuscadorDeHuecosComunes {

    /**
     * Calcula los bloques de tiempo en los que TODOS los calendarios están libres,
     * limitando la búsqueda a Lunes-Viernes, 6 AM a 11 PM.
     * @param response La respuesta FreeBusyResponse con los tiempos ocupados de todos.
     * @param durationMinutos La duración exacta de la reunión (60 minutos).
     * @return Una lista de TimePeriod que representan los tiempos libres comunes que cumplen la restricción.
     */
    public List<TimePeriod> encontrarHuecosLibres(FreeBusyResponse response, int durationMinutos) {

        // ... (Pasos 1 a 3: Recolección y Fusión de Ocupados) ...

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

        // Manejo de caso límite si no hay eventos ocupados
        if (todosLosOcupados.isEmpty()) {
            DateTime start = response.getTimeMin();
            long endMillis = start.getValue() + duracionMinMillis;
            DateTime end = new DateTime(endMillis);
            return List.of(new TimePeriod().setStart(start).setEnd(end));
        }

        List<TimePeriod> bloquesOcupadosFusionados = fusionarIntervalosOcupados(todosLosOcupados);
        List<TimePeriod> huecosLibres = calcularComplemento(bloquesOcupadosFusionados, response.getTimeMin(), response.getTimeMax());

        // 4. APLICAR FILTRO DE DÍA Y HORA (Automático)
        long duracionMaximaMillis = duracionMinMillis;

        List<TimePeriod> huecosValidos = new ArrayList<>();

        for (TimePeriod periodo : huecosLibres) {

            // 4a. Definir el rango de búsqueda diario (Lunes 6:00 AM a Viernes 11:00 PM)
            TimePeriod periodoCortado = cortarPeriodoPorHorarioLaboral(periodo);

            if (periodoCortado != null) {
                long duracionActual = periodoCortado.getEnd().getValue() - periodoCortado.getStart().getValue();

                // 4b. Aplicar el filtro de duración (60 minutos) y cortar
                if (duracionActual >= duracionMinMillis) {

                    long nuevoFinMillis = periodoCortado.getStart().getValue() + duracionMaximaMillis;

                    // Asegurar que no excedemos el final del periodo cortado
                    if (nuevoFinMillis > periodoCortado.getEnd().getValue()) {
                        nuevoFinMillis = periodoCortado.getEnd().getValue();
                    }

                    // Crear el hueco final con el inicio original y el nuevo fin de 60 minutos
                    TimePeriod huecoFinal = new TimePeriod()
                            .setStart(periodoCortado.getStart())
                            .setEnd(new DateTime(nuevoFinMillis));

                    huecosValidos.add(huecoFinal);
                }
            }
        }

        return huecosValidos;
    }


    /**
     * Aplica la restricción de Lunes-Viernes, 6 AM - 11 PM a un TimePeriod dado.
     * Si el periodo se extiende más allá del horario laboral, lo corta.
     */
    private TimePeriod cortarPeriodoPorHorarioLaboral(TimePeriod periodo) {

        // Usamos el calendario de Java para analizar días de la semana y horas locales
        Calendar calStart = Calendar.getInstance();
        calStart.setTimeInMillis(periodo.getStart().getValue());

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTimeInMillis(periodo.getEnd().getValue());

        int dayOfWeekStart = calStart.get(Calendar.DAY_OF_WEEK);

        // Lunes (2) a Viernes (6)
        if (dayOfWeekStart < Calendar.MONDAY || dayOfWeekStart > Calendar.FRIDAY) {
            return null; // Ignorar fines de semana
        }

        // 1. Establecer hora de inicio mínima (6:00 AM)
        if (calStart.get(Calendar.HOUR_OF_DAY) < 6) {
            calStart.set(Calendar.HOUR_OF_DAY, 6);
            calStart.set(Calendar.MINUTE, 0);
            calStart.set(Calendar.SECOND, 0);
        }

        // 2. Establecer hora de fin máxima (11:00 PM / 23:00)
        Calendar limiteFin = (Calendar) calEnd.clone();
        limiteFin.set(Calendar.HOUR_OF_DAY, 23);
        limiteFin.set(Calendar.MINUTE, 0);
        limiteFin.set(Calendar.SECOND, 0);

        // Cortar el final si se extiende más allá de las 11 PM del día actual.
        if (calEnd.after(limiteFin)) {
            calEnd = limiteFin;
        }

        // Si el periodo final es válido (final es posterior o igual al inicio, después del ajuste)
        if (calEnd.before(calStart) || calEnd.equals(calStart)) {
            return null;
        }

        // Devolver el periodo de tiempo cortado
        return new TimePeriod()
                .setStart(new DateTime(calStart.getTimeInMillis()))
                .setEnd(new DateTime(calEnd.getTimeInMillis()));
    }

    // --- fusionarIntervalosOcupados y calcularComplemento se mantienen iguales ---

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