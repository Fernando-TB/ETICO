package launcher;

import controlador.ManejadorConsola;
import modelo.*;
import vista.VentanaLogin;
import controlador.GestorAplicacion;
import controlador.IControladorAutenticacion;
import controlador.IControladorNavegacion;

import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        GestorAplicacion gestor = new GestorAplicacion();
        SwingUtilities.invokeLater(() -> {
            gestor.navegarALogin();
        });
    }
}