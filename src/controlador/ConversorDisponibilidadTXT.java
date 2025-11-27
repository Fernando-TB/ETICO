package controlador;

import com.google.api.services.calendar.model.TimePeriod;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ConversorDisponibilidadTXT {


    public void guardarDisponibilidad(List<TimePeriod> huecosComunes, String fileName,String nombre) throws IOException {

        if (huecosComunes == null || huecosComunes.isEmpty()) {
            String mensajeError = "ERROR_NODISPONIBLE: No se encontró ningún hueco libre " +
                    "de la duración requerida";

            System.err.println(mensajeError);
            throw new RuntimeException(mensajeError);
        }

        try (FileWriter fileWriter = new FileWriter(fileName);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            printWriter.println("NOMBRE | INICIO | FIN");

            TimePeriod primerHueco = huecosComunes.get(0);

            String tituloReunion = nombre;

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