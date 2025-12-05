
package controlador;

import java.util.List;


public class ConversorJSON {


    public boolean existeUsuario(String emailBuscado) {
        try {
            List<String> lineas = java.nio.file.Files.readAllLines(
                    java.nio.file.Paths.get("usuarios.json")
            );

            for (String linea : lineas) {
                String[] datos = linea.split(";");
                if (datos.length >= 1) {
                    String email = datos[0].trim();
                    if (email.equalsIgnoreCase(emailBuscado.trim())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error leyendo usuarios.json: " + e.getMessage());
        }
        return false;
    }
}
