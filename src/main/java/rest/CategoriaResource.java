package rest;

import logica.Categoria;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import services.CategoriaService;

@Stateless
public class CategoriaResource {

    @Inject
    private CategoriaService categoriaService;

    public boolean crearCategoria(Categoria categoria) {
        categoria.setEstado("Activa");
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        categoria.setDescripcion((categoria.getDescripcion() == null ? "" : categoria.getDescripcion()) + " (Creado: " + fecha + ")");
        categoriaService.crear(categoria);
        return true;
    }

    public boolean actualizarCategoria(Categoria categoria) {
        categoriaService.editar(categoria);
        return true;
    }

    public boolean eliminarCategoria(int id) {
        Categoria c = categoriaService.buscarPorId(id);
        if (c != null) {
            categoriaService.eliminar(c);
            return true;
        }
        return false;
    }

    public Categoria buscarPorId(int id) {
        return categoriaService.buscarPorId(id);
    }

    public List<Categoria> listarTodas() {
        return categoriaService.listarTodas();
    }

    public List<Categoria> buscarPorNombre(String nombre) {
        return categoriaService.buscarPorNombre(nombre);
    }
}