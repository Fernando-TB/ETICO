package controlador;

import com.google.api.services.calendar.model.Event;
import com.google.api.client.util.DateTime;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ConversorTXT {

    /**
     * Guarda la lista de eventos en un archivo de texto con un formato simplificado.
     * Incluye solo Título, Hora Inicio y Hora Fin.
     * @param eventos La lista de eventos de Google Calendar.
     * @param fileName El nombre del archivo de salida (ej: "eventos_Gonzalo_Fuentes.txt").
     */
    public void guardarEventosEnTXT(List<Event> eventos, String fileName) {
        if (eventos == null || eventos.isEmpty()) {
            System.out.printf("Advertencia: La lista de eventos para %s está vacía. No se creará el archivo.\n", fileName);
            return;
        }

        try (FileWriter fileWriter = new FileWriter(fileName);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            System.out.printf("Iniciando escritura de %d eventos en el archivo: %s\n",
                    eventos.size(), fileName);

            // Cabecera simplificada
            printWriter.println("Titulo | Hora Inicio | Hora Fin");

            for (Event evento : eventos) {
                String linea = formatEventLine(evento);
                printWriter.println(linea);
            }

            System.out.printf("Escritura completada con éxito. Archivo '%s' creado.\n", fileName);

        } catch (IOException e) {
            System.err.println("ERROR al escribir el archivo: " + e.getMessage());
        }
    }


    /**
     * Formatea un objeto Event para incluir solo el resumen (título), hora de inicio y hora de fin.
     */
    private String formatEventLine(Event evento) {
        String summary = cleanString(evento.getSummary());

        // Extrae y formatea las horas de inicio y fin
        String startTime = formatDateTime(evento.getStart() != null ? evento.getStart().getDateTime() : null);
        String endTime = formatDateTime(evento.getEnd() != null ? evento.getEnd().getDateTime() : null);

        // Retorna la línea con el formato simplificado: Título | Hora Inicio | Hora Fin
        return String.format("%s | %s | %s", summary, startTime, endTime);
    }


    private String formatDateTime(DateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }
        // Utiliza el formato estándar RFC3339 para la hora y fecha completa
        return dateTime.toStringRfc3339();
    }

    private String cleanString(String input) {
        if (input == null || input.isEmpty()) {
            return "N/A";
        }
        // Reemplaza saltos de línea para mantener el formato de línea única en el archivo de texto
        return input.replaceAll("[\\r\\n\\t]", " ").trim();
    }
}