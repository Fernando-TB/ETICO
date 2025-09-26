import java.util.List;

public class Jefe extends Trabajador{

    public Jefe(int edad, String nombre, String correo, String rol, List<EventoCalendario> calendario) {
        super(edad, nombre, correo, rol, calendario);
    }
}
