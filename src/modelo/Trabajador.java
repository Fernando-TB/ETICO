package modelo;

import java.util.Objects;

public class Trabajador {

    private String nombre;
    private String correo;
    private String contra;
    private String rol;
    private Calendario calendario;

    public Trabajador(String nombre, String correo,String contra, String rol, Calendario calendario) {

        this.nombre = nombre;
        this.correo = correo;
        this.contra = contra;
        this.rol = rol;
        this.calendario = calendario;

    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getRol() {
        return rol;
    }

    public Calendario getCalendario(){
        return calendario;
    }

    public boolean verificarDatos(String correo, String contraseña){
        if (Objects.equals(correo, this.correo) && Objects.equals(contraseña,this.contra)){
            return true;
        }else {
            return false;
        }
    }

}