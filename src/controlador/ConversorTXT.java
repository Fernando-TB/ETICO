package controlador;

import com.google.api.services.calendar.model.Event;
import com.google.api.client.util.DateTime;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ConversorTXT {

    public void guardarEventosEnTXT(List<Event> eventos, String fileName) {
        if (eventos == null || eventos.isEmpty()) {
            System.out.printf("Advertencia: La lista de eventos para %s está vacía. No se creará el archivo.\n", fileName);
            return;
        }

        try (FileWriter fileWriter = new FileWriter(fileName);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            System.out.printf("Iniciando escritura de %d eventos en el archivo: %s\n",
                    eventos.size(), fileName);

            for (Event evento : eventos) {
                String linea = formatEventLine(evento);
                printWriter.println(linea);
            }

            System.out.printf("Escritura completada con éxito. Archivo '%s' creado.\n", fileName);

        } catch (IOException e) {
            System.err.println("ERROR al escribir el archivo: " + e.getMessage());
        }
    }


    private String formatEventLine(Event evento) {
        String summary = cleanString(evento.getSummary());

        String startTime = formatDateTime(evento.getStart() != null ? evento.getStart().getDateTime() : null);
        String endTime = formatDateTime(evento.getEnd() != null ? evento.getEnd().getDateTime() : null);

        return String.format("%s | %s | %s", summary, startTime, endTime);
    }


    private String formatDateTime(DateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }
        return dateTime.toStringRfc3339();
    }

    private String cleanString(String input) {
        if (input == null || input.isEmpty()) {
            return "N/A";
        }
        return input.replaceAll("[\\r\\n\\t]", " ").trim();
    }
}