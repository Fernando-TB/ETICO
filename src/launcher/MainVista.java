package launcher;

import controlador.*;
import modelo.*;
import vista.VentanaLogin;

import javax.swing.*;
import java.util.List;

public class MainVista {

    public static void main(String[] args) {
        GestorAplicacion gestor = new GestorAplicacion();
        SwingUtilities.invokeLater(() -> {
            gestor.navegarALogin();
        });
    }
}


