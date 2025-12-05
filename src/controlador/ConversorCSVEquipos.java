package controlador;

import java.io.*;
import java.util.*;

public class ConversorCSVEquipos {

    private static final String RUTA_ARCHIVO = "equipos.csv";

    public List<String> leerEquipo(String correoJefe) {
        List<String> equipo = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return equipo;

        try (Scanner sc = new Scanner(archivo)) {
            while (sc.hasNextLine()) {
                String linea = sc.nextLine();
                String[] partes = linea.split(";");

                if (partes.length >= 2 && partes[0].equals(correoJefe)) {
                    equipo.addAll(Arrays.asList(partes).subList(1, partes.length));
                }
            }
        } catch (Exception e) {
            System.err.println("Error leyendo equipos.csv");
        }

        return equipo;
    }

    public boolean guardarEquipo(String correoJefe, List<String> equipo) {
        List<String> lineas = leerTodasLasLineas();
        List<String> nuevasLineas = new ArrayList<>();

        boolean reemplazado = false;

        for (String linea : lineas) {
            if (linea.startsWith(correoJefe + ";")) {
                String nueva = correoJefe + ";" + String.join(";", equipo);
                nuevasLineas.add(nueva);
                reemplazado = true;
            } else {
                nuevasLineas.add(linea);
            }
        }

        if (!reemplazado) {
            nuevasLineas.add(correoJefe + ";" + String.join(";", equipo));
        }

        try (PrintWriter pw = new PrintWriter(RUTA_ARCHIVO)) {
            for (String l : nuevasLineas) pw.println(l);
        } catch (Exception e) {
            System.err.println("Error guardando equipo CSV");
            return false;
        }

        return true;
    }

    private List<String> leerTodasLasLineas() {
        List<String> lineas = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return lineas;

        try (Scanner sc = new Scanner(archivo)) {
            while (sc.hasNextLine()) lineas.add(sc.nextLine());
        } catch (Exception ignored) {}

        return lineas;
    }

    public boolean eliminarEquipo(String correoJefe) {
        List<String> lineas = leerTodasLasLineas();
        List<String> filtrado = new ArrayList<>();

        for (String linea : lineas) {
            if (!linea.startsWith(correoJefe + ";")) filtrado.add(linea);
        }

        try (PrintWriter pw = new PrintWriter(RUTA_ARCHIVO)) {
            for (String l : filtrado) pw.println(l);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
