package com.goodstone.relojeria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Un carrito puede tener muchos productos (a traves de la tabla intermedia CarritoProducto).
    // cascade = ALL -> si guardamos o borramos el carrito, se propaga a sus CarritoProducto.
    // orphanRemoval = true -> si sacamos un CarritoProducto de la lista, se borra de la base.
    @JsonManagedReference
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoProducto> items = new ArrayList<>();
}