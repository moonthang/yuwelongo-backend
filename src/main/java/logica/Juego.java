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
    private String fecha_juego;
    private int preguntas_correctas;
    private int totalPreguntas;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "id_nivel")
    private NivelJuego nivel;

    public Juego() {
    }

    public Juego(int idJuego, int puntaje, String fecha_juego, int preguntas_correctas, int totalPreguntas, Usuario usuario, NivelJuego nivel) {
        this.idJuego = idJuego;
        this.puntaje = puntaje;
        this.fecha_juego = fecha_juego;
        this.preguntas_correctas = preguntas_correctas;
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
        return fecha_juego;
    }

    public void setFechaJuego(String fecha_juego) {
        this.fecha_juego = fecha_juego;
    }

    public int getPreguntasCorrectas() {
        return preguntas_correctas;
    }

    public void setPreguntasCorrectas(int preguntas_correctas) {
        this.preguntas_correctas = preguntas_correctas;
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