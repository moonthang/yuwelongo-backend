package models;

public class Favorito {
    private int idFavorito;
    private int idUsuario;
    private int idPalabra;
    private String palabraNasa;
    private String traduccionEspanol;
    private String imagenUrl;
    private String categoria;
    private String fechaAgregado;
    
    public Favorito() {
    }
    
    public Favorito(int idFavorito, int idUsuario, int idPalabra, String palabraNasa, 
                    String traduccionEspanol, String imagenUrl, String categoria, 
                    String fechaAgregado) {
        this.idFavorito = idFavorito;
        this.idUsuario = idUsuario;
        this.idPalabra = idPalabra;
        this.palabraNasa = palabraNasa;
        this.traduccionEspanol = traduccionEspanol;
        this.imagenUrl = imagenUrl;
        this.categoria = categoria;
        this.fechaAgregado = fechaAgregado;
    }
    
    public int getIdFavorito() {
        return idFavorito;
    }
    
    public void setIdFavorito(int idFavorito) {
        this.idFavorito = idFavorito;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public int getIdPalabra() {
        return idPalabra;
    }
    
    public void setIdPalabra(int idPalabra) {
        this.idPalabra = idPalabra;
    }
    
    public String getPalabraNasa() {
        return palabraNasa;
    }
    
    public void setPalabraNasa(String palabraNasa) {
        this.palabraNasa = palabraNasa;
    }
    
    public String getTraduccionEspanol() {
        return traduccionEspanol;
    }
    
    public void setTraduccionEspanol(String traduccionEspanol) {
        this.traduccionEspanol = traduccionEspanol;
    }
    
    public String getImagenUrl() {
        return imagenUrl;
    }
    
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public String getFechaAgregado() {
        return fechaAgregado;
    }
    
    public void setFechaAgregado(String fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
    }
}