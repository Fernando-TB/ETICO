package modelo;

import java.util.List;

public class Cita {
    private final String titulo;
    private final String horario;
    private final int duracion;
    private final List<String> trabajadores;

    public Cita(String titulo, String horario, int duracion, List<String> trabajadores) {
        this.titulo = titulo;
        this.horario = horario;
        this.duracion = duracion;
        this.trabajadores = trabajadores;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getHorario() {
        return horario;
    }

    public int getDuracion() {
        return duracion;
    }

    public List<String> getTrabajadores() {
        return trabajadores;
    }


    public String toCsvLine() {
        String participantes = String.join(",", trabajadores);
        return String.format("%s;%s;%d;%s", titulo, horario, duracion, participantes);
    }

    public static Cita fromCsvLine(String csvLine) {
        String[] partes = csvLine.split(";");
        if (partes.length != 4) {
            throw new IllegalArgumentException("Línea CSV inválida para Cita: " + csvLine);
        }

        String titulo = partes[0].trim();
        String horario = partes[1].trim();
        int duracion = Integer.parseInt(partes[2].trim());

        List<String> trabajadores = List.of(partes[3].split(","));

        return new Cita(titulo, horario, duracion, trabajadores);
    }
}