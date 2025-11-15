package controlador;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;


public interface IControladorCitas {

    Map<LocalDate, String> obtenerCitasParaMes(YearMonth mes, String usuario);


}
