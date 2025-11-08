package logica;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "palabras")
public class Palabra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_palabra")
    private int idPalabra;

    @Column(name = "palabra_nasa", nullable = false)
    private String palabraNasa;

    private String traduccion;
    private String frase_ejemplo;
    private String imagen_url;
    private String audio_url;
    private String fecha_creacion;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    public Palabra() {
    }

    public Palabra(int idPalabra, String palabraNasa, String traduccion, String frase_ejemplo, String imagen_url, String audio_url, String fecha_creacion, Categoria categoria, List<PreguntaJuego> preguntas) {
        this.idPalabra = idPalabra;
        this.palabraNasa = palabraNasa;
        this.traduccion = traduccion;
        this.frase_ejemplo = frase_ejemplo;
        this.imagen_url = imagen_url;
        this.audio_url = audio_url;
        this.fecha_creacion = fecha_creacion;
        this.categoria = categoria;
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

    public String getTraduccion() {
        return traduccion;
    }

    public void setTraduccion(String traduccion) {
        this.traduccion = traduccion;
    }

    public String getFraseEjemplo() {
        return frase_ejemplo;
    }

    public void setFraseEjemplo(String frase_ejemplo) {
        this.frase_ejemplo = frase_ejemplo;
    }

    public String getImagenUrl() {
        return imagen_url;
    }

    public void setImagenUrl(String imagen_url) {
        this.imagen_url = imagen_url;
    }

    public String getAudioUrl() {
        return audio_url;
    }

    public void setAudioUrl(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getFechaCreacion() {
        return fecha_creacion;
    }

    public void setFechaCreacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}