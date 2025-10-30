package api;

import logica.Palabra;
import rest.PalabraResource;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.*;
import java.util.List;

@WebServlet("/api/palabras")
public class PalabraServlet extends HttpServlet {

    @Inject
    private PalabraResource palabraResource;

    private String getString(JsonObject json, String key) {
        if (json.containsKey(key) && json.get(key) != null && !json.get(key).toString().equalsIgnoreCase("null")) {
            return json.getString(key);
        }
        return null;
    }

    private String getStringNumber(JsonObject json, String key) {
        if (json.containsKey(key) && json.get(key) != null && !json.get(key).toString().equalsIgnoreCase("null")) {
            try {
                return json.getString(key);
            } catch (ClassCastException e) {
                try {
                    return String.valueOf(json.getInt(key));
                } catch (Exception ex) {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = res.getWriter(); JsonReader jsonReader = Json.createReader(req.getReader())) {

            JsonObject jsonBody = jsonReader.readObject();

            String palabraNasa = getString(jsonBody, "palabraNasa");
            String traduccion = getString(jsonBody, "traduccion");
            String frase = getString(jsonBody, "fraseEjemplo");
            String imagenUrl = getString(jsonBody, "imagenUrl");
            String audioUrl = getString(jsonBody, "audioUrl");
            String categoriaId = getStringNumber(jsonBody, "idCategoria");

            if (palabraNasa == null || traduccion == null || categoriaId == null) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Campos incompletos: palabraNasa, traduccion e idCategoria son obligatorios\"}");
                return;
            }

            Palabra p = new Palabra();
            p.setPalabraNasa(palabraNasa);
            p.setTraduccion(traduccion);
            p.setFraseEjemplo(frase);
            p.setImagenUrl(imagenUrl);
            p.setAudioUrl(audioUrl);

            boolean ok = palabraResource.crearPalabra(p, Integer.parseInt(categoriaId));

            if (ok) {
                res.setStatus(HttpServletResponse.SC_CREATED);
                out.print("{\"mensaje\":\"Palabra creada correctamente\"}");
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Categoría no encontrada\"}");
            }
        } catch (JsonException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().print("{\"error\":\"El cuerpo de la solicitud no es un JSON válido\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();

        String idParam = req.getParameter("id");
        String nasa = req.getParameter("palabraNasa");
        String traduccion = req.getParameter("traduccion");

        if (idParam != null && !idParam.isEmpty()) {
            Palabra p = palabraResource.buscarPorId(Integer.parseInt(idParam));
            if (p == null) {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Palabra no encontrada\"}");
                return;
            }
            int idCategoria = (p.getCategoria() != null) ? p.getCategoria().getIdCategoria() : 0;
            out.printf("{\"id\":%d,\"palabraNasa\":\"%s\",\"traduccion\":\"%s\",\"fraseEjemplo\":\"%s\",\"imagenUrl\":\"%s\",\"audioUrl\":\"%s\",\"fecha_creacion\":\"%s\",\"idCategoria\":%d}",
                    p.getIdPalabra(),
                    p.getPalabraNasa() != null ? p.getPalabraNasa() : "",
                    p.getTraduccion() != null ? p.getTraduccion() : "",
                    p.getFraseEjemplo() != null ? p.getFraseEjemplo() : "",
                    p.getImagenUrl() != null ? p.getImagenUrl() : "",
                    p.getAudioUrl() != null ? p.getAudioUrl() : "",
                    p.getFechaCreacion() != null ? p.getFechaCreacion() : "",
                    idCategoria);
        } else if (nasa != null && !nasa.isEmpty()) {
            List<Palabra> lista = palabraResource.buscarPorPalabraNasa(nasa);
            imprimirLista(out, lista);
        } else if (traduccion != null && !traduccion.isEmpty()) {
            List<Palabra> lista = palabraResource.buscarPorTraduccion(traduccion);
            imprimirLista(out, lista);
        } else {
            List<Palabra> lista = palabraResource.listarTodas();
            imprimirLista(out, lista);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        res.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = res.getWriter(); JsonReader jsonReader = Json.createReader(req.getReader())) {

            JsonObject jsonBody = jsonReader.readObject();

            String idParam = getStringNumber(jsonBody, "id");
            String palabraNasa = getString(jsonBody, "palabraNasa");
            String traduccion = getString(jsonBody, "traduccion");
            String frase = getString(jsonBody, "fraseEjemplo");
            String imagenUrl = getString(jsonBody, "imagenUrl");
            String audioUrl = getString(jsonBody, "audioUrl");
            String categoriaId = getStringNumber(jsonBody, "idCategoria");

            if (idParam == null) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Debe incluir el id de la palabra a actualizar\"}");
                return;
            }

            Palabra existente = palabraResource.buscarPorId(Integer.parseInt(idParam));
            if (existente == null) {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Palabra no encontrada\"}");
                return;
            }

            if (palabraNasa != null) {
                existente.setPalabraNasa(palabraNasa);
            }
            if (traduccion != null) {
                existente.setTraduccion(traduccion);
            }
            if (frase != null) {
                existente.setFraseEjemplo(frase);
            }
            if (imagenUrl != null) {
                existente.setImagenUrl(imagenUrl);
            }
            if (audioUrl != null) {
                existente.setAudioUrl(audioUrl);
            }

            int idCategoria = (categoriaId != null) ? Integer.parseInt(categoriaId) : 0;

            palabraResource.actualizarPalabra(existente, idCategoria);

            res.setStatus(HttpServletResponse.SC_OK);
            out.print("{\"mensaje\":\"Palabra actualizada correctamente\"}");
        } catch (JsonException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().print("{\"error\":\"El cuerpo de la solicitud no es un JSON válido\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        String idParam = req.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Debe proporcionar un id para eliminar\"}");
            return;
        }

        boolean ok = palabraResource.eliminarPalabra(Integer.parseInt(idParam));
        if (ok) {
            res.setStatus(HttpServletResponse.SC_OK);
            out.print("{\"mensaje\":\"Palabra eliminada correctamente\"}");
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"error\":\"Palabra no encontrada\"}");
        }
    }

    private void imprimirLista(PrintWriter out, List<Palabra> lista) {
        out.print("[");
        for (int i = 0; i < lista.size(); i++) {
            Palabra p = lista.get(i);
            int idCategoria = (p.getCategoria() != null) ? p.getCategoria().getIdCategoria() : 0;

            out.printf("{\"id\":%d,\"palabraNasa\":\"%s\",\"traduccion\":\"%s\",\"fraseEjemplo\":\"%s\",\"imagenUrl\":\"%s\",\"audioUrl\":\"%s\",\"fecha_creacion\":\"%s\",\"idCategoria\":%d}%s",
                    p.getIdPalabra(),
                    p.getPalabraNasa() != null ? p.getPalabraNasa() : "",
                    p.getTraduccion() != null ? p.getTraduccion() : "",
                    p.getFraseEjemplo() != null ? p.getFraseEjemplo() : "",
                    p.getImagenUrl() != null ? p.getImagenUrl() : "",
                    p.getAudioUrl() != null ? p.getAudioUrl() : "",
                    p.getFechaCreacion() != null ? p.getFechaCreacion() : "",
                    idCategoria,
                    (i < lista.size() - 1 ? "," : "")
            );
        }
        out.print("]");
    }
}