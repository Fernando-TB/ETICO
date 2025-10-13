package modelo;

import java.util.HashMap;
import java.util.Map;

public class Registrar {

    private final Map<String, Map<String, String>> baseDeDatosSimulada;

    public Registrar() {
        this.baseDeDatosSimulada = new HashMap<>();

        Map<String, String> jefe = new HashMap<>();
        jefe.put("contrasena", "1234");
        jefe.put("rol", "jefe");
        baseDeDatosSimulada.put("jefe@jefe.com", jefe);
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
}
