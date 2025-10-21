package controlador;//a

import com.google.api.services.calendar.model.Event;
import com.google.api.client.util.DateTime;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ConversorCSV {

    private static final String NOMBRE_ARCHIVO_SALIDA = "eventos_filtrados.txt";

    public void guardarEventosEnCSV(List<Event> eventos) {
        if (eventos == null || eventos.isEmpty()) {
            System.out.println("Advertencia: La lista de eventos está vacía. No se creará el archivo.");
            return;
        }

        try (FileWriter fileWriter = new FileWriter(NOMBRE_ARCHIVO_SALIDA);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            System.out.printf("\nIniciando escritura de %d eventos en el archivo: %s\n",
                    eventos.size(), NOMBRE_ARCHIVO_SALIDA);

            printWriter.println("Titulo | Hora Inicio | Hora Fin | Creador| Descripción ");

            for (Event evento : eventos) {
                String linea = formatEventLine(evento);
                printWriter.println(linea);
            }

            System.out.println("Escritura completada con éxito. Archivo creado.");

        } catch (IOException e) {
            System.err.println("ERROR al escribir el archivo: " + e.getMessage());
        }
    }


    private String formatEventLine(Event evento) {
        String summary = cleanString(evento.getSummary());
        String description = cleanString(evento.getDescription());
        String creator = cleanString(evento.getCreator() != null ? evento.getCreator().getEmail() : "Desconocido");

        String startTime = formatDateTime(evento.getStart() != null ? evento.getStart().getDateTime() : null);
        String endTime = formatDateTime(evento.getEnd() != null ? evento.getEnd().getDateTime() : null);

        return String.format("%s | %s | %s | %s | %s",
                summary, startTime, endTime, creator, description);
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
        return input.replaceAll("\\r\\n|\\r|\\n", " ").trim();
    }
}
