package modelo;

import java.util.Map;

public class Logueo {

    private final Registrar registroUsuarios;

    public Logueo(Registrar registrousuarios) {
        this.registroUsuarios = registrousuarios;
    }

    public String obtenerRol(String correo, String contrasena) {

        Map<String, Map<String, String>> baseDeDatos = registroUsuarios.getBaseDeDatos();


        String correoLimpio = correo.trim();
        String contrasenaLimpia = contrasena.trim();

        if (baseDeDatos.containsKey(correoLimpio)) {
            Map<String, String> datos = baseDeDatos.get(correoLimpio);


            if (datos.get("contrasena").equals(contrasenaLimpia)) {
                System.out.println("Logueo: Login exitoso para " + correoLimpio);
                return datos.get("rol");
            }
        }
        System.out.println("Logueo: Fallo de credenciales para " + correo);
        return null;
    }

}

