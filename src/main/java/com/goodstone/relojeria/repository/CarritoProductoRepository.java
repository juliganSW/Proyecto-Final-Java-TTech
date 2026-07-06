package com.goodstone.relojeria.repository;

import com.goodstone.relojeria.model.CarritoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoProductoRepository extends JpaRepository<CarritoProducto, Integer> {

    // Busca si ya existe un producto dentro de ese carrito en particular
    Optional<CarritoProducto> findByCarritoIdAndProductoId(Integer carritoId, Integer productoId);
}