package com.goodstone.relojeria.exception;

public class CarritoNotFoundException extends RuntimeException {
    public CarritoNotFoundException(Integer id) {
        super("No se encontró ningún carrito con el id: " + id);
    }
}
