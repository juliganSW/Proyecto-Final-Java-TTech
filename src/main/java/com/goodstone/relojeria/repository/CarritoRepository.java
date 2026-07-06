package com.goodstone.relojeria.repository;

import com.goodstone.relojeria.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
   
}
