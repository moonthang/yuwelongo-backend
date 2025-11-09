package rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import logica.Categoria;
import services.CategoriaService;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Stateless
@Path("/categorias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoriaResource {

    @Inject
    private CategoriaService categoriaService;

    @POST
    public Response crearCategoria(Categoria categoria) {
        categoria.setEstado("Activa");
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        categoria.setDescripcion((categoria.getDescripcion() == null ? "" : categoria.getDescripcion()) + " (Creado: " + fecha + ")");
        categoriaService.crear(categoria);
        return Response.status(Response.Status.CREATED)
                .entity("{\"mensaje\":\"Categoría creada correctamente\"}")
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizarCategoria(@PathParam("id") int id, Categoria categoria) {
        Categoria existente = categoriaService.buscarPorId(id);
        if (existente == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Categoría no encontrada\"}")
                    .build();
        }

        existente.setNombre(categoria.getNombre());
        existente.setDescripcion(categoria.getDescripcion());
        existente.setImagenUrl(categoria.getImagenUrl());
        categoriaService.editar(existente);

        return Response.ok("{\"mensaje\":\"Categoría actualizada\"}").build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarCategoria(@PathParam("id") int id) {
        Categoria c = categoriaService.buscarPorId(id);
        if (c == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Categoría no encontrada\"}")
                    .build();
        }
        categoriaService.eliminar(c);
        return Response.ok("{\"mensaje\":\"Categoría eliminada\"}").build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        Categoria c = categoriaService.buscarPorId(id);
        if (c == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Categoría no encontrada\"}")
                    .build();
        }
        return Response.ok(c).build();
    }

    @GET
    public Response listar(@QueryParam("nombre") String nombre) {
        List<Categoria> lista = (nombre != null && !nombre.isEmpty())
                ? categoriaService.buscarPorNombre(nombre)
                : categoriaService.listarTodas();
        return Response.ok(lista).build();
    }
}