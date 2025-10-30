package security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebFilter("/api/*")
public class JwtFilter implements Filter {

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            "fQw8vDNz3pL!eR$Yx7uT@9sZqG2nH#jM".getBytes(StandardCharsets.UTF_8)
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        if ("OPTIONS".equalsIgnoreCase(req.getMethod()) ||
            path.endsWith("/api/login") ||
            (path.endsWith("/api/categorias") && "GET".equalsIgnoreCase(req.getMethod())) ||
            (path.endsWith("/api/usuarios") && "POST".equalsIgnoreCase(req.getMethod()))) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("{\"error\":\"Token no proporcionado\"}");
            return;
        }

        String token = authHeader.substring(7);

        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token);

            String rol = claims.getPayload().get("rol", String.class);

            if ("ADMIN".equals(rol) || "USUARIO".equals(rol)) {
                chain.doFilter(request, response);
            } else {
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                res.getWriter().write("{\"error\":\"Rol no autorizado\"}");
            }

        } catch (JwtException e) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("{\"error\":\"Token inválido o expirado\"}");
        }
    }
}