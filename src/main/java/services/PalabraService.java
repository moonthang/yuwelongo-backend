package services;

import logica.Palabra;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import java.util.List;

@Stateless
public class PalabraService {

    @PersistenceContext(unitName = "persistencia")
    private EntityManager em;

    public void crear(Palabra palabra) {
        em.persist(palabra);
    }

    public void editar(Palabra palabra) {
        em.merge(palabra);
    }

    public void eliminar(Palabra palabra) {
        em.remove(em.merge(palabra));
    }

    public Palabra buscarPorId(int id) {
        return em.find(Palabra.class, id);
    }

    public List<Palabra> listarTodas() {
        return em.createQuery("SELECT p FROM Palabra p", Palabra.class).getResultList();
    }

    public List<Palabra> buscarPorPalabraNasa(String texto) {
        return em.createQuery("SELECT p FROM Palabra p WHERE LOWER(p.palabraNasa) LIKE LOWER(:texto)", Palabra.class)
                .setParameter("texto", "%" + texto + "%")
                .getResultList();
    }

    public List<Palabra> buscarPorTraduccion(String texto) {
        return em.createQuery("SELECT p FROM Palabra p WHERE LOWER(p.traduccion) LIKE LOWER(:texto)", Palabra.class)
                .setParameter("texto", "%" + texto + "%")
                .getResultList();
    }

    public List<Palabra> obtenerPorCategoria(int idCategoria) {
        return em.createQuery(
                "SELECT p FROM Palabra p WHERE p.categoria.idCategoria = :idCategoria", Palabra.class)
                .setParameter("idCategoria", idCategoria)
                .getResultList();
    }
}