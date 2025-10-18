package api;

import logica.Categoria;
import rest.CategoriaResource;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.*;
import java.util.List;

@WebServlet("/api/categorias")
public class CategoriaServlet extends HttpServlet {

    @Inject
    private CategoriaResource categoriaResource;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json;charset=UTF-8");
        try (BufferedReader reader = req.getReader(); PrintWriter out = res.getWriter()) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            String body = sb.toString();

            String nombre = extraerCampo(body, "nombre");
            String descripcion = extraerCampo(body, "descripcion");
            String imagenUrl = extraerCampo(body, "imagenUrl");

            if (nombre == null || descripcion == null) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Campos incompletos\"}");
                return;
            }

            Categoria c = new Categoria();
            c.setNombre(nombre);
            c.setDescripcion(descripcion);
            c.setImagenUrl(imagenUrl);

            categoriaResource.crearCategoria(c);
            out.print("{\"mensaje\":\"Categoría creada correctamente\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();

        String idParam = req.getParameter("id");
        String nombre = req.getParameter("nombre");

        if (idParam != null) {
            Categoria c = categoriaResource.buscarPorId(Integer.parseInt(idParam));
            if (c == null) {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Categoría no encontrada\"}");
                return;
            }
            out.printf("{\"id\":%d,\"nombre\":\"%s\",\"descripcion\":\"%s\",\"imagenUrl\":\"%s\"}",
                    c.getIdCategoria(), c.getNombre(), c.getDescripcion(), c.getImagenUrl());
        } else if (nombre != null) {
            List<Categoria> lista = categoriaResource.buscarPorNombre(nombre);
            out.print("[");
            for (int i = 0; i < lista.size(); i++) {
                Categoria c = lista.get(i);
                out.printf("{\"id\":%d,\"nombre\":\"%s\",\"descripcion\":\"%s\"}%s",
                        c.getIdCategoria(), c.getNombre(), c.getDescripcion(),
                        (i < lista.size() - 1 ? "," : ""));
            }
            out.print("]");
        } else {
            List<Categoria> lista = categoriaResource.listarTodas();
            out.print("[");
            for (int i = 0; i < lista.size(); i++) {
                Categoria c = lista.get(i);
                out.printf("{\"id\":%d,\"nombre\":\"%s\",\"descripcion\":\"%s\",\"imagenUrl\":\"%s\"}%s",
                        c.getIdCategoria(), c.getNombre(), c.getDescripcion(), c.getImagenUrl(),
                        (i < lista.size() - 1 ? "," : ""));
            }
            out.print("]");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        String idParam = req.getParameter("id");
        PrintWriter out = res.getWriter();

        if (idParam == null) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Debe proporcoinar un id\"}");
            return;
        }

        boolean ok = categoriaResource.eliminarCategoria(Integer.parseInt(idParam));
        if (ok) out.print("{\"mensaje\":\"Categoría eliminada correctamente\"}");
        else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"error\":\"Categoría no encontrada\"}");
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