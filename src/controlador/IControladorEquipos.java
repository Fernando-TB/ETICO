package controlador;

import java.util.List;

public interface IControladorEquipos {
    boolean agregarPersonaAEquipo(String correoJefe, String correoIntegrante);
    List<String> obtenerEquipo(String correoJefe);
}
