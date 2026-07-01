package com.goodstone.relojeria.exception;

public class StockInsuficienteException extends RuntimeException {
    public StockInsuficienteException(String modelo, Integer stockDisponible) {
        super("Stock insuficiente para el producto '" + modelo + "'. Stock disponible: " + stockDisponible);
    }
}
    

