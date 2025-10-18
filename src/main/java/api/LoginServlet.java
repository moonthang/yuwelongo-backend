package api;

import logica.Usuario;
import rest.UsuarioResource;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {

    @Inject
    private UsuarioResource usuarioResource;

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
        "fQw8vDNz3pL!eR$Yx7uT@9sZqG2nH#jM".getBytes(StandardCharsets.UTF_8)
    );

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json;charset=UTF-8");

        try (BufferedReader reader = req.getReader(); PrintWriter out = res.getWriter()) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            String body = sb.toString();

            String correo = extraerCampo(body, "correo");
            String contrasena = extraerCampo(body, "contrasena");

            Usuario u = usuarioResource.autenticar(correo, contrasena);

            if (u != null) {
                String token = Jwts.builder()
                        .subject(u.getCorreo())
                        .claim("rol", u.getRol().name())
                        .claim("nombre", u.getNombre())
                        .issuedAt(new Date())
                        .expiration(new Date(System.currentTimeMillis() + 3600000))
                        .signWith(SECRET_KEY)
                        .compact();

                out.print("{");
                out.print("\"token\":\"" + token + "\",");
                out.print("\"rol\":\"" + u.getRol().name() + "\",");
                out.print("\"nombre\":\"" + u.getNombre() + "\"");
                out.print("}");
            } else {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"error\":\"Credenciales inválidas\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String extraerCampo(String json, String campo) {
        int i = json.indexOf(campo);
        if (i == -1) return null;
        int start = json.indexOf(":", i) + 1;
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        return json.substring(start, end).replaceAll("[\"{} ]", "");
    }
}