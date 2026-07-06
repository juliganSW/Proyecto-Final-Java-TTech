package com.goodstone.relojeria.controller;

import com.goodstone.relojeria.model.Carrito;
import com.goodstone.relojeria.service.CarritoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carritos")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    // Devuelve todos los carritos
    @GetMapping
    public ResponseEntity<List<Carrito>> obtenerTodos() {
        return ResponseEntity.ok(carritoService.obtenerTodos());
    }

    // Devuelve un carrito por id con todos sus items
    @GetMapping("/{id}")
    public ResponseEntity<Carrito> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(carritoService.obtenerPorId(id));
    }

    // Crea un carrito nuevo
    @PostMapping
    public ResponseEntity<Carrito> crear() {
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoService.crear());
    }

    // Agrega un producto al carrito con la cantidad indicada
    @PostMapping("/{carritoId}/productos/{productoId}")
    public ResponseEntity<Carrito> agregarProducto(@PathVariable Integer carritoId,
                                                   @PathVariable Integer productoId,
                                                   @RequestParam(defaultValue = "1") Integer cantidad) {
        return ResponseEntity.ok(carritoService.agregarProducto(carritoId, productoId, cantidad));
    }

   // Elimina un producto del carrito.
    @DeleteMapping("/{carritoId}/productos/{productoId}")
    public ResponseEntity<Carrito> eliminarProducto(@PathVariable Integer carritoId,
                                                    @PathVariable Integer productoId) {
        return ResponseEntity.ok(carritoService.eliminarProducto(carritoId, productoId));
    }

    // Vacia el carrito pero lo mantiene en la base
    @DeleteMapping("/{id}/vaciar")
    public ResponseEntity<Carrito> vaciarCarrito(@PathVariable Integer id) {
        return ResponseEntity.ok(carritoService.vaciarCarrito(id));
    }

    // Elimina el carrito completo 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        carritoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}