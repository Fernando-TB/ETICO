package controlador;

import java.util.List;

public interface IControladorAgendamiento {

    void agendarCita(String emailUsuario, String fecha, String horaInicio, String horaFin, String titulo);
}