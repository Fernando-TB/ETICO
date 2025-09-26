public class EventoCalendario {

    private String horaInicio;
    private String horaFin;
    private String nombreEvento;

    public EventoCalendario(String horaInicio, String horaFin, String nombreEvento){
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.nombreEvento = nombreEvento;
    }


    public String getHoraInicio(){
        return horaInicio;
    }
    public String getHoraFin(){
        return horaFin;
    }
    public String getNombreEvento(){
        return nombreEvento;
    }
}
