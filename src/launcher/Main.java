package launcher;

import controlador.*;
import modelo.*;
import javax.swing.*;


public class Main {

    public static void main(String[] args) {
        GestorAplicacion gestor = new GestorAplicacion();
        SwingUtilities.invokeLater(() -> {
            gestor.navegarALogin();
        });
    }
}
