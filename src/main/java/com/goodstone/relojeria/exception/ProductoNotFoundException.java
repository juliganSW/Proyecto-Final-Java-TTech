package com.goodstone.relojeria.exception;

public class ProductoNotFoundException extends RuntimeException {
    public ProductoNotFoundException(Integer id) {
        super("No se encontró ningún producto con el id: " + id);
    }
}