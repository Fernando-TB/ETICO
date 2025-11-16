package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Calendario {

    private List<EventoCalendario> lunes =  new ArrayList<>();
    private List<EventoCalendario> martes = new ArrayList<>();
    private List<EventoCalendario> miercoles = new ArrayList<>();
    private List<EventoCalendario> jueves = new ArrayList<>();
    private List<EventoCalendario> viernes = new ArrayList<>();

    public Calendario(List<EventoCalendario> eventos){

        boolean ultimoSolapaba = false;

        for(int i = 0; i < eventos.size() - 1; i++){

            EventoCalendario actual = eventos.get(i);
            EventoCalendario siguiente = eventos.get(i+1);

            if( solapaConSiguiente(actual,siguiente)){
                EventoCalendario eventoCombinado = juntarSolapadas(eventos.get(i),eventos.get(i+1));
                agregarDiaCorrespondiente(eventoCombinado);

                if(i + 1 == eventos.size() - 1){
                    ultimoSolapaba = true;
                }

            }else{
                agregarDiaCorrespondiente(actual);
            }

        }

        if(!ultimoSolapaba && !eventos.isEmpty()){
            agregarDiaCorrespondiente(eventos.getLast());
        }

    }

    public void agregarDiaCorrespondiente(EventoCalendario evento){

        switch (evento.getDia()){
            case "lunes" -> lunes.add(evento);
            case "martes" -> martes.add(evento);
            case "miercoles" -> miercoles.add(evento);
            case "jueves" -> jueves.add(evento);
            case "viernes" -> viernes.add(evento);
        }

    }

    public boolean solapaConSiguiente(EventoCalendario primer, EventoCalendario segundo){

        return Objects.equals(primer.getDia(),segundo.getDia()) &&
                Objects.equals(primer.getHoraFin(),segundo.getHoraInicio()) &&
                primer.getMinFin() > segundo.getMinInicio();
    }

    public EventoCalendario juntarSolapadas(EventoCalendario primerEvento, EventoCalendario segundoEvento){

        String dia = primerEvento.getDia();
        int horaInicio = primerEvento.getHoraInicio();
        int minInicio = primerEvento.getMinInicio();
        int horaFin = segundoEvento.getHoraFin();
        int minFin = segundoEvento.getMinFin();
        String nombreprimero = primerEvento.getNombreEvento();
        String nombresegundo = segundoEvento.getNombreEvento();

        EventoCalendario eventoCombinado = new EventoCalendario(dia,horaInicio,minInicio,horaFin,minFin,nombreprimero + nombresegundo);

        return eventoCombinado;

    }
}
