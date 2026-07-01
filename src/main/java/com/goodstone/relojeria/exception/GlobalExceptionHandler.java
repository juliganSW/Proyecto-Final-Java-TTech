package com.goodstone.relojeria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Maneja los errores de validacion de Bean Validation (@NotBlank, @Positive, etc.)
    // Cuando un @Valid falla, Spring lanza MethodArgumentNotValidException.
    // Devolvemos un mapa con el nombre del campo y el mensaje de error.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    // Maneja ProductoNotFoundException -> 404 Not Found.
    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductoNotFound(ProductoNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Maneja CategoriaNotFoundException -> 404 Not Found.
    @ExceptionHandler(CategoriaNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCategoriaNotFound(CategoriaNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Maneja CarritoNotFoundException -> 404 Not Found.
    @ExceptionHandler(CarritoNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCarritoNotFound(CarritoNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Maneja StockInsuficienteException -> 400 Bad Request.
    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<Map<String, String>> handleStockInsuficiente(StockInsuficienteException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Maneja IllegalArgumentException -> 400 Bad Request.
    // Cubre casos como categoria duplicada o producto no encontrado en el carrito.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Maneja cualquier otra excepcion no contemplada -> 500 Internal Server Error.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
    }

    // Metodo auxiliar que arma el mapa de error estandar para todas las respuestas.
    private ResponseEntity<Map<String, String>> buildErrorResponse(HttpStatus status, String mensaje) {
        Map<String, String> error = new HashMap<>();
        error.put("error", mensaje);
        return ResponseEntity.status(status).body(error);
    }
}