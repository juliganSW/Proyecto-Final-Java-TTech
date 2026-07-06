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

    // Maneja los errores de validacion de Bean Validation
    // Devuelve un mapa con el nombre del campo y el mensaje de error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    // Maneja ProductoNotFoundException
    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductoNotFound(ProductoNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Maneja CategoriaNotFoundException 
    @ExceptionHandler(CategoriaNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCategoriaNotFound(CategoriaNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Maneja CarritoNotFoundException
    @ExceptionHandler(CarritoNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCarritoNotFound(CarritoNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Maneja StockInsuficienteException 
    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<Map<String, String>> handleStockInsuficiente(StockInsuficienteException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Maneja casos como categoria duplicada o producto no encontrado
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Maneja cualquier excepcion no contemplada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
    }

    private ResponseEntity<Map<String, String>> buildErrorResponse(HttpStatus status, String mensaje) {
        Map<String, String> error = new HashMap<>();
        error.put("error", mensaje);
        return ResponseEntity.status(status).body(error);
    }
}