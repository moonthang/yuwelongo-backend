package rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.inject.Inject;
import java.util.List;
import logica.PreguntaJuego;
import logica.NivelJuego;
import logica.Palabra;
import services.PreguntaJuegoService;
import services.NivelJuegoService;
import services.PalabraService;

@Path("/preguntas-juego")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PreguntaJuegoResource {

    @Inject
    private PreguntaJuegoService preguntaService;

    @Inject
    private NivelJuegoService nivelService;

    @Inject
    private PalabraService palabraService;

    @POST
    public PreguntaJuego crear(PreguntaJuego pregunta) {
        if (pregunta.getNivel() == null || pregunta.getNivel().getIdNivel() == 0)
            throw new WebApplicationException("Debe incluir un nivel válido", 400);

        NivelJuego nivel = nivelService.obtenerNivelPorId(pregunta.getNivel().getIdNivel());
        if (nivel == null)
            throw new WebApplicationException("Nivel no encontrado", 404);

        if (pregunta.getPalabra() != null && pregunta.getPalabra().getIdPalabra() != 0) {
            Palabra palabra = palabraService.buscarPorId(pregunta.getPalabra().getIdPalabra());
            if (palabra == null)
                throw new WebApplicationException("Palabra no encontrada", 404);
            pregunta.setPalabra(palabra);
        }

        pregunta.setNivel(nivel);
        return preguntaService.crear(pregunta);
    }

    @GET
    public List<PreguntaJuego> obtenerTodas() {
        return preguntaService.obtenerTodasLasPreguntas();
    }

    @GET
    @Path("/{id}")
    public PreguntaJuego obtenerPorId(@PathParam("id") int id) {
        PreguntaJuego p = preguntaService.obtenerPreguntaPorId(id);
        if (p == null)
            throw new WebApplicationException("Pregunta no encontrada", 404);
        return p;
    }

    @GET
    @Path("/nivel/{idNivel}")
    public List<PreguntaJuego> obtenerPorNivel(@PathParam("idNivel") int idNivel,
                                               @QueryParam("aleatorias") @DefaultValue("false") boolean aleatorias,
                                               @QueryParam("cantidad") @DefaultValue("10") int cantidad) {
        if (aleatorias)
            return preguntaService.obtenerPreguntasAleatoriasPorNivel(idNivel, cantidad);
        return preguntaService.obtenerPreguntasPorNivel(idNivel);
    }

    @PUT
    @Path("/{id}")
    public PreguntaJuego actualizar(@PathParam("id") int id, PreguntaJuego datos) {
        PreguntaJuego existente = preguntaService.obtenerPreguntaPorId(id);
        if (existente == null)
            throw new WebApplicationException("Pregunta no encontrada", 404);

        if (datos.getPreguntaTexto() != null) existente.setPreguntaTexto(datos.getPreguntaTexto());
        if (datos.getOpcion1() != null) existente.setOpcion1(datos.getOpcion1());
        if (datos.getOpcion2() != null) existente.setOpcion2(datos.getOpcion2());
        if (datos.getOpcion3() != null) existente.setOpcion3(datos.getOpcion3());
        if (datos.getOpcion4() != null) existente.setOpcion4(datos.getOpcion4());
        if (datos.getRespuestaCoreccta() != null) existente.setRespuestaCoreccta(datos.getRespuestaCoreccta());
        if (datos.getXpValor() != 0) existente.setXpValor(datos.getXpValor());
        if (datos.getImagenUrl() != null) existente.setImagenUrl(datos.getImagenUrl());
        if (datos.getAudio_url() != null) existente.setAudio_url(datos.getAudio_url());

        if (datos.getPalabra() != null && datos.getPalabra().getIdPalabra() != 0) {
            Palabra palabra = palabraService.buscarPorId(datos.getPalabra().getIdPalabra());
            if (palabra != null) existente.setPalabra(palabra);
        }

        if (datos.getNivel() != null && datos.getNivel().getIdNivel() != 0) {
            NivelJuego nivel = nivelService.obtenerNivelPorId(datos.getNivel().getIdNivel());
            if (nivel != null) existente.setNivel(nivel);
        }

        return preguntaService.editar(existente);
    }

    @DELETE
    @Path("/{id}")
    public void eliminar(@PathParam("id") int id) {
        PreguntaJuego p = preguntaService.obtenerPreguntaPorId(id);
        if (p == null)
            throw new WebApplicationException("Pregunta no encontrada", 404);
        preguntaService.eliminar(id);
    }
}