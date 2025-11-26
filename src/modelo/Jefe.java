
package modelo;

import java.util.List;
import java.util.Objects;

public class Jefe extends Trabajador{

    private List<Trabajador> equipo;

    public Jefe(String nombre, String correo,String contra, String rol, Calendario calendario, List<Trabajador> equipo) {
        super(nombre, correo, contra, rol, calendario);

        this.equipo = equipo;
    }

    public void agregarMiembro(Trabajador miembroNuevo){

        if(miembroEnEquipo(miembroNuevo)){
            System.out.println("Miembro ya en equipo");
        }else {
            this.equipo.add(miembroNuevo);
        }
    }

    public boolean miembroEnEquipo(Trabajador miembro){
        for(Trabajador persona : this.equipo){
            if(Objects.equals(miembro.getCorreo(), persona.getCorreo())){
                return true;
            }
        }
        return false;
    }

}
