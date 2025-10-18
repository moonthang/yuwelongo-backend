package api;

import logica.Palabra;
import rest.PalabraResource;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.*;
import java.util.List;

@WebServlet("/api/palabras")
public class PalabraServlet extends HttpServlet {

    @Inject
    private PalabraResource palabraResource;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json;charset=UTF-8");
        try (BufferedReader reader = req.getReader(); PrintWriter out = res.getWriter()) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            String body = sb.toString();

            String palabraNasa = extraerCampo(body, "palabraNasa");
            String traduccion = extraerCampo(body, "traduccion");
            String frase = extraerCampo(body, "fraseEjemplo");
            String imagenUrl = extraerCampo(body, "imagenUrl");
            String audioUrl = extraerCampo(body, "audioUrl");
            String categoriaId = extraerCampo(body, "idCategoria");

            if (palabraNasa == null || traduccion == null || categoriaId == null) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Campos incompletos\"}");
                return;
            }

            Palabra p = new Palabra();
            p.setPalabraNasa(palabraNasa);
            p.setTraduccion(traduccion);
            p.setFraseEjemplo(frase);
            p.setImagenUrl(imagenUrl);
            p.setAudioUrl(audioUrl);

            boolean ok = palabraResource.crearPalabra(p, Integer.parseInt(categoriaId));

            if (ok) out.print("{\"mensaje\":\"Palabra creada correctamente\"}");
            else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Categoría no encontrada\"}");
            }
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

        if (idParam != null) {
            Palabra p = palabraResource.buscarPorId(Integer.parseInt(idParam));
            if (p == null) {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Palabra no encontrada\"}");
                return;
            }
            out.printf("{\"id\":%d,\"palabraNasa\":\"%s\",\"traduccion\":\"%s\",\"fraseEjemplo\":\"%s\",\"imagenUrl\":\"%s\",\"audioUrl\":\"%s\"}",
                    p.getIdPalabra(), p.getPalabraNasa(), p.getTraduccion(), p.getFraseEjemplo(),
                    p.getImagenUrl(), p.getAudioUrl());
        } else if (nasa != null) {
            List<Palabra> lista = palabraResource.buscarPorPalabraNasa(nasa);
            imprimirLista(out, lista);
        } else if (traduccion != null) {
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
        try (BufferedReader reader = req.getReader(); PrintWriter out = res.getWriter()) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            String body = sb.toString();

            String idParam = extraerCampo(body, "id");
            String palabraNasa = extraerCampo(body, "palabraNasa");
            String traduccion = extraerCampo(body, "traduccion");
            String frase = extraerCampo(body, "fraseEjemplo");
            String imagenUrl = extraerCampo(body, "imagenUrl");
            String audioUrl = extraerCampo(body, "audioUrl");
            String categoriaId = extraerCampo(body, "idCategoria");

            if (idParam == null) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Debe incluir el id de la palabra\"}");
                return;
            }

            Palabra existente = palabraResource.buscarPorId(Integer.parseInt(idParam));
            if (existente == null) {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Palabra no encontrada\"}");
                return;
            }

            existente.setPalabraNasa(palabraNasa);
            existente.setTraduccion(traduccion);
            existente.setFraseEjemplo(frase);
            existente.setImagenUrl(imagenUrl);
            existente.setAudioUrl(audioUrl);

            palabraResource.actualizarPalabra(existente, Integer.parseInt(categoriaId));
            out.print("{\"mensaje\":\"Palabra actualizada correctamente\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        String idParam = req.getParameter("id");

        if (idParam == null) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Debe proporcionar un id\"}");
            return;
        }

        boolean ok = palabraResource.eliminarPalabra(Integer.parseInt(idParam));
        if (ok) out.print("{\"mensaje\":\"Palabra eliminada correctamente\"}");
        else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"error\":\"Palabra no encontrada\"}");
        }
    }

    private void imprimirLista(PrintWriter out, List<Palabra> lista) {
        out.print("[");
        for (int i = 0; i < lista.size(); i++) {
            Palabra p = lista.get(i);
            out.printf("{\"id\":%d,\"palabraNasa\":\"%s\",\"traduccion\":\"%s\",\"fraseEjemplo\":\"%s\",\"imagenUrl\":\"%s\",\"audioUrl\":\"%s\"}%s",
                    p.getIdPalabra(), p.getPalabraNasa(), p.getTraduccion(), p.getFraseEjemplo(),
                    p.getImagenUrl(), p.getAudioUrl(), (i < lista.size() - 1 ? "," : ""));
        }
        out.print("]");
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