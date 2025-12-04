package controlador;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConversorCSVEquipos {

    private static final String RUTA_ARCHIVO = "equipos.csv";


    private List<String> leerTodasLasLineas() {
        List<String> lineas = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) {
            return lineas;
        }

        try (Scanner scanner = new Scanner(archivo)) {
            while (scanner.hasNextLine()) {
                lineas.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error al leer equipos.csv: " + e.getMessage());
        }
        return lineas;
    }


    private void sobrescribirArchivo(List<String> lineas) throws IOException {
        try (PrintWriter writer = new PrintWriter(RUTA_ARCHIVO)) {
            for (String linea : lineas) {
                writer.println(linea);
            }
        }
    }


    public boolean eliminarEquipo(String correoJefe) {

        List<String> lineasOriginales = leerTodasLasLineas();
        List<String> lineasConservadas = new ArrayList<>();
        boolean equipoEliminado = false;


        String prefijoJefe = correoJefe + ";";

        for (String linea : lineasOriginales) {
            if (linea.startsWith(prefijoJefe)) {

                equipoEliminado = true;
            } else {

                lineasConservadas.add(linea);
            }
        }

        if (!equipoEliminado) {
            System.out.println("No se encontró un equipo asociado a " + correoJefe);
            return true;
        }

        try {
            sobrescribirArchivo(lineasConservadas);
            System.out.println("Equipo de " + correoJefe + " eliminado con éxito. Lineas restantes: " + lineasConservadas.size());
            return true;
        } catch (IOException e) {
            System.err.println("Error al sobrescribir equipos.csv: " + e.getMessage());
            return false;
        }
    }

}