import java.util.ArrayList;
import java.util.List;

public class Trabajador {

    private int edad;
    private String nombre;
    private String correo;
    private String rol;
    private List<EventoCalendario> calendario;

    public Trabajador(int edad, String nombre, String correo, String rol, List<EventoCalendario> calendario) {

       this.edad = edad;
       this.nombre = nombre;
       this.correo = correo;
       this.rol = rol;
       this.calendario = calendario;

    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getRol() {
        return rol;
    }

    public List<EventoCalendario> getCalendario(){
        return calendario;
    }




}
