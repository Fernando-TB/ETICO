package launcher;

import controlador.ManejadorConsola;
import modelo.*;
import vista.VentanaLogin;

import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Registrar registroUsuarios = new Registrar();

        SwingUtilities.invokeLater(() -> {
            VentanaLogin login = new VentanaLogin(registroUsuarios);
            login.mostrar();

        });
    }

//    public static void main(String[] args) {
//
//        ManejadorConsola manejadorConsola = new ManejadorConsola();
//
//        APILeerCalendar apiLeerCalendar = new APILeerCalendar();
//        APIGemini apiGemini = new APIGemini();
//        APIEscribirCalendar apiEscribirCalendar = new APIEscribirCalendar();
//
//        Logica coordinadorDeLogica = new Logica(apiLeerCalendar, apiGemini, apiEscribirCalendar, manejadorConsola);
//
//
//        List<String> participantes = List.of("jefe@ejemplo.com", "trabajador1@ejemplo.com", "trabajador2@ejemplo.com");
//        String tituloReunion = "Reunion de Sincronizacion del Proyecto";
//        int duracionMinutos = 30;
//
//
//
//        coordinadorDeLogica.agendarReunion(participantes, tituloReunion, duracionMinutos);
//  }
}
