package com.goodstone.relojeria.repository;

import com.goodstone.relojeria.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    // Busca categorias cuyo nombre contenga el texto recibido, sin importar mayusculas/minusculas.
    // Spring Data JPA genera la consulta SQL automaticamente a partir del nombre del metodo.
    boolean existsByNombre(String nombre);
}