
package controlador;

import modelo.Registrar;

import javax.swing.*;

public interface IControladorNavegacion {
    void navegarALogin();
    void navegarARegistro();
    void navegarAVentanaJefe(String usuario, String contrasena, String rol, JFrame ventanaActual);
    void navegarAVentanaTrabajador(String usuario, String contrasena, String rol, JFrame ventanaActual);
    void navegarACalendarioVista(String usuario, String contrasena, String rol, javax.swing.JFrame ventanaActual);
    void cerrarVentanaActual(javax.swing.JFrame frameActual);
    void navegarAAgregarEquipo(String correo, String contrasena, String rol, JFrame ventanaActual);
    void navegarAIniciarEvento(String usuario, String contrasena, String rol, JFrame ventanaActual);
}
