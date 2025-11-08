package services;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import logica.Juego;

@Stateless
public class JuegoService {

    @PersistenceContext(unitName = "persistencia")
    private EntityManager em;

    public Juego guardarResultado(Juego juego) {
        em.persist(juego);
        return juego;
    }

    public Juego editar(Juego juego) {
        return em.merge(juego);
    }

    public List<Juego> obtenerHistorialPorUsuario(int idUsuario) {
        return em.createQuery("SELECT j FROM Juego j WHERE j.usuario.idUsuario = :idUsuario ORDER BY j.fecha_juego DESC", Juego.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

    public List<Juego> obtenerMejoresPuntajesPorNivel(int idNivel, int limite) {
        return em.createQuery("SELECT j FROM Juego j WHERE j.nivel.idNivel = :idNivel ORDER BY j.puntaje DESC, j.fecha_juego DESC", Juego.class)
                .setParameter("idNivel", idNivel)
                .setMaxResults(limite)
                .getResultList();
    }

    public Juego obtenerMejorPuntajeUsuarioPorNivel(int idUsuario, int idNivel) {
        List<Juego> resultados = em.createQuery("SELECT j FROM Juego j WHERE j.usuario.idUsuario = :idUsuario AND j.nivel.idNivel = :idNivel ORDER BY j.puntaje DESC", Juego.class)
                .setParameter("idUsuario", idUsuario)
                .setParameter("idNivel", idNivel)
                .setMaxResults(1)
                .getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    public int obtenerPuntajeTotalUsuario(int idUsuario) {
        Long total = em.createQuery("SELECT SUM(j.puntaje) FROM Juego j WHERE j.usuario.idUsuario = :idUsuario", Long.class)
                .setParameter("idUsuario", idUsuario)
                .getSingleResult();
        return total != null ? total.intValue() : 0;
    }

    public List<Juego> obtenerRankingGlobal(int limite) {
        return em.createQuery("SELECT j FROM Juego j ORDER BY j.puntaje DESC, j.fecha_juego DESC", Juego.class)
                .setMaxResults(limite)
                .getResultList();
    }
}