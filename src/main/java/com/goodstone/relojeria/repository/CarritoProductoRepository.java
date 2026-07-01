package com.goodstone.relojeria.repository;

import com.goodstone.relojeria.model.CarritoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoProductoRepository extends JpaRepository<CarritoProducto, Integer> {

    // Busca si ya existe un item con ese producto dentro de ese carrito especifico.
    // Util para cuando el usuario agrega un producto que ya estaba en el carrito:
    // en vez de crear un item nuevo, sumamos la cantidad al existente.
    Optional<CarritoProducto> findByCarritoIdAndProductoId(Integer carritoId, Integer productoId);
}