
package controlador;

import java.util.List;

public interface IControladorAgendamiento {

    int convertirDuracionAMinutos(String duracion);

    boolean agendarCita(String correoStr, String fechaStr, String horaInicioStr, String horaFinStr, String titulo);
}
