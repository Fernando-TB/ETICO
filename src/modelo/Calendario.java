

package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Calendario {

    private List<EventoCalendario> calendario =  new ArrayList<>();

    public Calendario(List<EventoCalendario> eventos){

        boolean ultimoSolapaba = false;

        for(int i = 0; i < eventos.size() - 1; i++){

            EventoCalendario actual = eventos.get(i);
            EventoCalendario siguiente = eventos.get(i+1);

            if( solapaConSiguiente(actual,siguiente)){

                EventoCalendario eventoCombinado = juntarSolapadas(eventos.get(i),eventos.get(i+1));

                calendario.add(eventoCombinado);

                if(i + 1 == eventos.size() - 1){
                    ultimoSolapaba = true;
                }

            }else{
                calendario.add(actual);
            }

        }

        if(!ultimoSolapaba && !eventos.isEmpty()){
            calendario.add(eventos.getLast());
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

    public List<EventoCalendario> getCalendario() {
        return calendario;
    }
}
