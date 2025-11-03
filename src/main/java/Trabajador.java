import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Trabajador {

    private int edad;
    private String nombre;
    private String correo;
    private String contraseña;
    private String rol;
    private List<EventoCalendario> calendario;

    public Trabajador(int edad, String nombre, String correo,String contraseña, String rol, List<EventoCalendario> calendario) {

       this.edad = edad;
       this.nombre = nombre;
       this.correo = correo;
       this.contraseña = contraseña;
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

    public boolean verificarDatos(String correo, String contraseña){
        if (Objects.equals(correo, this.correo) && Objects.equals(contraseña,this.contraseña)){
            return true;
        }else {
            return false;
        }
    }




}
