package modelo;

import java.util.List;
import java.util.Arrays;

public class Cita {
    private final String titulo;
    private final String horario;
    private final List<String> trabajadores;

    public Cita(String titulo, String horario, List<String> trabajadores) {
        this.titulo = titulo;
        this.horario = horario;
        this.trabajadores = trabajadores;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getHorario() {
        return horario;
    }

    public List<String> getTrabajadores() {
        return trabajadores;
    }


    public String toCsvLine() {
        String participantes = String.join(",", trabajadores);
        return String.format("%s;%s;%s", titulo, horario, participantes);
    }

    public static Cita fromCsvLine(String csvLine) {
        String[] partes = csvLine.split(";");
        if (partes.length != 3) {
            throw new IllegalArgumentException("Línea CSV inválida para Cita: " + csvLine);
        }

        String titulo = partes[0].trim();
        String horario = partes[1].trim();

        List<String> trabajadores = Arrays.stream(partes[2].trim().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        return new Cita(titulo, horario, trabajadores);
    }
}