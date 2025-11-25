package controlador;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;


public interface IControladorCitas {

    Map<LocalDate, String> obtenerCitasEntreFechas(LocalDate inicio, LocalDate fin, String usuario);



}
