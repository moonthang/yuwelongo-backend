package services;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import models.Favorito;
import utils.DatabaseConnection;

public class FavoritoService {
    
    public boolean agregarFavorito(int idUsuario, int idPalabra) throws SQLException {
        String sql = "INSERT INTO favoritos (id_usuario, id_palabra, fecha_guardado) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idPalabra);
            stmt.setString(3, LocalDate.now().toString());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        }
    }
    
    public boolean eliminarFavorito(int idUsuario, int idPalabra) throws SQLException {
        String sql = "DELETE FROM favoritos WHERE id_usuario = ? AND id_palabra = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idPalabra);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public List<Favorito> obtenerFavoritosPorUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT f.id_favorito, f.id_usuario, f.id_palabra, " +
                     "p.palabra_nasa, p.traduccion AS traduccion_espanol, p.imagen_url, " +
                     "c.nombre AS categoria, f.fecha_guardado " +
                     "FROM favoritos f " +
                     "INNER JOIN palabras p ON f.id_palabra = p.id_palabra " +
                     "LEFT JOIN categorias c ON p.id_categoria = c.id_categoria " +
                     "WHERE f.id_usuario = ? " +
                     "ORDER BY f.fecha_guardado DESC";
        
        List<Favorito> favoritos = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Favorito fav = new Favorito();
                fav.setIdFavorito(rs.getInt("id_favorito"));
                fav.setIdUsuario(rs.getInt("id_usuario"));
                fav.setIdPalabra(rs.getInt("id_palabra"));
                fav.setPalabraNasa(rs.getString("palabra_nasa"));
                fav.setTraduccionEspanol(rs.getString("traduccion_espanol"));
                fav.setImagenUrl(rs.getString("imagen_url"));
                fav.setCategoria(rs.getString("categoria"));
                fav.setFechaAgregado(rs.getString("fecha_guardado"));
                
                favoritos.add(fav);
            }
        }
        
        return favoritos;
    }
}