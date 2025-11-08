package services;

import jakarta.ejb.Stateless;
import logica.PreguntaJuego;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class PreguntaJuegoService {
    
    @PersistenceContext(unitName = "persistencia")
    private EntityManager em;
    
    public PreguntaJuego crear(PreguntaJuego pregunta) {
        em.persist(pregunta);
        return pregunta;
    }
    
    public PreguntaJuego editar(PreguntaJuego pregunta) {
        return em.merge(pregunta);
    }
    
    public void eliminar(int idPregunta) {
        PreguntaJuego pregunta = em.find(PreguntaJuego.class, idPregunta);
        if (pregunta != null) {
            em.remove(pregunta);
        }
    }
    
    public PreguntaJuego obtenerPreguntaPorId(int idPregunta) {
        return em.find(PreguntaJuego.class, idPregunta);
    }

    public List<PreguntaJuego> obtenerTodasLasPreguntas() {
        return em.createQuery("SELECT p FROM PreguntaJuego p ORDER BY p.idPregunta", PreguntaJuego.class)
                .getResultList();
    }

    public List<PreguntaJuego> obtenerPreguntasPorNivel(int idNivel) {
        return em.createQuery("SELECT p FROM PreguntaJuego p WHERE p.nivel.idNivel = :idNivel ORDER BY p.idPregunta", PreguntaJuego.class)
                .setParameter("idNivel", idNivel)
                .getResultList();
    }

    public List<PreguntaJuego> obtenerPreguntasAleatoriasPorNivel(int idNivel, int cantidad) {
        return em.createQuery("SELECT p FROM PreguntaJuego p WHERE p.nivel.idNivel = :idNivel ORDER BY RAND()", PreguntaJuego.class)
                .setParameter("idNivel", idNivel)
                .setMaxResults(cantidad)
                .getResultList();
    }
}