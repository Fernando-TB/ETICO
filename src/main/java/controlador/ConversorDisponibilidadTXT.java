package controlador;

import com.google.api.services.calendar.model.TimePeriod;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ConversorDisponibilidadTXT {

    /**
     * Procesa la lista de huecos libres comunes (TimePeriod) y los guarda en un archivo TXT.
     * Si la lista está vacía, no crea el archivo.
     * @param huecosComunes La lista de TimePeriod (huecos libres comunes) del Buscador.
     * @param fileName El nombre del archivo de salida.
     */
    public void guardarDisponibilidad(List<TimePeriod> huecosComunes, String fileName) throws IOException {

        // 1. Manejar el caso de lista vacía
        if (huecosComunes == null || huecosComunes.isEmpty()) {
            System.out.println("[CONVERSOR] Advertencia: No se encontraron huecos libres comunes. No se creará el archivo.");
            return;
        }

        try (FileWriter fileWriter = new FileWriter(fileName);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            // 2. Encabezado
            printWriter.println("NOMBRE REUNION | INICIO | FIN");

            // 3. Escribir solo el primer hueco libre (el más cercano)
            TimePeriod primerHueco = huecosComunes.get(0);

            // Título para el evento a crear
            String tituloReunion = "Reunion Equipo";

            // Formato RFC3339 (Ejemplo: 2025-12-04T14:00:00.000Z)
            String start = primerHueco.getStart().toStringRfc3339();
            String end = primerHueco.getEnd().toStringRfc3339();

            printWriter.printf("%s | %s | %s\n", tituloReunion, start, end);

            System.out.printf("[CONVERSOR] Escrito el primer hueco libre en el archivo: %s\n", fileName);

        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de disponibilidad: " + e.getMessage());
            throw e;
        }
    }
}