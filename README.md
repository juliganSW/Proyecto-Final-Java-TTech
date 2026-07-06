# Goodstone Watch Store — API REST con Spring Boot

Backend de un e-commerce de relojería desarrollado con Java y Spring Boot, conectado a una base de datos MySQL. Incluye un frontend reciclado y adaptado de un proyecto anterior del curso Frontend con JS de Talento Tech, conectado a la API mediante JavaScript

---

## Descripción del proyecto

Goodstone Watch Store es una API REST que permite gestionar un catálogo de relojes con un sistema de carrito de compras. El frontend consume la API dinámicamente y simula una experiencia de compra. 
Se incluye a un panel de administración para gestionar los productos. Se accede a dicho panel, haciendo click en el ícono de user (no hay login para facilitar el acceso)

---

## Tecnologías utilizadas

- **Java 21**
- **Spring Boot 3.x**
- **Spring Data JPA / Hibernate**
- **MySQL** (administrado con MySQL Workbench)
- **Lombok**
- **Hibernate Validator**
- **HTML, CSS y JavaScript** 
- **SweetAlert2** y **Toastify** (para la información visual en el frontend)

---

## Estructura del proyecto

```
com.goodstone.relojeria
├── config
│   └── WebConfig.java
├── controller
│   ├── CategoriaController.java
│   ├── ProductoController.java
│   └── CarritoController.java
├── exception
│   ├── GlobalExceptionHandler.java
│   ├── CategoriaNotFoundException.java
│   ├── ProductoNotFoundException.java
│   ├── CarritoNotFoundException.java
│   └── StockInsuficienteException.java
├── model
│   ├── Categoria.java
│   ├── Producto.java
│   ├── Carrito.java
│   └── CarritoProducto.java
├── repository
│   ├── CategoriaRepository.java
│   ├── ProductoRepository.java
│   ├── CarritoRepository.java
│   └── CarritoProductoRepository.java
├── service
│   ├── CategoriaService.java
│   ├── ProductoService.java
│   └── CarritoService.java
└── RelojeriaApplication.java
```

## Relaciones entre entidades

- **Producto → Categoria**: relación `@ManyToOne` — cada producto pertenece a una categoría.
- **Carrito → Producto**: relación `@ManyToMany` resuelta mediante la entidad intermedia `CarritoProducto`, que incorpora el atributo extra `cantidad`.

---

## Requisitos previos

- Java 21 instalado
- MySQL Workbench (o cualquier cliente MySQL) con el servidor corriendo en el puerto 3306
- Maven (incluido en el proyecto con Maven Wrapper)

---

## Instrucciones para ejecutar la aplicación

1. **Crear la base de datos en MySQL Workbench**
```sql
CREATE DATABASE relojeria;
```

2. **Configurar las credenciales en `application.properties`**

El archivo se encuentra en `src/main/resources/application.properties`. Verificar que el usuario y contraseña coincidan con tu instalación de MySQL:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/relojeria
spring.datasource.username=root
spring.datasource.password=tu_password
```

3. **Levantar la aplicación**

La aplicación va a arrancar en `http://localhost:8080`. Al iniciarse por primera vez, carga automáticamente las categorías y productos de ejemplo en la base de datos.

5. **Abrir el frontend**

Abrir `src/main/resources/static/index.html` con Live Server desde VS Code, o navegar directamente a `http://localhost:8080/index.html`.


## Endpoints disponibles para las pruebas

### Categorías

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/categorias` | Lista todas las categorías |
| GET | `/categorias/{id}` | Obtiene una categoría por id |
| POST | `/categorias` | Crea una categoría nueva |
| PUT | `/categorias/{id}` | Actualiza una categoría |
| DELETE | `/categorias/{id}` | Elimina una categoría |

### Productos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/productos` | Lista todos los productos |
| GET | `/productos/{id}` | Obtiene un producto por id |
| GET | `/productos/buscar?modelo=` | Busca productos por nombre |
| GET | `/productos/categoria/{categoriaId}` | Filtra productos por categoría |
| POST | `/productos` | Crea un producto nuevo |
| PUT | `/productos/{id}` | Actualiza un producto |
| DELETE | `/productos/{id}` | Elimina un producto |

### Carrito

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/carritos` | Lista todos los carritos |
| GET | `/carritos/{id}` | Obtiene un carrito con sus productos |
| POST | `/carritos` | Crea un carrito vacío |
| POST | `/carritos/{carritoId}/productos/{productoId}?cantidad=` | Agrega un producto al carrito |
| DELETE | `/carritos/{carritoId}/productos/{productoId}` | Elimina un producto del carrito |
| DELETE | `/carritos/{id}/vaciar` | Vacía el carrito |
| DELETE | `/carritos/{id}` | Elimina el carrito completo |

---

## Frontend

El frontend fue reciclado y adaptado de un proyecto anterior. Consiste en páginas HTML/CSS conectadas a la API mediante JavaScript con `fetch()`. Incluye:

- **index.html** — Catálogo de productos cargado dinámicamente desde la API.
- **carrito.html** — Vista del carrito de compras con opción de eliminar productos y vaciar el carrito.
- **admin.html** — Panel de administración para agregar, editar y eliminar productos

---
## Autor

Julián Piedrabuena
