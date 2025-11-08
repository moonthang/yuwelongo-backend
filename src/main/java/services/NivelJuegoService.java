package services;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import logica.NivelJuego;

@Stateless
public class NivelJuegoService {

    @PersistenceContext(unitName = "persistencia")
    private EntityManager em;

    public NivelJuego crear(NivelJuego nivel) {
        em.persist(nivel);
        return nivel;
    }

    public NivelJuego editar(NivelJuego nivel) {
        return em.merge(nivel);
    }

    public void eliminar(int idNivel) {
        NivelJuego nivel = em.find(NivelJuego.class, idNivel);
        if (nivel != null) {
            em.remove(nivel);
        }
    }

    public NivelJuego obtenerNivelPorId(int idNivel) {
        return em.find(NivelJuego.class, idNivel);
    }

    public List<NivelJuego> obtenerTodosLosNiveles() {
        return em.createQuery("SELECT n FROM NivelJuego n WHERE n.estado = 'activo' ORDER BY n.orden", NivelJuego.class)
                .getResultList();
    }

    public NivelJuego obtenerSiguienteNivel(int ordenActual) {
        List<NivelJuego> niveles = em.createQuery("SELECT n FROM NivelJuego n WHERE n.orden > :ordenActual AND n.estado = 'activo' ORDER BY n.orden ASC", NivelJuego.class)
                .setParameter("ordenActual", ordenActual)
                .setMaxResults(1)
                .getResultList();
        return niveles.isEmpty() ? null : niveles.get(0);
    }

    public NivelJuego obtenerNivelPorOrden(int orden) {
        List<NivelJuego> niveles = em.createQuery("SELECT n FROM NivelJuego n WHERE n.orden = :orden AND n.estado = 'activo'", NivelJuego.class)
                .setParameter("orden", orden)
                .getResultList();
        return niveles.isEmpty() ? null : niveles.get(0);
    }
}