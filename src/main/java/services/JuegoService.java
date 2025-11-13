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
        return em.createQuery(
                "SELECT j FROM Juego j WHERE j.usuario.idUsuario = :idUsuario ORDER BY j.fecha_juego DESC",
                Juego.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

    public List<Juego> obtenerMejoresPuntajesPorNivel(int idNivel, int limite) {
        return em.createQuery(
                "SELECT j FROM Juego j WHERE j.nivel.idNivel = :idNivel ORDER BY j.puntaje DESC, j.fecha_juego DESC",
                Juego.class)
                .setParameter("idNivel", idNivel)
                .setMaxResults(limite)
                .getResultList();
    }

    public Juego obtenerMejorPuntajeUsuarioPorNivel(int idUsuario, int idNivel) {
        List<Juego> resultados = em.createQuery(
                "SELECT j FROM Juego j WHERE j.usuario.idUsuario = :idUsuario AND j.nivel.idNivel = :idNivel ORDER BY j.puntaje DESC",
                Juego.class)
                .setParameter("idUsuario", idUsuario)
                .setParameter("idNivel", idNivel)
                .setMaxResults(1)
                .getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    public int obtenerPuntajeTotalUsuario(int idUsuario) {
        Long total = em.createQuery(
                "SELECT SUM(j.puntaje) FROM Juego j WHERE j.usuario.idUsuario = :idUsuario",
                Long.class)
                .setParameter("idUsuario", idUsuario)
                .getSingleResult();
        return total != null ? total.intValue() : 0;
    }

    public List<Object[]> obtenerRankingGlobal(int limite) {
        try {
            return em.createQuery(
                    "SELECT u.idUsuario, u.nombre, SUM(j.puntaje) AS totalPuntaje, j.idPartida "
                    + "FROM Juego j JOIN j.usuario u "
                    + "WHERE j.idPartida IS NOT NULL "
                    + "GROUP BY u.idUsuario, u.nombre, j.idPartida "
                    + "ORDER BY totalPuntaje DESC",
                    Object[].class
            )
                    .setMaxResults(limite)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Object[]> obtenerRankingPorPartida(String idPartida) {
        try {
            return em.createQuery(
                    "SELECT u.idUsuario, u.nombre, SUM(j.puntaje) AS totalPuntaje "
                    + "FROM Juego j JOIN j.usuario u "
                    + "WHERE j.idPartida = :idPartida "
                    + "GROUP BY u.idUsuario, u.nombre "
                    + "ORDER BY totalPuntaje DESC",
                    Object[].class
            )
                    .setParameter("idPartida", idPartida)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}