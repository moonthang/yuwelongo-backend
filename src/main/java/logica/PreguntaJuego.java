package logica;

import jakarta.persistence.*;

@Entity
@Table(name = "preguntas_juego")
public class PreguntaJuego {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private int idPregunta;
    
    @Column(name = "pregunta_texto")
    private String preguntaTexto;
    private String opcion1;
    private String opcion2;
    private String opcion3;
    private String opcion4;
    private String respuestaCoreccta;
    private int xpValor;
    private String imagenUrl;
    
    @ManyToOne
    @JoinColumn(name = "id_palabra")
    private Palabra palabra;
    
    @ManyToOne
    @JoinColumn(name = "id_nivel")
    private NivelJuego nivel;

    public PreguntaJuego() {
    }

    public PreguntaJuego(int idPregunta, String preguntaTexto, String opcion1, String opcion2, String opcion3, String opcion4, String respuestaCoreccta, int xpValor, String imagenUrl, Palabra palabra, NivelJuego nivel) {
        this.idPregunta = idPregunta;
        this.preguntaTexto = preguntaTexto;
        this.opcion1 = opcion1;
        this.opcion2 = opcion2;
        this.opcion3 = opcion3;
        this.opcion4 = opcion4;
        this.respuestaCoreccta = respuestaCoreccta;
        this.xpValor = xpValor;
        this.imagenUrl = imagenUrl;
        this.palabra = palabra;
        this.nivel = nivel;
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getPreguntaTexto() {
        return preguntaTexto;
    }

    public void setPreguntaTexto(String preguntaTexto) {
        this.preguntaTexto = preguntaTexto;
    }

    public String getOpcion1() {
        return opcion1;
    }

    public void setOpcion1(String opcion1) {
        this.opcion1 = opcion1;
    }

    public String getOpcion2() {
        return opcion2;
    }

    public void setOpcion2(String opcion2) {
        this.opcion2 = opcion2;
    }

    public String getOpcion3() {
        return opcion3;
    }

    public void setOpcion3(String opcion3) {
        this.opcion3 = opcion3;
    }

    public String getOpcion4() {
        return opcion4;
    }

    public void setOpcion4(String opcion4) {
        this.opcion4 = opcion4;
    }

    public String getRespuestaCoreccta() {
        return respuestaCoreccta;
    }

    public void setRespuestaCoreccta(String respuestaCoreccta) {
        this.respuestaCoreccta = respuestaCoreccta;
    }

    public int getXpValor() {
        return xpValor;
    }

    public void setXpValor(int xpValor) {
        this.xpValor = xpValor;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Palabra getPalabra() {
        return palabra;
    }

    public void setPalabra(Palabra palabra) {
        this.palabra = palabra;
    }

    public NivelJuego getNivel() {
        return nivel;
    }

    public void setNivel(NivelJuego nivel) {
        this.nivel = nivel;
    }
}