package modelo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Registrar {

    private static final String RUTA_ARCHIVO = "usuarios.json";
    private final Map<String, Map<String, String>> baseDeDatosSimulada;

    public Registrar() {


        this.baseDeDatosSimulada = new HashMap<>();

        File archivo = new File(RUTA_ARCHIVO);
        boolean archivoExiste = archivo.exists();

        cargarUsuariosDesdeArchivo();


        if (!archivoExiste && baseDeDatosSimulada.isEmpty()) {
            Map<String, String> jefe = new HashMap<>();
            jefe.put("contrasena", "1234");
            jefe.put("rol", "Jefe");
            baseDeDatosSimulada.put("jefe@jefe.com", jefe);


            guardarUsuarioEnArchivo();
            System.out.println("Base de datos inicializada con usuario por defecto y guardada en disco.");
        } else if (archivoExiste && baseDeDatosSimulada.isEmpty()) {
            System.out.println("Archivo de usuarios no pudo ser leido, iniciando sin usuarios");
        } else  {
            System.out.println("Usuarios cargados correctamente del archivo");
        }
    }


    public Map<String, Map<String, String>> getBaseDeDatos() {
        return baseDeDatosSimulada;
    }


    public boolean guardarUsuario(String correo, String contrasena, String rol) {
        if (baseDeDatosSimulada.containsKey(correo)) {
            System.out.println("El correo ya existe en el sistema.");
            return false;
        }

        Map<String, String> datos = new HashMap<>();
        datos.put("contrasena", contrasena);
        datos.put("rol", rol);

        baseDeDatosSimulada.put(correo, datos);

        guardarUsuarioEnArchivo();

        System.out.println("Usuario registrado");
        return true;
    }
    private void guardarUsuarioEnArchivo() {
        try (PrintWriter writer = new PrintWriter(RUTA_ARCHIVO)) {

            for (Map.Entry<String, Map<String, String>> entry : baseDeDatosSimulada.entrySet()) {
                String correo = entry.getKey();
                String contrasena = entry.getValue().get("contrasena");
                String rol = entry.getValue().get("rol");

                writer.println(correo + ";" + contrasena + ";" + rol);
            }
            System.out.println("Datos guardados en " + RUTA_ARCHIVO);
        } catch (FileNotFoundException e) {
            System.err.println("No se pudo escribir en el archivo JSON.");
        }
    }

    private void cargarUsuariosDesdeArchivo() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return;

        try (Scanner scanner = new Scanner(archivo)) {
            baseDeDatosSimulada.clear();
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                if (linea.trim().isEmpty()) continue;

                String[] partes = linea.split(";", 3);

                if (partes.length == 3) {

                    String correo = partes[0].trim();
                    String contrasena = partes[1].trim();
                    String rol = partes[2].trim();

                    Map<String, String> datos = new HashMap<>();
                    datos.put("contrasena", contrasena);
                    datos.put("rol", rol);

                    baseDeDatosSimulada.put(correo, datos);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: No se pudo leer el archivo de usuarios.");
        }
    }



    }

