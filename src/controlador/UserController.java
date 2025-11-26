package controlador;

import modelo.Trabajador;

import java.util.List;
import java.util.Objects;

public class UserController {

    private List<Trabajador> usuarios;

    public List<Trabajador> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Trabajador> usuarios) {
        this.usuarios = usuarios;
    }

    public void agregarUsuario(Trabajador usuario){
        this.usuarios.add(usuario);
    }

    public boolean usuarioRegistrado(Trabajador usuario){
        for(Trabajador user : this.usuarios){
            if(Objects.equals(usuario.getCorreo(),user.getCorreo())){
                return true;
            }
        }
        return false;
    }
}
