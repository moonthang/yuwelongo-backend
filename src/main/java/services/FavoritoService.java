package services;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import logica.Favorito;
import java.util.List;

@Stateless
public class FavoritoService {

    @PersistenceContext(unitName = "persistencia")
    private EntityManager em;

    public void crear(Favorito favorito) {
        em.persist(favorito);
    }

    public Favorito buscarPorId(int id) {
        return em.find(Favorito.class, id);
    }

    public void eliminar(Favorito favorito) {
        if (!em.contains(favorito)) {
            favorito = em.merge(favorito);
        }
        em.remove(favorito);
    }

    public List<Favorito> listarTodos() {
        return em.createQuery("SELECT f FROM Favorito f", Favorito.class).getResultList();
    }

    public List<Favorito> obtenerPorUsuario(int idUsuario) {
        TypedQuery<Favorito> query = em.createQuery(
                "SELECT f FROM Favorito f WHERE f.usuario.idUsuario = :idUsuario", Favorito.class);
        query.setParameter("idUsuario", idUsuario);
        return query.getResultList();
    }

    public Favorito buscarPorUsuarioYPalabra(int idUsuario, int idPalabra) {
        TypedQuery<Favorito> query = em.createQuery(
                "SELECT f FROM Favorito f WHERE f.usuario.idUsuario = :idUsuario AND f.palabra.idPalabra = :idPalabra",
                Favorito.class);
        query.setParameter("idUsuario", idUsuario);
        query.setParameter("idPalabra", idPalabra);
        List<Favorito> resultados = query.getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }
}