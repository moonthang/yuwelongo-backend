package services;

import logica.Usuario;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import java.util.List;

@Stateless
public class UsuarioService {

    @PersistenceContext(unitName = "persistencia")
    private EntityManager em;

    public void crear(Usuario usuario) {
        em.persist(usuario);
    }

    public void editar(Usuario usuario) {
        em.merge(usuario);
    }

    public void eliminar(Usuario usuario) {
        em.remove(em.merge(usuario));
    }

    public Usuario buscarPorId(int id) {
        return em.find(Usuario.class, id);
    }

    public Usuario buscarPorCorreo(String correo) {
        try {
            return em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.correo = :correo", Usuario.class)
                    .setParameter("correo", correo)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Usuario buscarPorRefreshToken(String token) {
        try {
            return em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.refreshToken = :token", Usuario.class)
                    .setParameter("token", token)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Usuario> listarTodos() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }
}