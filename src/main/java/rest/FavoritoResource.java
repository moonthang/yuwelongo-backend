package rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import logica.Favorito;
import logica.Palabra;
import logica.Usuario;
import services.FavoritoService;
import services.PalabraService;
import services.UsuarioService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Stateless
@Path("/favoritos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FavoritoResource {

    @Inject
    private FavoritoService favoritoService;

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private PalabraService palabraService;

    @POST
    public Response agregarFavorito(@QueryParam("idUsuario") int idUsuario,
                                    @QueryParam("idPalabra") int idPalabra) {

        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        Palabra palabra = palabraService.buscarPorId(idPalabra);

        if (usuario == null || palabra == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Usuario o Palabra no encontrados\"}")
                    .build();
        }

        Favorito existente = favoritoService.buscarPorUsuarioYPalabra(idUsuario, idPalabra);
        if (existente != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"La palabra ya está en favoritos\"}")
                    .build();
        }

        Favorito nuevo = new Favorito();
        nuevo.setUsuario(usuario);
        nuevo.setPalabra(palabra);
        nuevo.setFechaGuardado(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        favoritoService.crear(nuevo);

        return Response.status(Response.Status.CREATED)
                .entity("{\"mensaje\":\"Favorito agregado correctamente\"}")
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarFavorito(@PathParam("id") int id) {
        Favorito favorito = favoritoService.buscarPorId(id);
        if (favorito == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Favorito no encontrado\"}")
                    .build();
        }

        favoritoService.eliminar(favorito);
        return Response.ok("{\"mensaje\":\"Favorito eliminado correctamente\"}").build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        Favorito favorito = favoritoService.buscarPorId(id);
        if (favorito == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Favorito no encontrado\"}")
                    .build();
        }
        return Response.ok(favorito).build();
    }

    @GET
    public Response listarTodos() {
        List<Favorito> lista = favoritoService.listarTodos();
        return Response.ok(lista).build();
    }

    @GET
    @Path("/usuario/{idUsuario}")
    public Response obtenerPorUsuario(@PathParam("idUsuario") int idUsuario) {
        List<Favorito> favoritos = favoritoService.obtenerPorUsuario(idUsuario);
        if (favoritos.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"El usuario no tiene favoritos\"}")
                    .build();
        }
        return Response.ok(favoritos).build();
    }
}