package logica;

import jakarta.persistence.*;

@Entity
@Table(name = "juego")
public class Juego {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_juego")
    private int idJuego;
    private int puntaje;
    private String fechaJuego;
    private int preguntasCorrectas;
    private int totalPreguntas;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "id_nivel")
    private NivelJuego nivel;

    public Juego() {
    }

    public Juego(int idJuego, int puntaje, String fechaJuego, int preguntasCorrectas, int totalPreguntas, Usuario usuario, NivelJuego nivel) {
        this.idJuego = idJuego;
        this.puntaje = puntaje;
        this.fechaJuego = fechaJuego;
        this.preguntasCorrectas = preguntasCorrectas;
        this.totalPreguntas = totalPreguntas;
        this.usuario = usuario;
        this.nivel = nivel;
    }

    public int getIdJuego() {
        return idJuego;
    }

    public void setIdJuego(int idJuego) {
        this.idJuego = idJuego;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public String getFechaJuego() {
        return fechaJuego;
    }

    public void setFechaJuego(String fechaJuego) {
        this.fechaJuego = fechaJuego;
    }

    public int getPreguntasCorrectas() {
        return preguntasCorrectas;
    }

    public void setPreguntasCorrectas(int preguntasCorrectas) {
        this.preguntasCorrectas = preguntasCorrectas;
    }

    public int getTotalPreguntas() {
        return totalPreguntas;
    }

    public void setTotalPreguntas(int totalPreguntas) {
        this.totalPreguntas = totalPreguntas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public NivelJuego getNivel() {
        return nivel;
    }

    public void setNivel(NivelJuego nivel) {
        this.nivel = nivel;
    }
}
