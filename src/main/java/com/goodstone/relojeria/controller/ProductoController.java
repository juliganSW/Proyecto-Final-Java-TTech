package com.goodstone.relojeria.controller;

import com.goodstone.relojeria.model.Producto;
import com.goodstone.relojeria.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // GET /productos -> devuelve todos los productos.
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    // GET /productos/{id} -> devuelve un producto por id.
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    // GET /productos/buscar?modelo=canyon -> busca productos por nombre.
    // Util para el buscador del front.
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorModelo(@RequestParam String modelo) {
        return ResponseEntity.ok(productoService.buscarPorModelo(modelo));
    }

    // GET /productos/categoria/{categoriaId} -> devuelve productos de una categoria especifica.
    // Util para filtrar el catalogo por Deportivos o Luxury.
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> obtenerPorCategoria(@PathVariable Integer categoriaId) {
        return ResponseEntity.ok(productoService.obtenerPorCategoria(categoriaId));
    }

    // POST /productos -> crea un producto nuevo.
    @PostMapping
    public ResponseEntity<Producto> guardar(@Valid @RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.guardar(producto));
    }

    // PUT /productos/{id} -> actualiza un producto existente.
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Integer id,
                                               @Valid @RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.actualizar(id, producto));
    }

    // DELETE /productos/{id} -> elimina un producto por id.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
