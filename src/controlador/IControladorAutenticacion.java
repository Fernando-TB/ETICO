package controlador;

public interface IControladorAutenticacion {

    String intentarLogin(String correo, String contrasena);
    boolean registrarNuevoUsuario(String correo, String contrasena, String rol);

}
