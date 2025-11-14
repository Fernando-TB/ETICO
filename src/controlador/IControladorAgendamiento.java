package controlador;

import java.util.List;

public interface IControladorAgendamiento {
    void solicitarAgendarReunion(List<String> trabajadores, String titulo, int duracion);
}
