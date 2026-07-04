// URL base de la API de Spring Boot
const API_URL = "http://localhost:8080";

// ---------------------------------------------------------------
// MANEJO DEL CARRITO
// Guardamos el id del carrito en localStorage para mantenerlo
// entre páginas (index.html y carrito.html)
// ---------------------------------------------------------------

// Obtiene el id del carrito del localStorage, o crea uno nuevo si no existe.
async function obtenerOCrearCarrito() {
    let carritoId = localStorage.getItem("carritoId");

    if (!carritoId) {
        const response = await fetch(`${API_URL}/carritos`, {
            method: "POST"
        });
        const carrito = await response.json();
        localStorage.setItem("carritoId", carrito.id);
        carritoId = carrito.id;
    }

    return carritoId;
}

// ---------------------------------------------------------------
// CATÁLOGO DE PRODUCTOS (index.html)
// ---------------------------------------------------------------

// Carga todos los productos desde la API y los renderiza en el HTML.
async function cargarProductos() {
    const contenedor = document.getElementById("contenedor-productos");
    if (!contenedor) return;

    try {
        const response = await fetch(`${API_URL}/productos`);
        const productos = await response.json();
        renderizarProductos(productos);
    } catch (error) {
        console.error("Error al cargar productos:", error);
    }
}

// Genera el HTML de cada producto y lo inserta en el contenedor.
function renderizarProductos(productos) {
    const contenedor = document.getElementById("contenedor-productos");
    contenedor.innerHTML = "";

    productos.forEach(producto => {
        const card = document.createElement("div");
        card.classList.add("product");
        card.innerHTML = `
            <i class="fa-solid fa-heart"></i>
            <img src="${producto.imagenUrl}" alt="${producto.modelo}">
            <div class="product-info">
                <h2 class="product-title">${producto.modelo}</h2>
                <p class="product-categoria">${producto.categoria ? producto.categoria.nombre : ""}</p>
                <p>$${producto.precio.toLocaleString("es-AR")}</p>
                <p class="product-stock">Stock: ${producto.stock}</p>
                <button class="product-btn" onclick="agregarAlCarrito(${producto.id}, '${producto.modelo}')">
                    Agregar al Carrito
                </button>
            </div>
        `;
        contenedor.appendChild(card);
    });
}

// Agrega un producto al carrito llamando a la API.
async function agregarAlCarrito(productoId, modelo) {
    try {
        const carritoId = await obtenerOCrearCarrito();

        const response = await fetch(`${API_URL}/carritos/${carritoId}/productos/${productoId}?cantidad=1`, {
            method: "POST"
        });

        if (response.ok) {
            // Toastify para confirmar que se agregó el producto
            Toastify({
                text: `✔ ${modelo} agregado al carrito`,
                duration: 3000,
                gravity: "top",
                position: "right",
                backgroundColor: "#39f2ae",
                stopOnFocus: true
            }).showToast();
            actualizarContadorCarrito();
        } else {
            const error = await response.json();
            // SweetAlert para mostrar el error (ej: stock insuficiente)
            Swal.fire({
                title: "Error",
                text: error.error,
                icon: "error",
                confirmButtonColor: "#39f2ae"
            });
        }
    } catch (error) {
        console.error("Error al agregar al carrito:", error);
    }
}

// Actualiza el contador de items en el ícono del carrito del navbar.
async function actualizarContadorCarrito() {
    const carritoId = localStorage.getItem("carritoId");
    const contador = document.getElementById("contador-carrito");
    if (!carritoId || !contador) return;

    try {
        const response = await fetch(`${API_URL}/carritos/${carritoId}`);
        const carrito = await response.json();
        contador.textContent = carrito.items.length;
        contador.style.display = carrito.items.length > 0 ? "inline" : "none";
    } catch (error) {
        console.error("Error al actualizar contador:", error);
    }
}

// ---------------------------------------------------------------
// PÁGINA DEL CARRITO (carrito.html)
// ---------------------------------------------------------------

// Carga y muestra los items del carrito.
async function cargarCarrito() {
    const contenedor = document.getElementById("lista-carrito");
    if (!contenedor) return;

    const carritoId = localStorage.getItem("carritoId");

    if (!carritoId) {
        contenedor.innerHTML = "<p>No tenés ningún carrito activo.</p>";
        return;
    }

    try {
        const response = await fetch(`${API_URL}/carritos/${carritoId}`);
        const carrito = await response.json();
        renderizarCarrito(carrito);
    } catch (error) {
        console.error("Error al cargar el carrito:", error);
    }
}

// Genera el HTML del carrito con sus items.
function renderizarCarrito(carrito) {
    const contenedor = document.getElementById("lista-carrito");

    // Limpiamos el total siempre, antes de cualquier otra cosa.
    const totalDiv = document.getElementById("total-carrito");
    if (totalDiv) totalDiv.textContent = "";

    if (carrito.items.length === 0) {
        contenedor.innerHTML = "<p class='carrito-vacio'>Tu carrito está vacío.</p>";
        return;
    }

    let total = 0;
    contenedor.innerHTML = "";

    carrito.items.forEach(item => {
        total += item.producto.precio * item.cantidad;

        const fila = document.createElement("div");
        fila.classList.add("carrito-item");
        fila.innerHTML = `
            <img src="${item.producto.imagenUrl}" alt="${item.producto.modelo}">
            <div class="carrito-item-info">
                <h3>${item.producto.modelo}</h3>
                <p>Precio: $${item.producto.precio.toLocaleString("es-AR")}</p>
                <p>Cantidad: ${item.cantidad}</p>
                <p>Subtotal: $${(item.producto.precio * item.cantidad).toLocaleString("es-AR")}</p>
            </div>
            <button class="btn-eliminar" onclick="eliminarDelCarrito(${item.producto.id}, '${item.producto.modelo}')">
                <i class="fa-solid fa-trash"></i>
            </button>
        `;
        contenedor.appendChild(fila);
    });

    if (totalDiv) {
        totalDiv.textContent = `Total: $${total.toLocaleString("es-AR")}`;
    }
}

// Elimina un producto específico del carrito con confirmación.
async function eliminarDelCarrito(productoId, modelo) {
    const carritoId = localStorage.getItem("carritoId");
    if (!carritoId) return;

    const confirmacion = await Swal.fire({
        title: "¿Eliminar producto?",
        text: `¿Querés quitar ${modelo} del carrito?`,
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#39f2ae",
        cancelButtonColor: "#e74c3c",
        confirmButtonText: "Sí, eliminar",
        cancelButtonText: "Cancelar"
    });

    if (!confirmacion.isConfirmed) return;

    try {
        const response = await fetch(`${API_URL}/carritos/${carritoId}/productos/${productoId}`, {
            method: "DELETE"
        });

        if (response.ok) {
            Toastify({
                text: `${modelo} eliminado del carrito`,
                duration: 3000,
                gravity: "top",
                position: "right",
                backgroundColor: "#e74c3c",
                stopOnFocus: true
            }).showToast();
            cargarCarrito();
        } else {
            const error = await response.json();
            Swal.fire("Error", error.error, "error");
        }
    } catch (error) {
        console.error("Error al eliminar producto:", error);
    }
}

// Vacía todos los items del carrito.
async function vaciarCarrito() {
    const carritoId = localStorage.getItem("carritoId");
    if (!carritoId) return;

    const confirmacion = await Swal.fire({
        title: "¿Vaciar carrito?",
        text: "Se van a eliminar todos los productos del carrito",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#39f2ae",
        cancelButtonColor: "#e74c3c",
        confirmButtonText: "Sí, vaciar",
        cancelButtonText: "Cancelar"
    });

    if (!confirmacion.isConfirmed) return;

    try {
        const response = await fetch(`${API_URL}/carritos/${carritoId}/vaciar`, {
            method: "DELETE"
        });

        if (response.ok) {
            Toastify({
                text: "El carrito fue vaciado",
                duration: 3000,
                gravity: "top",
                position: "right",
                backgroundColor: "#e74c3c",
                stopOnFocus: true
            }).showToast();
            cargarCarrito();
        } else {
            const error = await response.json();
            Swal.fire("Error", error.error, "error");
        }
    } catch (error) {
        console.error("Error al vaciar el carrito:", error);
    }
}

// ---------------------------------------------------------------
// INICIALIZACIÓN
// ---------------------------------------------------------------
document.addEventListener("DOMContentLoaded", () => {
    cargarProductos();
    cargarCarrito();
    actualizarContadorCarrito();

    const btnVaciar = document.getElementById("vaciar-carrito");
    if (btnVaciar) {
        btnVaciar.addEventListener("click", vaciarCarrito);
    }
});