package rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.inject.Inject;
import logica.NivelJuego;
import services.NivelJuegoService;
import java.util.List;

@Path("/niveles-juego")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NivelJuegoResource {

    @Inject
    private NivelJuegoService nivelService;

    @POST
    public NivelJuego crearNivel(NivelJuego nivel) {
        return nivelService.crear(nivel);
    }

    @GET
    public List<NivelJuego> obtenerTodos() {
        return nivelService.obtenerTodosLosNiveles();
    }

    @GET
    @Path("/{id}")
    public NivelJuego obtenerPorId(@PathParam("id") int id) {
        return nivelService.obtenerNivelPorId(id);
    }

    @PUT
    @Path("/{id}")
    public NivelJuego actualizarNivel(@PathParam("id") int id, NivelJuego nivel) {
        nivel.setIdNivel(id);
        return nivelService.editar(nivel);
    }

    @DELETE
    @Path("/{id}")
    public void eliminarNivel(@PathParam("id") int id) {
        nivelService.eliminar(id);
    }
}