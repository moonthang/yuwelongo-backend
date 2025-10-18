package api;

import logica.Usuario;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.*;
import java.util.List;
import rest.UsuarioResource;
import java.util.stream.Collectors;

@WebServlet("/api/usuarios/*")
public class UserServlet extends HttpServlet {

    @Inject
    private UsuarioResource usuarioResource;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json;charset=UTF-8");

        String body = req.getReader().lines().collect(Collectors.joining());
        Usuario nuevo = parseUsuario(body);

        try (PrintWriter out = res.getWriter()) {
            boolean ok = usuarioResource.crearUsuario(nuevo);
            if (ok) {
                out.print("{\"mensaje\":\"Usuario creado correctamente\"}");
            } else {
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                out.print("{\"error\":\"El correo ya existe\"}");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json;charset=UTF-8");
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.length() > 1) {
            int id = Integer.parseInt(pathInfo.substring(1));
            Usuario u = usuarioResource.buscarPorId(id);
            if (u != null) {
                res.getWriter().write(usuarioToJson(u));
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                res.getWriter().write("{\"error\":\"Usuario no encontrado\"}");
            }
            return;
        }

        String correo = req.getParameter("correo");
        if (correo != null) {
            Usuario u = usuarioResource.buscarPorCorreo(correo);
            if (u != null) {
                res.getWriter().write(usuarioToJson(u));
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                res.getWriter().write("{\"error\":\"Usuario no encontrado\"}");
            }
            return;
        }

        List<Usuario> lista = usuarioResource.listarUsuarios();
        String json = lista.stream()
                .map(this::usuarioToJson)
                .collect(Collectors.joining(",", "[", "]"));
        res.getWriter().write(json);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json;charset=UTF-8");
        String body = req.getReader().lines().collect(Collectors.joining());
        Usuario actualizado = parseUsuario(body);

        boolean ok = usuarioResource.actualizarUsuario(actualizado);

        try (PrintWriter out = res.getWriter()) {
            if (ok) {
                out.print("{\"mensaje\":\"Usuario actualizado\"}");
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Usuario no encontrado\"}");
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json;charset=UTF-8");
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("{\"error\":\"Debe especificar el ID\"}");
            return;
        }

        int id = Integer.parseInt(pathInfo.substring(1));
        boolean ok = usuarioResource.eliminarUsuario(id);

        try (PrintWriter out = res.getWriter()) {
            if (ok) {
                out.print("{\"mensaje\":\"Usuario eliminado\"}");
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Usuario no encontrado\"}");
            }
        }
    }

    private Usuario parseUsuario(String json) {
        Usuario u = new Usuario();
        u.setNombre(extraerCampo(json, "nombre"));
        u.setCorreo(extraerCampo(json, "correo"));
        u.setContrasena(extraerCampo(json, "contrasena"));
        String rol = extraerCampo(json, "rol");
        if (rol != null) {
            try {
                u.setRol(Usuario.Rol.valueOf(rol.toUpperCase()));
            } catch (Exception ignored) {
                u.setRol(Usuario.Rol.INVITADO);
            }
        }
        String id = extraerCampo(json, "idUsuario");
        if (id != null && !id.isEmpty()) {
            u.setIdUsuario(Integer.parseInt(id));
        }
        return u;
    }

    private String usuarioToJson(Usuario u) {
        return String.format(
                "{\"idUsuario\":%d,\"nombre\":\"%s\",\"correo\":\"%s\",\"rol\":\"%s\",\"estado\":\"%s\",\"fechaRegistro\":\"%s\"}",
                u.getIdUsuario(), u.getNombre(), u.getCorreo(), u.getRol(), u.getEstado(), u.getFechaRegistro());
    }

    private String extraerCampo(String json, String campo) {
        int i = json.indexOf(campo);
        if (i == -1) return null;
        int start = json.indexOf(":", i) + 1;
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        if (start == 0 || end == -1) return null;
        return json.substring(start, end).replaceAll("[\"{} ]", "");
    }
}