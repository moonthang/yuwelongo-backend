package servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Favorito;
import services.FavoritoService;
import utils.JwtUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "FavoritoServlet", urlPatterns = {"/api/favoritos/*"})
public class FavoritoServlet extends HttpServlet {
    
    private final FavoritoService favoritoService = new FavoritoService();
    private final Gson gson = new Gson();
    
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        configurarCORS(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
    private void configurarCORS(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        configurarCORS(response);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print(gson.toJson(crearRespuesta(false, "Token no proporcionado", null)));
                return;
            }
            
            String token = authHeader.substring(7);
            Integer idUsuario = JwtUtil.validarToken(token);
            
            if (idUsuario == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print(gson.toJson(crearRespuesta(false, "Token inválido o expirado", null)));
                return;
            }
            
            List<Favorito> favoritos = favoritoService.obtenerFavoritosPorUsuario(idUsuario);
            
            Map<String, Object> data = new HashMap<>();
            data.put("favoritos", favoritos);
            data.put("total", favoritos.size());
            
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(gson.toJson(crearRespuesta(true, "Favoritos obtenidos exitosamente", data)));
            
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(crearRespuesta(false, "Error al obtener favoritos: " + e.getMessage(), null)));
        } finally {
            out.flush();
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        configurarCORS(response);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print(gson.toJson(crearRespuesta(false, "Token no proporcionado", null)));
                return;
            }
            
            String token = authHeader.substring(7);
            Integer idUsuario = JwtUtil.validarToken(token);
            
            if (idUsuario == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print(gson.toJson(crearRespuesta(false, "Token inválido o expirado", null)));
                return;
            }
            
            BufferedReader reader = request.getReader();
            Map<String, Object> requestData = gson.fromJson(reader, Map.class);
            
            if (!requestData.containsKey("idPalabra")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(crearRespuesta(false, "idPalabra es requerido", null)));
                return;
            }
            
            int idPalabra = ((Double) requestData.get("idPalabra")).intValue();
            
            boolean agregado = favoritoService.agregarFavorito(idUsuario, idPalabra);
            
            if (agregado) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print(gson.toJson(crearRespuesta(true, "Palabra agregada a favoritos", null)));
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                out.print(gson.toJson(crearRespuesta(false, "La palabra ya está en favoritos", null)));
            }
            
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(crearRespuesta(false, "Error al agregar favorito: " + e.getMessage(), null)));
        } finally {
            out.flush();
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        configurarCORS(response);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print(gson.toJson(crearRespuesta(false, "Token no proporcionado", null)));
                return;
            }
            
            String token = authHeader.substring(7);
            Integer idUsuario = JwtUtil.validarToken(token);
            
            if (idUsuario == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print(gson.toJson(crearRespuesta(false, "Token inválido o expirado", null)));
                return;
            }
            
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.length() <= 1) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(crearRespuesta(false, "idPalabra no proporcionado en la URL", null)));
                return;
            }
            
            int idPalabra = Integer.parseInt(pathInfo.substring(1));
            
            boolean eliminado = favoritoService.eliminarFavorito(idUsuario, idPalabra);
            
            if (eliminado) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.print(gson.toJson(crearRespuesta(true, "Palabra eliminada de favoritos", null)));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(gson.toJson(crearRespuesta(false, "Favorito no encontrado", null)));
            }
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(crearRespuesta(false, "idPalabra inválido", null)));
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(crearRespuesta(false, "Error al eliminar favorito: " + e.getMessage(), null)));
        } finally {
            out.flush();
        }
    }
    
    private Map<String, Object> crearRespuesta(boolean success, String mensaje, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("mensaje", mensaje);
        if (data != null) {
            response.put("data", data);
        }
        return response;
    }
}
