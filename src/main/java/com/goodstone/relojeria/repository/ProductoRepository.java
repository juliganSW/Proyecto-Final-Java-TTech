package com.goodstone.relojeria.repository;

import com.goodstone.relojeria.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Busca productos cuyo modelo contenga el texto recibido, sin importar mayusculas/minusculas.
    // Util para implementar un buscador en el front (ej: buscar "canyon").
    List<Producto> findByModeloContainingIgnoreCase(String modelo);

    // Busca todos los productos que pertenezcan a una categoria especifica por su id.
    // Util para filtrar el catalogo por categoria (ej: mostrar solo "Deportivos").
    List<Producto> findByCategoriaId(Integer categoriaId);
}
