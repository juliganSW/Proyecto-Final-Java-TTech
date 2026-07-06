package com.goodstone.relojeria.controller;

import com.goodstone.relojeria.model.Categoria;
import com.goodstone.relojeria.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // Devuelve todas las categorias.
    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerTodas() {
        return ResponseEntity.ok(categoriaService.obtenerTodas());
    }

    // Devuelve una categoria por id.
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(categoriaService.obtenerPorId(id));
    }

    // Crea una categoria
    @PostMapping
    public ResponseEntity<Categoria> guardar(@Valid @RequestBody Categoria categoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.guardar(categoria));
    }

    // Actualiza una categoria existente.
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable Integer id,
                                                @Valid @RequestBody Categoria categoria) {
        return ResponseEntity.ok(categoriaService.actualizar(id, categoria));
    }

    // Elimina una categoria por id.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}