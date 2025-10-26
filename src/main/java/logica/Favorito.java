package logica;

import jakarta.persistence.*;

@Entity
@Table(name = "favoritos")
public class Favorito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_favorito")
    private int idFavorito;
    private String fecha_guardado;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "id_palabra")
    private Palabra palabra;

    public Favorito() {
    }

    public Favorito(int idFavorito, String fecha_guardado, Usuario usuario, Palabra palabra) {
        this.idFavorito = idFavorito;
        this.fecha_guardado = fecha_guardado;
        this.usuario = usuario;
        this.palabra = palabra;
    }

    public int getIdFavorito() {
        return idFavorito;
    }

    public void setIdFavorito(int idFavorito) {
        this.idFavorito = idFavorito;
    }

    public String getFechaGuardado() {
        return fecha_guardado;
    }

    public void setFechaGuardado(String fecha_guardado) {
        this.fecha_guardado = fecha_guardado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Palabra getPalabra() {
        return palabra;
    }

    public void setPalabra(Palabra palabra) {
        this.palabra = palabra;
    }
}
