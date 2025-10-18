package rest;

import logica.Palabra;
import logica.Categoria;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import services.PalabraService;
import services.CategoriaService;

@Stateless
public class PalabraResource {

    @Inject
    private PalabraService palabraService;

    @Inject
    private CategoriaService categoriaService;

    public boolean crearPalabra(Palabra palabra, int idCategoria) {
        Categoria categoria = categoriaService.buscarPorId(idCategoria);
        if (categoria == null) return false;

        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        palabra.setFechaCreacion(fecha);
        palabra.setCategoria(categoria);

        palabraService.crear(palabra);
        return true;
    }

    public boolean actualizarPalabra(Palabra palabra, int idCategoria) {
        Categoria categoria = categoriaService.buscarPorId(idCategoria);
        if (categoria != null) palabra.setCategoria(categoria);
        palabraService.editar(palabra);
        return true;
    }

    public boolean eliminarPalabra(int id) {
        Palabra p = palabraService.buscarPorId(id);
        if (p != null) {
            palabraService.eliminar(p);
            return true;
        }
        return false;
    }

    public Palabra buscarPorId(int id) {
        return palabraService.buscarPorId(id);
    }

    public List<Palabra> listarTodas() {
        return palabraService.listarTodas();
    }

    public List<Palabra> buscarPorPalabraNasa(String texto) {
        return palabraService.buscarPorPalabraNasa(texto);
    }

    public List<Palabra> buscarPorTraduccion(String texto) {
        return palabraService.buscarPorTraduccion(texto);
    }
}