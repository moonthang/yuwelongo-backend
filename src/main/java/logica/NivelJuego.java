package logica;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "niveles_juego")
public class NivelJuego {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nivel")
    private int idNivel;
    private String nombre;
    private String descripcion;
    private int orden;
    private String estado = "Activo";
    
    public NivelJuego() {
    }

    public NivelJuego(int idNivel, String nombre, String descripcion, int orden, List<PreguntaJuego> pregunta) {
        this.idNivel = idNivel;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.orden = orden;
    }

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}