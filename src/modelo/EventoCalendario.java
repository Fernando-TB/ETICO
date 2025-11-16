package modelo;

public class EventoCalendario {

    private String dia;
    private int horaInicio;
    private int minInicio;
    private int horaFin;
    private int minFin;
    private String nombreEvento;

    public EventoCalendario(String dia, int horaInicio, int minInicio, int horaFin, int minFin, String nombreEvento){
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.minInicio = minInicio;
        this.horaFin = horaFin;
        this.minFin = minFin;
        this.nombreEvento = nombreEvento;
    }

    public String getDia() {
        return dia;
    }

    public int getMinInicio() {
        return minInicio;
    }

    public int getMinFin() {
        return minFin;
    }
    public int getHoraInicio(){
        return horaInicio;
    }
    public int getHoraFin(){
        return horaFin;
    }
    public String getNombreEvento(){
        return nombreEvento;
    }
}
