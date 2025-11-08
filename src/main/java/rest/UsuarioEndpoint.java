package rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.inject.Inject;
import logica.Usuario;
import java.util.List;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioEndpoint {

    @Inject
    private UsuarioResource usuarioResource;

    @POST
    public Response crearUsuario(Usuario u) {
        boolean ok = usuarioResource.crearUsuario(u);
        if (!ok) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"El correo ya existe\"}")
                    .build();
        }
        return Response.ok("{\"mensaje\":\"Usuario creado correctamente\"}").build();
    }

    @GET
    public Response listarUsuarios(@QueryParam("correo") String correo) {
        if (correo != null) {
            Usuario u = usuarioResource.buscarPorCorreo(correo);
            if (u == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Usuario no encontrado\"}")
                        .build();
            }
            return Response.ok(u).build();
        }
        List<Usuario> lista = usuarioResource.listarUsuarios();
        return Response.ok(lista).build();
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") int id) {
        Usuario u = usuarioResource.buscarPorId(id);
        if (u == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Usuario no encontrado\"}")
                    .build();
        }
        return Response.ok(u).build();
    }

    @PUT
    public Response actualizarUsuario(Usuario u) {
        boolean ok = usuarioResource.actualizarUsuario(u);
        if (!ok) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Usuario no encontrado\"}")
                    .build();
        }
        return Response.ok("{\"mensaje\":\"Usuario actualizado\"}").build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarUsuario(@PathParam("id") int id) {
        boolean ok = usuarioResource.eliminarUsuario(id);
        if (!ok) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Usuario no encontrado\"}")
                    .build();
        }
        return Response.ok("{\"mensaje\":\"Usuario eliminado\"}").build();
    }
}