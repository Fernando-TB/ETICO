import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Logica {

    private final APILeerCalendar apiLeerCalendar;
    private final APIGemini apiGemini;
    private final APIEscribirCalendar apiEscribirCalendar;
    private final ManejadorConsola manejadorConsola;

    public Logica(APILeerCalendar apiLeerCalendar, APIGemini apiGemini, APIEscribirCalendar apiEscribirCalendar, ManejadorConsola manejadorConsola) {

        this.apiLeerCalendar = apiLeerCalendar;
        this.apiGemini = apiGemini;
        this.apiEscribirCalendar = apiEscribirCalendar;
        this.manejadorConsola = manejadorConsola;

    }

    public void agendarReunion(List<String> trabajadores, String titulo, int duracion) {

        manejadorConsola.imprimirMensaje("\n--- Iniciando el proceso para agendar la reunion: '" + titulo + "' ---");
        try {
            Map<String, Map<String, List<String>>> horariosDeTodos = new HashMap<>();
            for(String trabajador : trabajadores) {                          //Metodo obtenerHorariosOcupados probable cambio por metodo hecho por el conector de APIS
                Map<String, List<String>> horariosOcupados = apiLeerCalendar.obtenerHorariosOcupados(trabajador);
                horariosDeTodos.put(trabajador, horariosOcupados);
            }
            manejadorConsola.imprimirMensaje("--> Horarios ocupados de todos los participantes recopilados");

            String promptParaGemini = crearPromptParaGemini(horariosDeTodos, duracion);

                                        //Clase de apiGemini debe tener metodo que se llame analizarYEncontrarHorario
            String horarioSugerido = apiGemini.analizarYEncontrarHorario(promptParaGemini);
            manejadorConsola.imprimirMensaje("--> La IA ha sugerido el siguiente horario: " + horarioSugerido);

            for (String trabajador : trabajadores) {
                apiEscribirCalendar.crearEvento(trabajador, titulo, horarioSugerido);
            }
            manejadorConsola.imprimirMensaje("\n--- Reunion agendada con exito para todos los participantes. ---");

        } catch (Exception e) {
            manejadorConsola.imprimirMensaje("Error Ocurrio un problema al agendar la reunion: " + e.getMessage());
        }

    }
    private String crearPromptParaGemini(Map<String, Map<String, List<String>>> horarios, int duracion) {

        StringBuilder sb = new StringBuilder();
        sb.append("Analiza los siguientes horarios ocupados para un grupo de personas. ");
        sb.append("Necesito encontrar un bloque de tiempo libre de ").append(duracion).append(" minutos que sirva para todos.");
        sb.append("Responde SOLO con el horario de inicio y fin en formato ISO 8601 (YYYY-MM-DDTHH:MM:SS-HH:MM:SS), sin texto adicional. ");
        sb.append("Datos de horarios:\n").append(horarios.toString());
        return sb.toString();


    }

    }
