package com.goodstone.relojeria;

import com.goodstone.relojeria.model.Categoria;
import com.goodstone.relojeria.model.Producto;
import com.goodstone.relojeria.service.CategoriaService;
import com.goodstone.relojeria.service.ProductoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RelojeriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RelojeriaApplication.class, args);
	}

	@Bean
	CommandLineRunner cargarDatos(CategoriaService categoriaService,
								  ProductoService productoService) {
		return args -> {
			if (categoriaService.obtenerTodas().isEmpty()) {

				// Creamos las categorias base.
				Categoria deportivos = categoriaService.guardar(
						new Categoria(null, "Deportivos", "Relojes resistentes para el deporte y la aventura"));
				Categoria luxury = categoriaService.guardar(
						new Categoria(null, "Luxury", "Relojes de alta gama diseñados con materiales premium"));

				// Relojes Deportivos.
				productoService.guardar(new Producto(null, "Canyon Trail", 179995.00, 10,
						"https://i.postimg.cc/FFbjWR1F/reloj-hombre1.png",
						deportivos));
				productoService.guardar(new Producto(null, "Midnight Explorer", 159995.00, 8,
						"https://i.postimg.cc/K8jB8Df4/reloj-hombre2.png",
						deportivos));
				productoService.guardar(new Producto(null, "Adventure Matic", 149995.00, 15,
						"https://i.postimg.cc/FKJSjF24/reloj-hombre6.png",
						deportivos));
				productoService.guardar(new Producto(null, "Forest Ranger", 189995.00, 5,
						"https://i.postimg.cc/VvzbQq5f/reloj-hombre7.png",
						deportivos));

				// Relojes Luxury.
				productoService.guardar(new Producto(null, "Minimal Blue", 179000.00, 6,
						"https://i.postimg.cc/v85vJh2s/reloj-dama1.png",
						luxury));
				productoService.guardar(new Producto(null, "Timeless Tech", 169995.00, 9,
						 "https://i.postimg.cc/XvTwhBwq/reloj-dama2.png",
						luxury));
				productoService.guardar(new Producto(null, "Daily Driver", 169995.00, 12,
						"https://i.postimg.cc/3xfjXcNg/reloj-dama5.png",
						luxury));
				productoService.guardar(new Producto(null, "Mechanic MD4", 169800.00, 7,
						"https://i.postimg.cc/K8jB8Df4/reloj-hombre8.png",
						luxury));
			}
		};
	}
}