package modelo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TXTtoCalendario {

    public Calendario TXTaCalendario(String ruta) throws IOException {

        List<String> lineas = Files.readAllLines(Paths.get(ruta));

        List<EventoCalendario> eventos = new ArrayList<>();

        for(String linea : lineas){

            EventoCalendario event = lineToEvento(linea);

            eventos.add(event);

        }

        Calendario calendario = new Calendario(eventos);

        return calendario;

    }

    public EventoCalendario lineToEvento(String linea){

        String[] sep = linea.split("\\|");

        String Titulo = sep[0];

        String diahorasInicio = sep[1];

        String[] diashorasInicio = diahorasInicio.split("T");

        String diaInicio = diashorasInicio[0];

        String hrminzonaInicio = diashorasInicio[1];

        String[] momentoInicio = hrminzonaInicio.split("\\.");

        String hrminsInicio = momentoInicio[0];

        String[] hr_min_sInicio = hrminsInicio.split(":");

        String hrInicio = hr_min_sInicio[0];

        String minInicio = hr_min_sInicio[1];

        String diahorasFin = sep[2];

        String[] diashorasFin = diahorasFin.split("T");

        String hrminzonaFin = diashorasFin[1];

        String[] momentoFin = hrminzonaFin.split("\\.");

        String hrminsFin = momentoFin[0];

        String[] hr_min_sFin = hrminsFin.split(":");

        String hrFin = hr_min_sFin[0];

        String minFin = hr_min_sFin[1];

        int horaInicio = Integer.parseInt(hrInicio);
        int horaFin = Integer.parseInt(hrFin);
        int mnInicio = Integer.parseInt(minInicio);
        int mnFin = Integer.parseInt(minFin);

        return new EventoCalendario(diaInicio,horaInicio,mnInicio,horaFin,mnFin,Titulo);

    }

}
