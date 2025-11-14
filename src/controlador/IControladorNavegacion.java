package controlador;

public interface IControladorNavegacion {
    void navegarALogin();
    void navegarARegistro();
    void navegarAVentanaJefe(String usuario, String contrasena);
    void navegarAVentanaTrabajador(String usuario, String contrasena);
    void navegarACalendarioVista(String usuario, String rol, String contrasena);
    void cerrarVentanaActual(javax.swing.JFrame frameActual);
}
