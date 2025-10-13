package launcher;

import controlador.ManejadorConsola;
import modelo.*;
import vista.VentanaLogin;

import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Registrar registroUsuarios = new Registrar();

        Logueo logueo = new Logueo(registroUsuarios);

        SwingUtilities.invokeLater(() -> {
            VentanaLogin login = new VentanaLogin(registroUsuarios, logueo);
            login.mostrar();

        });
    }
}