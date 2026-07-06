package com.goodstone.relojeria.service;

import com.goodstone.relojeria.exception.CategoriaNotFoundException;
import com.goodstone.relojeria.model.Categoria;
import com.goodstone.relojeria.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    // Inyeccion de dependencias por constructor 
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // Devuelve todas las categorias guardadas en la BBDD
    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    // Busca una categoria por id. Si no existe, tira la excepción
    public Categoria obtenerPorId(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException(id));
    }

    // Guarda una categoria nueva y valida que no exista otra con el mismo nombre.
    public Categoria guardar(Categoria categoria) {
        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
        return categoriaRepository.save(categoria);
    }

    // Actualiza una categoria existente
    public Categoria actualizar(Integer id, Categoria categoriaActualizada) {
        Categoria categoriaExistente = obtenerPorId(id);
        categoriaExistente.setNombre(categoriaActualizada.getNombre());
        categoriaExistente.setDescripcion(categoriaActualizada.getDescripcion());
        return categoriaRepository.save(categoriaExistente);
    }

    // Elimina una categoria por id
    public void eliminar(Integer id) {
        obtenerPorId(id);
        categoriaRepository.deleteById(id);
    }
}