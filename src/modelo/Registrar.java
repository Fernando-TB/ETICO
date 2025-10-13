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

        cargarUsuariosDesdeArchivo();

        if (baseDeDatosSimulada.isEmpty()) {

            Map<String, String> jefe = new HashMap<>();
            jefe.put("contrasena", "1234");
            jefe.put("rol", "jefe");
            baseDeDatosSimulada.put("jefe@jefe.com", jefe);
            System.out.println("Base de datos iniciada por defecto");
        }

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

    public String obtenerRol(String correo, String contrasena) {
        if (baseDeDatosSimulada.containsKey(correo)) {
            Map<String, String> datos = baseDeDatosSimulada.get(correo);

            if (datos.get("contrasena").equals(contrasena)) {
                System.out.println("Login exitoso");
                return datos.get("rol");
            }
        }
        System.out.println("Fallo de logueo");
        return null;
    }

    private void guardarUsuarioEnArchivo() {
        try (PrintWriter writer = new PrintWriter(RUTA_ARCHIVO)) {

            writer.print(baseDeDatosSimulada.toString());
            System.out.println("Datos guardados en " + RUTA_ARCHIVO);
        } catch (FileNotFoundException e) {
            System.err.println("No se pudo escribir en el archivo JSON.");
        }
    }

    private void cargarUsuariosDesdeArchivo() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) {
            System.out.println("Persistencia: Archivo JSON no encontrado. Iniciando vacío.");
            return;
        }

        try (Scanner scanner = new Scanner(archivo)) {
            // Leer la única línea del archivo
            String contenido = scanner.nextLine();

            if (contenido.isEmpty() || contenido.equals("{}")) return;


            String limpio = contenido.substring(1, contenido.length() - 1);
            String[] entradas = limpio.split(",\\s*");

            for (String entrada : entradas) {
                if (entrada.contains("=")) {
                    String[] partes = entrada.split("=", 2); // Separar correo y datos
                    String correo = partes[0].trim();
                    String datosString = partes[1].trim();


                    Map<String, String> datos = new HashMap<>();
                    String limpioDatos = datosString.substring(1, datosString.length() - 1);
                    String[] pares = limpioDatos.split(",\\s*");

                    for (String par : pares) {
                        String[] kv = par.split("=", 2);
                        if (kv.length == 2) {
                            datos.put(kv[0].trim(), kv[1].trim());
                        }
                    }
                    baseDeDatosSimulada.put(correo, datos);
                }
            }
            System.out.println("Usuarios cargados desde " + RUTA_ARCHIVO);

        } catch (FileNotFoundException e) {
            System.err.println("No se pudo leer el archivo JSON.");
        } catch (Exception e) {
            System.err.println("Falló la reconstrucción de datos del archivo. Ignorando datos.");
            baseDeDatosSimulada.clear();
        }
    }



    }

