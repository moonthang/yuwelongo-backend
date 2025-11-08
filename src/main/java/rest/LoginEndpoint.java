package rest;

import io.jsonwebtoken.Jwts;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import logica.Usuario;
import security.SecurityConstants;
import java.util.Date;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class LoginEndpoint {

    @Inject
    private UsuarioResource usuarioResource;

    @POST
    public Response login(Usuario credenciales) {
        if (credenciales == null || credenciales.getCorreo() == null || credenciales.getContrasena() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Faltan credenciales\"}")
                    .build();
        }

        Usuario u = usuarioResource.autenticar(credenciales.getCorreo(), credenciales.getContrasena());

        if (u == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Credenciales inválidas\"}")
                    .build();
        }

        String accessToken = Jwts.builder()
                .subject(u.getCorreo())
                .claim("rol", u.getRol().name())
                .claim("nombre", u.getNombre())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + SecurityConstants.ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(SecurityConstants.JWT_SECRET_KEY)
                .compact();

        String json = String.format(
                "{\"accessToken\":\"%s\",\"rol\":\"%s\",\"nombre\":\"%s\"}",
                accessToken, u.getRol().name(), u.getNombre()
        );

        return Response.ok(json).build();
    }
}