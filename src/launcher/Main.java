package launcher;

import controlador.ManejadorConsola;
import modelo.*;
import vista.VentanaLogin;
import controlador.GestorAplicacion; // Importar el Gestor
import controlador.IControladorAutenticacion; // Importar las interfaces
import controlador.IControladorNavegacion;

import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        Registrar registroUsuarios = new Registrar();
        Logueo logueo = new Logueo(registroUsuarios);

        APIEscribirCalendar apiEscribirCalendar = new APIEscribirCalendar();
        APIGemini apiGemini = new APIGemini();
        APILeerCalendar apiLeerCalendar = new APILeerCalendar();
        ManejadorConsola manejadorConsola = new ManejadorConsola();

        Logica logica = new Logica(apiLeerCalendar, apiGemini, apiEscribirCalendar, manejadorConsola);

        GestorAplicacion gestor = new GestorAplicacion(registroUsuarios, logueo, logica);

        SwingUtilities.invokeLater(() -> {

            VentanaLogin login = new VentanaLogin(gestor, gestor);
            login.mostrar();
        });
    }
}