package modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APILeerCalendar {
    public Map<String, List<String>> obtenerHorariosOcupados(String userEmail) {
        System.out.println("Simulación: Obteniendo horarios ocupados para " + userEmail);
        Map<String, List<String>> ocupados = new HashMap<>();
        ocupados.put("2025-10-25", List.of("10:00-11:00", "14:00-15:00"));
        return ocupados;
    }
}
