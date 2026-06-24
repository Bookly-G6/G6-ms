# API REST

## Configuración Local

Para consumir esta API localmente:

**Base URL:** `http://localhost:8080/api/v1`

---

# Módulo 1: Catálogo de Productos

Todas las rutas de este módulo nacen del endpoint `/productos`. Los IDs manejados por el sistema son de tipo **UUID**.

## Endpoints Disponibles

| Método   | Endpoint          | Descripción                                  | Requiere Body |
| :------- | :---------------- | :------------------------------------------- | :-----------: |
| `POST`   | `/productos`      | Crea un nuevo producto en el catálogo        |       Sí      |
| `PUT`    | `/productos/{id}` | Actualiza los datos de un producto existente |       Sí      |
| `DELETE` | `/productos/{id}` | Realiza un borrado lógico (Soft Delete)      |       No      |

---

## Payload de Creación y Actualización

Tanto para crear (`POST`) como para actualizar (`PUT`), el backend espera recibir un objeto JSON con la siguiente estructura.

> **Importante:** Las claves foráneas (`idTipoProducto`, `idEditorialSello`, `idRangoEtario`) deben existir previamente en la base de datos o la API devolverá un error.

```json
{
  "codigoBarras": "978-987-566-068-4",
  "nombreProducto": "Ficciones",
  "descripcion": "Libro de cuentos de Jorge Luis Borges",
  "precioActual": 22500.00,
  "idTipoProducto": 1,
  "idEditorialSello": 1,
  "idRangoEtario": 1
}
```

---

# Módulo 2: Logística y Envíos

Todas las rutas de este módulo nacen del endpoint `/envios`. Este módulo administra el flujo físico de los paquetes y mantiene una trazabilidad inmutable (historial) de cada cambio de estado. Los IDs utilizados son de tipo **UUID**.

## Endpoints Disponibles

| Método  | Endpoint                   | Descripción                                                  | Requiere Body |
| :------ | :------------------------- | :----------------------------------------------------------- | :-----------: |
| `POST`  | `/envios`                  | Inicializa un nuevo envío atado a una venta                  |       Sí      |
| `PATCH` | `/envios/{idEnvio}/estado` | Actualiza el estado logístico y genera un registro histórico |       Sí      |
| `GET`   | `/envios`                  | Obtiene el listado completo de envíos activos                |       No      |
| `GET`   | `/envios/venta/{idVenta}`  | Obtiene los detalles logísticos usando el ID de la Venta     |       No      |

---

## Diccionario de Datos (Enums)

El sistema es estricto y solo acepta los siguientes valores para garantizar la integridad de las reglas de negocio.

### Tipos de Envío Permitidos (`tipoEnvio`)

* `DOMICILIO`
* `RETIRO_SUCURSAL`

### Estados Logísticos Permitidos (`estadoLogistica` / `nuevoEstado`)

* `EN_PREPARACION` (Estado inicial por defecto al crear)
* `LISTO_PARA_RETIRO`
* `DESPACHADO`
* `EN_CAMINO`
* `ENTREGADO`
* `CANCELADO`
* `DEVUELTO`

---

## Payload 1: Inicialización del Envío (`POST /envios`)

Se dispara automáticamente cuando una venta es confirmada. El sistema asignará el estado `EN_PREPARACION` por defecto. Si el tipo es `RETIRO_SUCURSAL`, el backend generará automáticamente un código alfanumérico (Ej: `RET-A1B2C3`).

### Ejemplo de Request

```json
{
  "idVenta": "77777777-7777-7777-7777-777777777777",
  "tipoEnvio": "DOMICILIO",
  "observaciones": "El timbre no funciona bien, golpear las manos por favor."
}
```

---

## Payload 2: Actualización de Estado (`PATCH /envios/{idEnvio}/estado`)

Utilizado por los empleados para avanzar el paquete en el flujo logístico. Todo cambio realizado por este endpoint genera automáticamente un registro inmutable en la tabla `historial_envio` vinculando al empleado responsable.

### Ejemplo de Request

```json
{
  "nuevoEstado": "DESPACHADO",
  "numeroTracking": "AR-987654321X",
  "empresaCorreo": "Andreani",
  "idEmpleado": "e2e2e2e2-e2e2-e2e2-e2e2-e2e2e2e2e2e2"
}
```

> **Nota:** Los campos `numeroTracking` y `empresaCorreo` son opcionales y solo deben enviarse si el estado requiere datos de la transportista. El `idEmpleado` es obligatorio para mantener la auditoría.

---

# Módulo 3: GET para Frontend (Ventas)

Se agregaron endpoints GET en el módulo existente de ventas para devolver información legible (nombres y descripciones) en lugar de trabajar solo con IDs.

- Tipos de venta: `GET /ventas/tipos-venta`
- Formas de pago: `GET /ventas/formas-pago`
- Empleados: `GET /ventas/empleados`

Documentación detallada de respuestas y uso en frontend:

- `docs/ventas-get-catalogos.md`

---

# Referencia API Completa (Backend + Frontend)

Esta sección consolida todo el contrato API: endpoint, método, autenticación, rol, body, campos obligatorios/opcionales, respuesta esperada y validaciones/reglas de negocio.

## Reglas Globales

- Base URL: `http://localhost:8080/api/v1`
- Autenticación: `Authorization: Bearer <JWT>`
- Errores frecuentes:
  - `400`: validación de request o regla de negocio
  - `401`: token ausente, inválido o expirado
  - `403`: rol insuficiente
  - `404`: recurso no encontrado

### Roles

- `ADMIN`
- `CLIENTE`
- `VENDEDOR`

### Notas funcionales importantes

- Checkout de ventas usa sucursal fija `1` en backend.
- Movimiento de stock usa sucursal fija `1` en backend.
- En seguridad global, `GET /ventas/**` está restringido a `ADMIN` o `CLIENTE`.

---

## Módulo Auth

### `POST /auth/register`

- Token: No
- Rol: Público
- Body:

```json
{
  "nombre": "Luciano",
  "apellido": "Perez",
  "email": "luciano@mail.com",
  "password": "123456",
  "dni": "30111222",
  "telefono": "3704-123456"
}
```

- Campos:
  - Obligatorios: `nombre`, `apellido`, `email`, `password`
  - Opcionales: `dni`, `telefono`
- Validaciones:
  - `email` válido
  - `password` mínimo 6 caracteres
  - email único
- Respuesta:

```json
{
  "token": "jwt...",
  "idUsuario": "uuid",
  "email": "luciano@mail.com",
  "nombre": "Luciano",
  "apellido": "Perez",
  "rol": "CLIENTE"
}
```

### `POST /auth/login`

- Token: No
- Rol: Público
- Body:

```json
{
  "email": "luciano@mail.com",
  "password": "123456"
}
```

- Campos:
  - Obligatorios: `email`, `password`
- Validaciones:
  - credenciales válidas
  - usuario activo
- Respuesta: igual estructura de `AuthResponseDTO` (token + datos del usuario)

### `GET /auth/me`

- Token: Sí
- Rol: cualquier autenticado
- Body: No
- Respuesta:

```json
{
  "idUsuario": "uuid",
  "email": "luciano@mail.com",
  "nombre": "Luciano",
  "apellido": "Perez",
  "rol": "CLIENTE"
}
```

---

## Módulo Usuarios

### `GET /usuarios`

- Token: Sí
- Rol: `ADMIN`
- Body: No
- Respuesta: lista `UsuarioResponseDTO`

### `GET /usuarios/{id}`

- Token: Sí
- Rol: `ADMIN`
- Body: No
- Respuesta: `UsuarioResponseDTO`

### `POST /usuarios`

- Token: Sí
- Rol: `ADMIN`
- Body:

```json
{
  "nombre": "Ana",
  "apellido": "Lopez",
  "email": "ana@mail.com",
  "password": "123456",
  "dni": "28999111",
  "telefono": "3704-555555",
  "activo": true
}
```

- Campos:
  - Obligatorios: `nombre`, `apellido`, `email`, `password`
  - Opcionales: `dni`, `telefono`, `activo`
- Validaciones:
  - `email` válido y único
  - `password` mínimo 6
- Respuesta: `UsuarioResponseDTO`

### `PUT /usuarios/{id}`

- Token: Sí
- Rol: `ADMIN`
- Body:

```json
{
  "nombre": "Ana",
  "apellido": "Lopez",
  "email": "ana@mail.com",
  "password": "nueva123",
  "dni": "28999111",
  "telefono": "3704-111111",
  "activo": true
}
```

- Campos:
  - Obligatorios: `nombre`, `apellido`, `email`
  - Opcionales: `password`, `dni`, `telefono`, `activo`
- Validaciones:
  - `email` válido y no repetido en otro usuario
  - si viene `password`, mínimo 6
- Respuesta: `UsuarioResponseDTO`

### `PUT /usuarios/{id}/rol`

- Token: Sí
- Rol: `ADMIN`
- Body:

```json
{
  "nombreRol": "ADMIN"
}
```

- Campos:
  - Obligatorio: `nombreRol`
- Validaciones:
  - el rol debe existir
- Respuesta: `UsuarioResponseDTO`

### `DELETE /usuarios/{id}`

- Token: Sí
- Rol: `ADMIN`
- Body: No
- Respuesta: `204 No Content`

---

## Módulo Roles

### `GET /roles`

- Token: Sí
- Rol: `ADMIN`
- Body: No
- Respuesta: lista `RoleResponseDTO`

### `GET /roles/{id}`

- Token: Sí
- Rol: `ADMIN`
- Body: No
- Respuesta: `RoleResponseDTO`

### `POST /roles`

- Token: Sí
- Rol: `ADMIN`
- Body:

```json
{
  "nombreRol": "MODERADOR"
}
```

- Campos:
  - Obligatorio: `nombreRol`
- Validaciones:
  - `nombreRol` no puede estar vacío
  - `nombreRol` debe ser único
  - se normaliza a mayúsculas automáticamente
- Respuesta: `RoleResponseDTO`

### `PUT /roles/{id}`

- Token: Sí
- Rol: `ADMIN`
- Body:

```json
{
  "nombreRol": "MODERADOR_PREMIUM"
}
```

- Campos:
  - Obligatorio: `nombreRol`
- Validaciones:
  - `nombreRol` no puede estar vacío
  - `nombreRol` debe ser único (excepto el del rol siendo actualizado)
  - se normaliza a mayúsculas automáticamente
- Respuesta: `RoleResponseDTO`

### `DELETE /roles/{id}`

- Token: Sí
- Rol: `ADMIN`
- Body: No
- Respuesta: `204 No Content`
- Validación:
  - el rol a eliminar no debe tener usuarios asociados

---
## Módulo Catálogo de Productos

### `GET /productos`

- Token: No
- Rol: Público
- Body: No
- Respuesta: lista `ProductoResponseDTO` con nombres legibles (tipo, editorial, rango, categorías, autores)

### `GET /productos/{id}`

- Token: No
- Rol: Público
- Body: No
- Respuesta: `ProductoResponseDTO`
- Regla:
  - si está inactivo, responde como no encontrado

### `POST /productos`

- Token: Sí
- Rol: `ADMIN`
- Body:

```json
{
  "codigoBarras": "9789871234567",
  "nombreProducto": "Libro X",
  "descripcion": "Descripcion",
  "precioCosto": 10000.0,
  "precioActual": 15000.0,
  "idTipoProducto": 1,
  "idEditorialSello": 1,
  "idRangoEtario": 1,
  "idsCategorias": [1, 2],
  "atributosEspecificos": { "idioma": "ES" },
  "idsAutores": [1]
}
```

- Campos:
  - Obligatorios: `codigoBarras`, `nombreProducto`, `precioCosto`, `precioActual`, `idTipoProducto`, `idEditorialSello`, `idRangoEtario`, `idsCategorias`, `idsAutores`
  - Opcionales: `descripcion`, `atributosEspecificos`
- Validaciones:
  - `codigoBarras` entre 8 y 50
  - precios > 0
  - al menos 1 categoría y 1 autor
  - IDs de relaciones existentes
- Respuesta: `ProductoResponseDTO`

### `DELETE /productos/{id}`

- Token: Sí
- Rol: `ADMIN`
- Body: No
- Respuesta: `204 No Content`
- Regla:
  - soft delete (`activo=false`)

---

## Módulos Catálogo Simple

Aplica para:

- `/categorias`
- `/autores`
- `/editoriales`
- `/tipos-producto`
- `/rangos-etarios`

### GET de catálogo

- Token: No
- Rol: Público
- Body: No
- Respuesta: lista de entidades de catálogo

### POST de catálogo

- Token: Sí
- Rol efectivo: `ADMIN`
- Body ejemplos:

Categoría:

```json
{
  "nombreCategoria": "Ficcion",
  "activa": true
}
```

Autor:

```json
{
  "nombre": "Autor X",
  "biografia": "Texto",
  "activa": true
}
```

Editorial:

```json
{
  "nombreEditorial": "Planeta",
  "activa": true
}
```

Tipo producto:

```json
{
  "nombreTipoProducto": "Libro Fisico",
  "activa": true
}
```

Rango etario:

```json
{
  "descripcion": "Adultos"
}
```

### DELETE de catálogo (`/{id}`)

- Token: Sí
- Rol efectivo: `ADMIN`
- Body: No
- Respuesta: `204 No Content`

---

## Módulo Ventas

### `POST /ventas/checkout`

- Token: Sí
- Roles: `ADMIN`, `CLIENTE`, `VENDEDOR`
- Body:

```json
{
  "origenVenta": "WEB",
  "idCliente": "uuid-opcional",
  "idEmpleado": "uuid-opcional",
  "items": [
    {
      "idProducto": "uuid",
      "cantidad": 2,
      "idPromocion": null
    }
  ],
  "pagos": [
    {
      "idFormaPago": 1,
      "montoAbonado": 30000.0
    }
  ],
  "generarEnvio": true,
  "tipoEnvio": "DOMICILIO",
  "observacionesEnvio": "Entregar por la tarde"
}
```

- Campos:
  - Obligatorios: `origenVenta`, `items`, `pagos`
  - Opcionales: `idCliente`, `idEmpleado`, `generarEnvio`, `tipoEnvio`, `observacionesEnvio`
- Validaciones:
  - `items` mínimo 1
  - `pagos` mínimo 1
  - `cantidad >= 1`
  - `montoAbonado > 0`
  - stock suficiente
  - productos activos
  - forma de pago existente
  - total abonado >= total venta
  - si corresponde envío y no hay `tipoEnvio`, falla
- Reglas:
  - sucursal fija `1`
  - si rol VENDEDOR, origen forzado a `LOCAL`
- Respuesta: `VentaResponseDTO`

### `GET /ventas/tipos-venta`

- Token: Sí
- Rol efectivo: `ADMIN` o `CLIENTE`
- Body: No
- Respuesta:

```json
[
  {
    "codigo": "WEB",
    "nombre": "Venta web",
    "descripcion": "Compra online realizada por cliente autenticado",
    "requiereEmpleado": false,
    "generaEnvioAutomatico": true
  },
  {
    "codigo": "LOCAL",
    "nombre": "Venta local",
    "descripcion": "Venta presencial atendida por personal autorizado",
    "requiereEmpleado": true,
    "generaEnvioAutomatico": false
  }
]
```

### `GET /ventas/formas-pago`

- Token: Sí
- Rol efectivo: `ADMIN` o `CLIENTE`
- Body: No
- Respuesta:

```json
[
  {
    "idFormaPago": 1,
    "nombrePago": "EFECTIVO"
  }
]
```

### `GET /ventas/empleados`

- Token: Sí
- Rol efectivo actual: restringido por regla global de `/ventas/**`
- Body: No
- Respuesta:

```json
[
  {
    "idEmpleado": "uuid",
    "idPersona": "uuid",
    "idEmpleado": "uuid",
    "nombreCompleto": "Carlos Logistico",
    "nombre": "Carlos",
    "apellido": "Logistico",
    "dni": "20111222",
    "telefono": null
  }
]
```

### `GET /ventas`

- Token: Sí
- Rol: `ADMIN`
- Body: No
- Respuesta: lista `VentaResponseDTO`

### `GET /ventas/mis-ordenes`

- Token: Sí
- Rol: `CLIENTE`
- Body: No
- Respuesta: lista `VentaResponseDTO` del cliente autenticado

### `GET /ventas/{id}`

- Token: Sí
- Roles: `ADMIN`, `CLIENTE`
- Body: No
- Respuesta: `VentaResponseDTO`
- Regla:
  - cliente solo puede ver sus ventas

---

## Módulo Inventario y Movimiento de Stock

### `GET /inventario`

- Token: Sí
- Roles: `ADMIN`, `VENDEDOR`
- Body: No
- Respuesta: lista `InventarioResponseDTO`

### `GET /inventario/{idProducto}`

- Token: Sí
- Roles: `ADMIN`, `VENDEDOR`
- Body: No
- Respuesta: `InventarioResponseDTO`

### `GET /inventario/movimientos`

- Token: Sí
- Roles: `ADMIN`, `VENDEDOR`
- Body: No
- Respuesta: lista `MovimientoStockResponseDTO`

### `GET /inventario/movimientos/{idMovimiento}`

- Token: Sí
- Roles: `ADMIN`, `VENDEDOR`
- Body: No
- Respuesta: `MovimientoStockResponseDTO`

### `POST /inventario/movimientos`
### `POST /inventario/movimientos/entrada`
### `POST /inventario/movimientos/salida`

- Token: Sí
- Roles: `ADMIN`, `VENDEDOR`
- Body:

```json
{
  "idProducto": "uuid",
  "cantidad": 3,
  "tipoMovimiento": "ENTRADA",
  "idEmpleado": "uuid"
}
```

- Campos:
  - Obligatorios: `idProducto`, `cantidad`, `tipoMovimiento` (solo genérico), `idEmpleado`
  - No requiere sucursal
- Validaciones:
  - `cantidad >= 1`
  - `tipoMovimiento` válido: `ENTRADA` o `SALIDA`
  - producto y empleado existentes
  - no permite stock negativo
- Regla:
  - sucursal fija `1`
- Respuesta: `MovimientoStockResponseDTO`

### `PUT /inventario/movimientos/{idMovimiento}`

- Token: Sí
- Roles: `ADMIN`, `VENDEDOR`
- Body: igual al de creación de movimiento
- Respuesta: `MovimientoStockResponseDTO`
- Regla:
  - revierte impacto anterior y aplica el nuevo

### `DELETE /inventario/movimientos/{idMovimiento}`

- Token: Sí
- Roles: `ADMIN`, `VENDEDOR`
- Body: No
- Respuesta: `204 No Content`
- Regla:
  - revierte el movimiento antes de eliminar

---

## Módulo Carrito

### `GET /carrito/mio`

- Token: Sí
- Roles: `ADMIN`, `CLIENTE`
- Body: No
- Respuesta: `CarritoResponseDTO`

### `POST /carrito/items`

- Token: Sí
- Roles: `ADMIN`, `CLIENTE`
- Body:

```json
{
  "idProducto": "uuid",
  "cantidad": 1
}
```

- Campos:
  - Obligatorios: `idProducto`, `cantidad`
- Validaciones:
  - `cantidad >= 1`
  - producto existente y activo
- Reglas:
  - si ya existe el item, suma cantidad
- Respuesta: `CarritoResponseDTO` actualizado

### `PATCH /carrito/items/{idItem}`

- Token: Sí
- Roles: `ADMIN`, `CLIENTE`
- Body:

```json
{
  "cantidad": 4
}
```

- Campos:
  - Obligatorio: `cantidad`
- Validaciones:
  - `cantidad >= 1`
  - item pertenece al carrito del usuario
- Respuesta: `CarritoResponseDTO` actualizado

### `DELETE /carrito/items/{idItem}`

- Token: Sí
- Roles: `ADMIN`, `CLIENTE`
- Body: No
- Respuesta: `204 No Content`
- Validación:
  - item pertenece al carrito del usuario

---

## Módulo Logística y Envíos

### `GET /envios`

- Token: Sí
- Roles: `ADMIN`, `CLIENTE`
- Body: No
- Respuesta: lista `EnvioResponseDTO`
- Regla:
  - ADMIN ve todos los activos
  - CLIENTE ve solo los suyos

### `GET /envios/venta/{idVenta}`

- Token: Sí
- Roles: `ADMIN`, `CLIENTE`
- Body: No
- Respuesta: `EnvioResponseDTO`
- Regla:
  - CLIENTE solo si la venta le pertenece

### `POST /envios`

- Token: Sí
- Rol: `ADMIN`
- Body:

```json
{
  "idVenta": "uuid",
  "tipoEnvio": "DOMICILIO",
  "observaciones": "Texto opcional"
}
```

- Campos:
  - Obligatorios: `idVenta`, `tipoEnvio`
  - Opcional: `observaciones`
- `tipoEnvio` permitido:
  - `DOMICILIO`
  - `RETIRO_LOCAL`
  - `DIGITAL`
- Validaciones:
  - no puede existir otro envío activo para la misma venta
- Respuesta: `EnvioResponseDTO`

### `PATCH /envios/{idEnvio}/estado`

- Token: Sí
- Rol: `ADMIN`
- Body:

```json
{
  "nuevoEstado": "DESPACHADO",
  "numeroTracking": "AR123456",
  "empresaCorreo": "Andreani",
  "idEmpleado": "uuid"
}
```

- Campos:
  - Obligatorio: `nuevoEstado`
  - Opcionales: `numeroTracking`, `empresaCorreo`, `idEmpleado`
- `nuevoEstado` permitido:
  - `PENDIENTE`
  - `EN_PREPARACION`
  - `LISTO_PARA_RETIRO`
  - `DESPACHADO`
  - `ENTREGADO`
  - `CANCELADO`
  - `DEVUELTO`
- Respuesta: `EnvioResponseDTO`
- Regla:
  - registra historial de estado
