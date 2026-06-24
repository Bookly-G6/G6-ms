# Documentación de Nuevos Endpoints (Flujo Logística y Catálogo)

Tras la integración de las nuevas funcionalidades, se han expuesto nuevos endpoints que mejoran la gestión de envíos y la auditoría de productos. Aquí se detalla cada uno, sus requerimientos y cómo integrarlos desde el Frontend de forma lógica y consistente.

---

## 1. Flujo de Envíos (Logística)

### ¿Cómo debería usarse en el Frontend?

El flujo ideal para el panel de administración logística sería el siguiente:

1. **Panel Principal (Bandeja de Entrada de Envíos):**
   El administrador ingresa a la sección de envíos. El frontend llama a `GET /api/v1/envios/pendientes`. Esto devuelve una tabla paginada de todos los envíos que están en preparación. Se puede usar la barra de búsqueda integrada (`terminoBusqueda`) para filtrar.
2. **Preparación del Paquete (Ver Detalle):**
   El administrador hace clic en un envío específico para armar la caja. El frontend llama a `GET /api/v1/envios/{id}/detalle-completo`. Con esta información, el frontend muestra los productos exactos que debe contener la caja y la dirección del cliente para imprimir la etiqueta de envío.
3. **Despacho del Paquete (Actualizar Estado):**
   El administrador entrega el paquete al correo. El frontend muestra un modal pidiendo el número de tracking y la empresa de correo. Al confirmar, el frontend hace un `PATCH /api/v1/envios/{idEnvio}/estado` cambiando el estado a `EN_CAMINO`.
4. **Entrega Final:**
   Una vez que el paquete llega a destino, se puede volver a usar el `PATCH` para cambiar el estado a `ENTREGADO`. Todo este proceso deja automáticamente un registro en la tabla `HistorialEnvio`.

### Endpoints de Logística

#### A. Listar Envíos Pendientes
* **Ruta:** `GET /api/v1/envios/pendientes`
* **Autorización:** `ROLE_ADMIN`
* **Query Params (Opcionales):**
  * `idVenta` (UUID) - Filtrar por una venta específica.
  * `terminoBusqueda` (String) - Búsqueda libre (ej. cliente, dirección).
  * `page` (int) - Página (por defecto `0`).
  * `size` (int) - Tamaño de página (por defecto `20`).
* **Devuelve:** `Page<EnvioResponseDTO>` (Objeto de paginación de Spring Data).

#### B. Obtener Detalle Completo de Envío
* **Ruta:** `GET /api/v1/envios/{id}/detalle-completo`
* **Autorización:** Administradores o Clientes (implícito por configuración global).
* **Path Variable:** `id` (UUID del envío).
* **Devuelve:** `EnvioEnriquecidoDTO` (Incluye datos del envío, dirección, y lista de productos).

#### C. Actualizar Estado de Envío
* **Ruta:** `PATCH /api/v1/envios/{idEnvio}/estado`
* **Autorización:** `ROLE_ADMIN`
* **Path Variable:** `idEnvio` (UUID del envío).
* **Body Esperado (`ActualizarEstadoRequestDTO`):**
  ```json
  {
    "nuevoEstado": "EN_CAMINO", // Enum: EN_PREPARACION, EN_CAMINO, ENTREGADO, CANCELADO
    "numeroTracking": "TRK-123456", // Opcional
    "empresaCorreo": "Andreani", // Opcional
    "idEmpleado": "123e4567-e89b-12d3-a456-426614174000" // Obligatorio para auditoría
  }
  ```
* **Devuelve:** `EnvioResponseDTO` actualizado.

---

## 2. Flujo de Auditoría de Productos (Catálogo)

Las copias del módulo incorporaron trazabilidad en el catálogo. Cuando se actualiza un producto (`PUT /api/v1/productos/{id}`), el backend ahora revisa si el `stock` o el `precioCosto` / `precioActual` fueron modificados. Si hubo un cambio, se registra automáticamente en las tablas de historial de forma silenciosa para el usuario.

### Endpoint de Auditoría

#### A. Consultar Historial de Precios
Útil para mostrar una gráfica o tabla en el Frontend sobre cómo ha variado el precio de un producto en el tiempo.
* **Ruta:** `GET /api/v1/productos/{id}/historial-precios`
* **Autorización:** `ROLE_ADMIN`
* **Path Variable:** `id` (UUID del producto).
* **Devuelve:** Lista de `HistorialPrecioResponseDTO`:
  ```json
  [
    {
      "idHistorial": "uuid",
      "precioCostoAnterior": 100.00,
      "precioVentaAnterior": 150.00,
      "precioCostoNuevo": 110.00,
      "precioVentaNuevo": 165.00,
      "fechaCambio": "2026-06-24T06:15:21",
      "empleadoNombre": "Juan Pérez"
    }
  ]
  ```
