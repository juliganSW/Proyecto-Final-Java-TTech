package com.goodstone.relojeria.exception;

public class CategoriaNotFoundException extends RuntimeException {
    public CategoriaNotFoundException(Integer id) {
        super("No se encontró ninguna categoría con el id: " + id);
    }
}