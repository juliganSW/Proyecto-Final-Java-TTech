package com.goodstone.relojeria.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carrito_producto")
public class CarritoProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
     @JsonBackReference
    // Muchos CarritoProducto pertenecen a un Carrito.
    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;
 
    // Muchos CarritoProducto pueden referenciar un mismo Producto.
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Positive(message = "La cantidad debe ser mayor que cero")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
}