package com.goodstone.relojeria.service;

import com.goodstone.relojeria.exception.CarritoNotFoundException;
import com.goodstone.relojeria.exception.StockInsuficienteException;
import com.goodstone.relojeria.model.Carrito;
import com.goodstone.relojeria.model.CarritoProducto;
import com.goodstone.relojeria.model.Producto;
import com.goodstone.relojeria.repository.CarritoProductoRepository;
import com.goodstone.relojeria.repository.CarritoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final CarritoProductoRepository carritoProductoRepository;
    private final ProductoService productoService;

    public CarritoService(CarritoRepository carritoRepository,
                          CarritoProductoRepository carritoProductoRepository,
                          ProductoService productoService) {
        this.carritoRepository = carritoRepository;
        this.carritoProductoRepository = carritoProductoRepository;
        this.productoService = productoService;
    }

    // Devuelve todos los carritos guardados en la base.
    public List<Carrito> obtenerTodos() {
        return carritoRepository.findAll();
    }

    // Busca un carrito por id. Si no existe, lanza la excepcion personalizada.
    public Carrito obtenerPorId(Integer id) {
        return carritoRepository.findById(id)
                .orElseThrow(() -> new CarritoNotFoundException(id));
    }

    // Crea un carrito nuevo y vacio.
    public Carrito crear() {
        return carritoRepository.save(new Carrito());
    }

    // Agrega un producto al carrito. 
    // Si el producto ya estaba en el carrito, suma la cantidad.
    // Si no estaba, crea un item nuevo.
    // En ambos casos valida que haya stock suficiente.
    public Carrito agregarProducto(Integer carritoId, Integer productoId, Integer cantidad) {

        // Verificamos que el carrito y el producto existan.
        Carrito carrito = obtenerPorId(carritoId);
        Producto producto = productoService.obtenerPorId(productoId);

        // Validacion de negocio: verificamos que haya stock suficiente.
        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException(producto.getModelo(), producto.getStock());
        }

        // Buscamos si el producto ya estaba en el carrito.
        Optional<CarritoProducto> itemExistente = carritoProductoRepository
                .findByCarritoIdAndProductoId(carritoId, productoId);

        if (itemExistente.isPresent()) {
            // El producto ya estaba: sumamos la cantidad pedida a la existente.
            CarritoProducto item = itemExistente.get();
            Integer nuevaCantidad = item.getCantidad() + cantidad;

            // Volvemos a validar que el stock alcance para la cantidad total.
            if (producto.getStock() < nuevaCantidad) {
                throw new StockInsuficienteException(producto.getModelo(), producto.getStock());
            }
            item.setCantidad(nuevaCantidad);
            carritoProductoRepository.save(item);
        } else {
            // El producto no estaba: creamos un item nuevo en la tabla intermedia.
            CarritoProducto nuevoItem = new CarritoProducto();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(cantidad);
            carritoProductoRepository.save(nuevoItem);
        }

        // Devolvemos el carrito actualizado.
        return obtenerPorId(carritoId);
    }

    // Elimina un producto especifico del carrito.
    public Carrito eliminarProducto(Integer carritoId, Integer productoId) {
        // Verificamos que el carrito exista.
        obtenerPorId(carritoId);

        // Buscamos el item en la tabla intermedia.
        CarritoProducto item = carritoProductoRepository
                .findByCarritoIdAndProductoId(carritoId, productoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El producto con id " + productoId + " no está en el carrito " + carritoId));

        carritoProductoRepository.delete(item);
        return obtenerPorId(carritoId);
    }

    // Vacia el carrito eliminando todos sus items, pero mantiene el carrito en la base.
    public Carrito vaciarCarrito(Integer carritoId) {
        Carrito carrito = obtenerPorId(carritoId);
        carrito.getItems().clear();
        return carritoRepository.save(carrito);
    }

    // Elimina el carrito completo de la base de datos.
    public void eliminar(Integer carritoId) {
        obtenerPorId(carritoId);
        carritoRepository.deleteById(carritoId);
    }
}
