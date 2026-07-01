package com.goodstone.relojeria.service;

import com.goodstone.relojeria.exception.ProductoNotFoundException;
import com.goodstone.relojeria.model.Categoria;
import com.goodstone.relojeria.model.Producto;
import com.goodstone.relojeria.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;

    public ProductoService(ProductoRepository productoRepository, CategoriaService categoriaService) {
        this.productoRepository = productoRepository;
        this.categoriaService = categoriaService;
    }

    // Devuelve todos los productos guardados en la base.
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    // Busca un producto por id. Si no existe, lanza la excepcion personalizada.
    public Producto obtenerPorId(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(id));
    }

    // Busca productos cuyo modelo contenga el texto recibido (para el buscador del front).
    public List<Producto> buscarPorModelo(String modelo) {
        return productoRepository.findByModeloContainingIgnoreCase(modelo);
    }

    // Devuelve todos los productos de una categoria especifica (para filtrar por Deportivos/Luxury).
    public List<Producto> obtenerPorCategoria(Integer categoriaId) {
        // Verificamos que la categoria exista antes de buscar sus productos.
        categoriaService.obtenerPorId(categoriaId);
        return productoRepository.findByCategoriaId(categoriaId);
    }

    // Guarda un producto nuevo. Verifica que la categoria asignada exista en la base.
    public Producto guardar(Producto producto) {
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            Categoria categoria = categoriaService.obtenerPorId(producto.getCategoria().getId());
            producto.setCategoria(categoria);
        }
        return productoRepository.save(producto);
    }

    // Actualiza un producto existente. Verifica primero que exista.
    public Producto actualizar(Integer id, Producto productoActualizado) {
        Producto productoExistente = obtenerPorId(id);
        productoExistente.setModelo(productoActualizado.getModelo());
        productoExistente.setPrecio(productoActualizado.getPrecio());
        productoExistente.setStock(productoActualizado.getStock());
        productoExistente.setImagenUrl(productoActualizado.getImagenUrl());

        // Si viene una categoria nueva, verificamos que exista antes de asignarla.
        if (productoActualizado.getCategoria() != null && productoActualizado.getCategoria().getId() != null) {
            Categoria categoria = categoriaService.obtenerPorId(productoActualizado.getCategoria().getId());
            productoExistente.setCategoria(categoria);
        }

        return productoRepository.save(productoExistente);
    }

    // Elimina un producto por id. Verifica primero que exista.
    public void eliminar(Integer id) {
        obtenerPorId(id);
        productoRepository.deleteById(id);
    }
}