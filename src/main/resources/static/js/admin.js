// URL base de la API de Spring Boot
const API_URL = "http://localhost:8080";

// ---------------------------------------------------------------
// CARGAR Y RENDERIZAR PRODUCTOS
// ---------------------------------------------------------------

async function cargarProductosAdmin() {
    const contenedor = document.getElementById("contenedor-admin");
    if (!contenedor) return;

    try {
        const response = await fetch(`${API_URL}/productos`);
        const productos = await response.json();
        renderizarProductosAdmin(productos);
    } catch (error) {
        console.error("Error al cargar productos:", error);
    }
}

function renderizarProductosAdmin(productos) {
    const contenedor = document.getElementById("contenedor-admin");
    contenedor.innerHTML = "";

    productos.forEach(producto => {
        const card = document.createElement("div");
        card.classList.add("product");
        card.innerHTML = `
            <img src="${producto.imagenUrl}" alt="${producto.modelo}">
            <div class="product-info">
                <h2 class="product-title">${producto.modelo}</h2>
                <p class="product-categoria">${producto.categoria ? producto.categoria.nombre : ""}</p>
                <p>$${producto.precio.toLocaleString("es-AR")}</p>
                <p class="product-stock">Stock: ${producto.stock}</p>
                <div class="admin-botones">
                    <button class="product-btn btn-editar" onclick="editarProducto(${producto.id})">
                        </i> Editar
                    </button>
                    <button class="product-btn btn-eliminar-admin" onclick="eliminarProducto(${producto.id}, '${producto.modelo}')">
                        </i> Eliminar
                    </button>
                </div>
            </div>
        `;
        contenedor.appendChild(card);
    });
}

// ---------------------------------------------------------------
// AGREGAR PRODUCTO
// ---------------------------------------------------------------

async function abrirFormularioAgregar() {
    // Traemos las categorias para mostrarlas en el select
    const categorias = await obtenerCategorias();
    const opcionesCategorias = categorias.map(c =>
        `<option value="${c.id}">${c.nombre}</option>`
    ).join("");

    const { value: formValues } = await Swal.fire({
        title: "Agregar Producto",
        html: `
            <input id="swal-modelo" class="swal2-input" placeholder="Modelo del reloj">
            <input id="swal-precio" class="swal2-input" type="number" placeholder="Precio">
            <input id="swal-stock" class="swal2-input" type="number" placeholder="Stock">
            <input id="swal-imagen" class="swal2-input" placeholder="URL de la imagen">
            <select id="swal-categoria" class="swal2-select">
                ${opcionesCategorias}
            </select>
        `,
        confirmButtonText: "Agregar",
        confirmButtonColor: "#39f2ae",
        cancelButtonText: "Cancelar",
        cancelButtonColor: "#e74c3c",
        showCancelButton: true,
        focusConfirm: false,
        preConfirm: () => {
            const modelo = document.getElementById("swal-modelo").value.trim();
            const precio = parseFloat(document.getElementById("swal-precio").value);
            const stock = parseInt(document.getElementById("swal-stock").value);
            const imagenUrl = document.getElementById("swal-imagen").value.trim();
            const categoriaId = parseInt(document.getElementById("swal-categoria").value);

            if (!modelo || !precio || stock === undefined || !imagenUrl || !categoriaId) {
                Swal.showValidationMessage("Completá todos los campos");
                return false;
            }

            return { modelo, precio, stock, imagenUrl, categoria: { id: categoriaId } };
        }
    });

    if (!formValues) return;

    try {
        const response = await fetch(`${API_URL}/productos`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(formValues)
        });

        if (response.ok) {
            Toastify({
                text: `✔ ${formValues.modelo} agregado correctamente`,
                duration: 3000,
                gravity: "top",
                position: "right",
                backgroundColor: "#39f2ae"
            }).showToast();
            cargarProductosAdmin();
        } else {
            const error = await response.json();
            Swal.fire("Error", error.error || "No se pudo agregar el producto", "error");
        }
    } catch (error) {
        console.error("Error al agregar producto:", error);
    }
}

// ---------------------------------------------------------------
// EDITAR PRODUCTO
// ---------------------------------------------------------------

async function editarProducto(id) {
    // Traemos los datos actuales del producto y las categorias
    const [productoRes, categorias] = await Promise.all([
        fetch(`${API_URL}/productos/${id}`).then(r => r.json()),
        obtenerCategorias()
    ]);

    const opcionesCategorias = categorias.map(c =>
        `<option value="${c.id}" ${c.id === productoRes.categoria?.id ? "selected" : ""}>${c.nombre}</option>`
    ).join("");

    const { value: formValues } = await Swal.fire({
        title: "Editar Producto",
        html: `
            <input id="swal-modelo" class="swal2-input" placeholder="Modelo del reloj" value="${productoRes.modelo}">
            <input id="swal-precio" class="swal2-input" type="number" placeholder="Precio" value="${productoRes.precio}">
            <input id="swal-stock" class="swal2-input" type="number" placeholder="Stock" value="${productoRes.stock}">
            <input id="swal-imagen" class="swal2-input" placeholder="URL de la imagen" value="${productoRes.imagenUrl}">
            <select id="swal-categoria" class="swal2-select">
                ${opcionesCategorias}
            </select>
        `,
        confirmButtonText: "Guardar",
        confirmButtonColor: "#39f2ae",
        cancelButtonText: "Cancelar",
        cancelButtonColor: "#e74c3c",
        showCancelButton: true,
        focusConfirm: false,
        preConfirm: () => {
            const modelo = document.getElementById("swal-modelo").value.trim();
            const precio = parseFloat(document.getElementById("swal-precio").value);
            const stock = parseInt(document.getElementById("swal-stock").value);
            const imagenUrl = document.getElementById("swal-imagen").value.trim();
            const categoriaId = parseInt(document.getElementById("swal-categoria").value);

            if (!modelo || !precio || stock === undefined || !imagenUrl || !categoriaId) {
                Swal.showValidationMessage("Completá todos los campos");
                return false;
            }

            return { modelo, precio, stock, imagenUrl, categoria: { id: categoriaId } };
        }
    });

    if (!formValues) return;

    try {
        const response = await fetch(`${API_URL}/productos/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(formValues)
        });

        if (response.ok) {
            Toastify({
                text: `✔ ${formValues.modelo} actualizado correctamente`,
                duration: 3000,
                gravity: "top",
                position: "right",
                backgroundColor: "#39f2ae"
            }).showToast();
            cargarProductosAdmin();
        } else {
            const error = await response.json();
            Swal.fire("Error", error.error || "No se pudo actualizar el producto", "error");
        }
    } catch (error) {
        console.error("Error al editar producto:", error);
    }
}

// ---------------------------------------------------------------
// ELIMINAR PRODUCTO
// ---------------------------------------------------------------

async function eliminarProducto(id, modelo) {
    const confirmacion = await Swal.fire({
        title: "¿Eliminar producto?",
        text: `¿Estás seguro que querés eliminar "${modelo}"? Esta acción no se puede deshacer.`,
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#e74c3c",
        cancelButtonColor: "#39f2ae",
        confirmButtonText: "Sí, eliminar",
        cancelButtonText: "Cancelar"
    });

    if (!confirmacion.isConfirmed) return;

    try {
        const response = await fetch(`${API_URL}/productos/${id}`, {
            method: "DELETE"
        });

        if (response.ok) {
            Toastify({
                text: `${modelo} eliminado correctamente`,
                duration: 3000,
                gravity: "top",
                position: "right",
                backgroundColor: "#e74c3c"
            }).showToast();
            cargarProductosAdmin();
        } else {
            const error = await response.json();
            Swal.fire("Error", error.error || "No se pudo eliminar el producto", "error");
        }
    } catch (error) {
        console.error("Error al eliminar producto:", error);
    }
}

// ---------------------------------------------------------------
// HELPER: obtener categorias para los selects
// ---------------------------------------------------------------

async function obtenerCategorias() {
    try {
        const response = await fetch(`${API_URL}/categorias`);
        return await response.json();
    } catch (error) {
        console.error("Error al obtener categorías:", error);
        return [];
    }
}

// ---------------------------------------------------------------
// INICIALIZACIÓN
// ---------------------------------------------------------------

document.addEventListener("DOMContentLoaded", () => {
    cargarProductosAdmin();

    const btnAgregar = document.getElementById("btn-agregar-producto");
    if (btnAgregar) {
        btnAgregar.addEventListener("click", abrirFormularioAgregar);
    }
});
