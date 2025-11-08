package services;

import logica.Categoria;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import java.util.List;

@Stateless
public class CategoriaService {

    @PersistenceContext(unitName = "persistencia")
    private EntityManager em;

    public void crear(Categoria categoria) {
        em.persist(categoria);
    }

    public void editar(Categoria categoria) {
        em.merge(categoria);
    }

    public void eliminar(Categoria categoria) {
        em.remove(em.merge(categoria));
    }

    public Categoria buscarPorId(int id) {
        return em.find(Categoria.class, id);
    }

    public List<Categoria> listarTodas() {
        return em.createQuery("SELECT c FROM Categoria c", Categoria.class).getResultList();
    }

    public List<Categoria> buscarPorNombre(String nombre) {
        return em.createQuery("SELECT c FROM Categoria c WHERE LOWER(c.nombre) LIKE LOWER(:nombre)", Categoria.class)
                .setParameter("nombre", "%" + nombre + "%")
                .getResultList();
    }
}