package modelo; // Paquete simple

import com.google.api.services.calendar.model.Event;
import com.google.api.client.util.DateTime;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Se encarga de formatear una lista de eventos y escribirlos en un archivo de texto.
 * NOTA: Esta clase asume la responsabilidad de la escritura de archivos.
 */
public class ConversorCSV {

    /**
     * Convierte la lista de eventos al formato deseado y la escribe en el archivo.
     * @param eventos Lista de eventos de Google Calendar.
     * @param nombreArchivo Nombre del archivo de salida (e.g., "eventos.txt").
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    public void escribirEventosAArchivo(List<Event> eventos, String nombreArchivo) throws IOException {

        // Utilizamos try-with-resources para asegurar que PrintWriter se cierre automáticamente
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {

            // Escribir el encabezado del archivo
            writer.println("Título del Evento | Fecha de Inicio | Fecha de Fin | Descripción | Estado");
            writer.println("--------------------------------------------------------------------------------------------------------------------------------------");

            for (Event evento : eventos) {
                // Obtener datos del evento
                String summary = evento.getSummary() != null ? evento.getSummary() : "(Sin Título)";
                String description = evento.getDescription() != null ? evento.getDescription().replace("\n", " ").trim() : "(Sin Descripción)";
                String status = evento.getStatus() != null ? evento.getStatus() : "No especificado";

                // Formato de fecha/hora (usa el objeto DateTime de Google para obtener las estampas)
                DateTime startDateTime = evento.getStart() != null ? evento.getStart().getDateTime() : null;
                DateTime endDateTime = evento.getEnd() != null ? evento.getEnd().getDateTime() : null;

                String fechaInicio = startDateTime != null ? startDateTime.toStringRfc3339() : "(Fecha no especificada)";
                String fechaFin = endDateTime != null ? endDateTime.toStringRfc3339() : "(Fecha no especificada)";

                // Crear la línea en el formato solicitado (separador '|' para simular CSV/separación)
                String linea = String.format("%s | %s | %s | %s | %s",
                        summary,
                        fechaInicio,
                        fechaFin,
                        description,
                        status);

                writer.println(linea);
            }

            System.out.println("Conversión y escritura completada en el disco.");
        }
    }
}
