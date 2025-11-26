
package controlador;

import java.util.List;

public interface IControladorAgendamiento {

    boolean agendarCita(String emailUsuario, String fecha, String horaInicio, String horaFin, String titulo);
    int convertirDuracionAMinutos(String duracion);
}
