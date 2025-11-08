package security;

import io.jsonwebtoken.*;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.Response;
import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String method = requestContext.getMethod();
        String path = requestContext.getUriInfo().getPath();

        if ("OPTIONS".equalsIgnoreCase(method)) {
            requestContext.abortWith(Response.ok().build());
            return;
        }

        if (path.endsWith("login")
                || (path.contains("categorias") && method.equalsIgnoreCase("GET"))
                || (path.contains("palabras") && method.equalsIgnoreCase("GET"))
                || (path.contains("niveles-juego") && method.equalsIgnoreCase("GET"))
                || (path.contains("preguntas-juego") && method.equalsIgnoreCase("GET"))
                || (path.contains("juegos") && method.equalsIgnoreCase("GET"))
                || (path.contains("usuarios") && method.equalsIgnoreCase("POST"))) {
            return;
        }

        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Token no proporcionado\"}").build());
            return;
        }

        String token = authHeader.substring(7);

        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(SecurityConstants.JWT_SECRET_KEY) 
                    .build()
                    .parseSignedClaims(token);

            String rol = claims.getPayload().get("rol", String.class);
            if (!("ADMIN".equals(rol) || "USUARIO".equals(rol))) {
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                        .entity("{\"error\":\"Rol no autorizado\"}").build());
            }
        } catch (JwtException e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Token inválido o expirado\"}").build());
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {

        String origin = requestContext.getHeaderString("Origin");
        if (origin != null && !origin.isEmpty()) {
            responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", origin);
        } else {
            responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        }

        responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
        responseContext.getHeaders().putSingle("Access-Control-Expose-Headers", "Authorization");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().putSingle("Access-Control-Max-Age", "3600");
        responseContext.getHeaders().putSingle("Vary", "Origin");
    }
}