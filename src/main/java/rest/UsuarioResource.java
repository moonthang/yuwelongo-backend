package rest;

import logica.Usuario;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;
import java.time.LocalDate;
import java.util.List;
import services.UsuarioService;

@Stateless
public class UsuarioResource {

    @Inject
    private UsuarioService usuarioService;

    public boolean crearUsuario(Usuario u) {
        Usuario existente = usuarioService.buscarPorCorreo(u.getCorreo());
        if (existente != null) return false;

        String hash = BCrypt.hashpw(u.getContrasena(), BCrypt.gensalt());
        u.setContrasena(hash);

        u.setFechaRegistro(LocalDate.now().toString());
        u.setEstado("Activo");

        usuarioService.crear(u);
        return true;
    }

    public Usuario autenticar(String correo, String contrasena) {
        Usuario u = usuarioService.buscarPorCorreo(correo);
        if (u != null && BCrypt.checkpw(contrasena, u.getContrasena())) {
            return u;
        }
        return null;
    }

    public Usuario buscarPorCorreo(String correo) {
        return usuarioService.buscarPorCorreo(correo);
    }

    public Usuario buscarPorId(int id) {
        return usuarioService.buscarPorId(id);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioService.listarTodos();
    }

    public boolean actualizarUsuario(Usuario u) {
        Usuario existente = usuarioService.buscarPorId(u.getIdUsuario());
        if (existente == null) return false;

        if (u.getContrasena() != null && !u.getContrasena().isEmpty()) {
            String hash = BCrypt.hashpw(u.getContrasena(), BCrypt.gensalt());
            existente.setContrasena(hash);
        }

        existente.setNombre(u.getNombre());
        existente.setCorreo(u.getCorreo());
        existente.setRol(u.getRol());
        existente.setEstado(u.getEstado());

        usuarioService.editar(existente);
        return true;
    }

    public boolean eliminarUsuario(int id) {
        Usuario u = usuarioService.buscarPorId(id);
        if (u != null) {
            usuarioService.eliminar(u);
            return true;
        }
        return false;
    }
}