package rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import logica.Palabra;
import logica.Categoria;
import services.PalabraService;
import services.CategoriaService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Stateless
@Path("/palabras")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PalabraResource {

    @Inject
    private PalabraService palabraService;

    @Inject
    private CategoriaService categoriaService;

    @POST
    public Response crearPalabra(Palabra palabra, @QueryParam("idCategoria") int idCategoria) {
        Categoria categoria = categoriaService.buscarPorId(idCategoria);
        if (categoria == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Categoría no encontrada\"}")
                    .build();
        }

        palabra.setCategoria(categoria);
        palabra.setFechaCreacion(LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        palabraService.crear(palabra);

        return Response.status(Response.Status.CREATED)
                .entity("{\"mensaje\":\"Palabra creada correctamente\"}")
                .build();
    }

    @PUT
    public Response actualizarPalabra(Palabra palabra, @QueryParam("idCategoria") int idCategoria) {
        Palabra existente = palabraService.buscarPorId(palabra.getIdPalabra());
        if (existente == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Palabra no encontrada\"}")
                    .build();
        }

        if (idCategoria > 0) {
            Categoria categoria = categoriaService.buscarPorId(idCategoria);
            if (categoria != null) {
                existente.setCategoria(categoria);
            }
        }

        if (palabra.getPalabraNasa() != null) {
            existente.setPalabraNasa(palabra.getPalabraNasa());
        }
        if (palabra.getTraduccion() != null) {
            existente.setTraduccion(palabra.getTraduccion());
        }
        if (palabra.getFraseEjemplo() != null) {
            existente.setFraseEjemplo(palabra.getFraseEjemplo());
        }
        if (palabra.getImagenUrl() != null) {
            existente.setImagenUrl(palabra.getImagenUrl());
        }
        if (palabra.getAudioUrl() != null) {
            existente.setAudioUrl(palabra.getAudioUrl());
        }

        palabraService.editar(existente);

        return Response.ok("{\"mensaje\":\"Palabra actualizada correctamente\"}").build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarPalabra(@PathParam("id") int id) {
        Palabra p = palabraService.buscarPorId(id);
        if (p == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Palabra no encontrada\"}")
                    .build();
        }

        palabraService.eliminar(p);
        return Response.ok("{\"mensaje\":\"Palabra eliminada correctamente\"}").build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        Palabra p = palabraService.buscarPorId(id);
        if (p == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Palabra no encontrada\"}")
                    .build();
        }
        return Response.ok(p).build();
    }

    @GET
    public Response listar(
            @QueryParam("palabraNasa") String palabraNasa,
            @QueryParam("traduccion") String traduccion) {

        List<Palabra> lista;
        if (palabraNasa != null && !palabraNasa.isEmpty()) {
            lista = palabraService.buscarPorPalabraNasa(palabraNasa);
        } else if (traduccion != null && !traduccion.isEmpty()) {
            lista = palabraService.buscarPorTraduccion(traduccion);
        } else {
            lista = palabraService.listarTodas();
        }

        return Response.ok(lista).build();
    }

    @GET
    @Path("/categoria/{idCategoria}")
    public Response obtenerPorCategoria(@PathParam("idCategoria") int idCategoria) {
        List<Palabra> palabras = palabraService.obtenerPorCategoria(idCategoria);
        if (palabras.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"No hay palabras para esta categoría\"}")
                    .build();
        }
        return Response.ok(palabras).build();
    }

}