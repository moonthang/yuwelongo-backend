package rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.inject.Inject;
import java.util.List;
import logica.Juego;
import services.JuegoService;

@Path("/juegos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JuegoResource {

    @Inject
    private JuegoService juegoService;

    @POST
    public Juego guardarJuego(Juego juego) {
        try {
            return juegoService.guardarResultado(juego);
        } catch (Exception e) {
            throw new WebApplicationException("Error al guardar el resultado: " + e.getMessage());
        }
    }

    @PUT
    @Path("/{id}")
    public Juego actualizarJuego(@PathParam("id") int id, Juego juego) {
        try {
            juego.setIdJuego(id);
            return juegoService.editar(juego);
        } catch (Exception e) {
            throw new WebApplicationException("Error al actualizar el resultado: " + e.getMessage());
        }
    }

    @GET
    @Path("/historial/{idUsuario}")
    public List<Juego> obtenerHistorialPorUsuario(@PathParam("idUsuario") int idUsuario) {
        try {
            return juegoService.obtenerHistorialPorUsuario(idUsuario);
        } catch (Exception e) {
            throw new WebApplicationException("Error al obtener el historial: " + e.getMessage());
        }
    }

    @GET
    @Path("/mejores/{idNivel}")
    public List<Juego> obtenerMejoresPuntajesPorNivel(
            @PathParam("idNivel") int idNivel,
            @QueryParam("limite") @DefaultValue("10") int limite) {
        try {
            return juegoService.obtenerMejoresPuntajesPorNivel(idNivel, limite);
        } catch (Exception e) {
            throw new WebApplicationException("Error al obtener el ranking por nivel: " + e.getMessage());
        }
    }

    @GET
    @Path("/mejor/{idUsuario}/{idNivel}")
    public Juego obtenerMejorPuntajeUsuarioPorNivel(
            @PathParam("idUsuario") int idUsuario,
            @PathParam("idNivel") int idNivel) {
        try {
            return juegoService.obtenerMejorPuntajeUsuarioPorNivel(idUsuario, idNivel);
        } catch (Exception e) {
            throw new WebApplicationException("Error al obtener el mejor puntaje: " + e.getMessage());
        }
    }

    @GET
    @Path("/total/{idUsuario}")
    public int obtenerPuntajeTotalUsuario(@PathParam("idUsuario") int idUsuario) {
        try {
            return juegoService.obtenerPuntajeTotalUsuario(idUsuario);
        } catch (Exception e) {
            throw new WebApplicationException("Error al calcular el puntaje total: " + e.getMessage());
        }
    }

    @GET
    @Path("/ranking")
    public List<Object[]> obtenerRankingGlobal(@QueryParam("idPartida") String idPartida, @QueryParam("limite") @DefaultValue("10") int limite) {
        try {
            if (idPartida != null && !idPartida.isEmpty()) {
                return juegoService.obtenerRankingPorPartida(idPartida);
            } else {
                return juegoService.obtenerRankingGlobal(limite);
            }
        } catch (Exception e) {
            throw new WebApplicationException("Error al obtener el ranking: " + e.getMessage());
        }
    }
}