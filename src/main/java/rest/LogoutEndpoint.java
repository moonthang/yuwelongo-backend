package rest;

import jakarta.ejb.Stateless;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/logout")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class LogoutEndpoint {

    @POST
    public Response logout() {
        return Response.ok("{\"mensaje\":\"Sesión cerrada correctamente\"}").build();
    }
}