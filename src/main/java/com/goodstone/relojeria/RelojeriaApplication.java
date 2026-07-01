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
						new Categoria(null, "Deportivos", "Relojes resistentes y funcionales para el deporte y la aventura"));
				Categoria luxury = categoriaService.guardar(
						new Categoria(null, "Luxury", "Relojes de alta gama con diseño sofisticado y materiales premium"));

				// Relojes Deportivos.
				productoService.guardar(new Producto(null, "Canyon Trail", 179995.00, 10,
						"URL_CANYON_TRAIL",
						deportivos));
				productoService.guardar(new Producto(null, "Midnight Explorer", 159995.00, 8,
						"URL_MIDNIGHT_EXPLORER",
						deportivos));
				productoService.guardar(new Producto(null, "Adventure Matic", 149995.00, 15,
						"URL_ADVENTURE_MATIC",
						deportivos));
				productoService.guardar(new Producto(null, "Forest Ranger", 189995.00, 5,
						"URL_FOREST_RANGER",
						deportivos));

				// Relojes Luxury.
				productoService.guardar(new Producto(null, "Minimal Blue", 179000.00, 6,
						"URL_MINIMAL_BLUE",
						luxury));
				productoService.guardar(new Producto(null, "Timeless Tech", 169995.00, 9,
						"URL_TIMELESS_TECH",
						luxury));
				productoService.guardar(new Producto(null, "Daily Driver", 169995.00, 12,
						"URL_DAILY_DRIVER",
						luxury));
				productoService.guardar(new Producto(null, "Mechanic MD4", 169800.00, 7,
						"URL_MECHANIC_MD4",
						luxury));
			}
		};
	}
}