package logica;

import jakarta.persistence.*;

@Entity
@Table(name = "preguntas_juego")
public class PreguntaJuego {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private int idPregunta;
    
    private String pregunta_texto;
    private String opcion1;
    private String opcion2;
    private String opcion3;
    private String opcion4;
    private String respuesta_correcta;
    private int xp_valor;
    private String imagen_url;
    private String audio_url;
    
    @ManyToOne
    @JoinColumn(name = "id_palabra")
    private Palabra palabra;
    
    @ManyToOne
    @JoinColumn(name = "id_nivel")
    private NivelJuego nivel;

    public PreguntaJuego() {
    }

    public PreguntaJuego(int idPregunta, String pregunta_texto, String opcion1, String opcion2, String opcion3, String opcion4, String respuesta_correcta, int xp_valor, String imagen_url, String audio_url, Palabra palabra, NivelJuego nivel) {
        this.idPregunta = idPregunta;
        this.pregunta_texto = pregunta_texto;
        this.opcion1 = opcion1;
        this.opcion2 = opcion2;
        this.opcion3 = opcion3;
        this.opcion4 = opcion4;
        this.respuesta_correcta = respuesta_correcta;
        this.xp_valor = xp_valor;
        this.imagen_url = imagen_url;
        this.audio_url = audio_url;
        this.palabra = palabra;
        this.nivel = nivel;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getPreguntaTexto() {
        return pregunta_texto;
    }

    public void setPreguntaTexto(String pregunta_texto) {
        this.pregunta_texto = pregunta_texto;
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
        return respuesta_correcta;
    }

    public void setRespuestaCoreccta(String respuesta_correcta	) {
        this.respuesta_correcta	 = respuesta_correcta;
    }

    public int getXpValor() {
        return xp_valor;
    }

    public void setXpValor(int xp_valor) {
        this.xp_valor = xp_valor;
    }

    public String getImagenUrl() {
        return imagen_url;
    }

    public void setImagenUrl(String imagen_url) {
        this.imagen_url = imagen_url;
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