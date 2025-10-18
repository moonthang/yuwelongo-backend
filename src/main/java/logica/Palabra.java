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
    private String fraseEjemplo;
    private String imagenUrl;
    private String audioUrl;
    private String fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @OneToMany(mappedBy = "palabra", cascade = CascadeType.ALL)
    private List<PreguntaJuego> preguntas;

    public Palabra() {
    }

    public Palabra(int idPalabra, String palabraNasa, String traduccion, String fraseEjemplo, String imagenUrl, String audioUrl, String fechaCreacion, Categoria categoria, List<PreguntaJuego> preguntas) {
        this.idPalabra = idPalabra;
        this.palabraNasa = palabraNasa;
        this.traduccion = traduccion;
        this.fraseEjemplo = fraseEjemplo;
        this.imagenUrl = imagenUrl;
        this.audioUrl = audioUrl;
        this.fechaCreacion = fechaCreacion;
        this.categoria = categoria;
        this.preguntas = preguntas;
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
        return fraseEjemplo;
    }

    public void setFraseEjemplo(String fraseEjemplo) {
        this.fraseEjemplo = fraseEjemplo;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<PreguntaJuego> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<PreguntaJuego> preguntas) {
        this.preguntas = preguntas;
    }
}