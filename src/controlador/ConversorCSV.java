package controlador;

import modelo.Cita;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



public class ConversorCSV {

    private static final String RUTA_ARCHIVO = "reuniones_agendadas.csv";

    public List<Cita> cargarCitas() {
        List<Cita> citas = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);

        if (!archivo.exists()) {
            return citas;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    try {
                        citas.add(Cita.fromCsvLine(linea));
                    } catch (Exception e) {
                        System.err.println("Error al parsear línea CSV: " + linea + " | " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
        return citas;
    }


    public void guardarNuevaCita(Cita nuevaCita) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            writer.println(nuevaCita.toCsvLine());
            System.out.println("Cita guardada en CSV: " + nuevaCita.getTitulo());
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo CSV: " + e.getMessage());
        }
    }
}