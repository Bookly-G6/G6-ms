# Bookly - Manual de Endpoints para Postman y Frontend (README2)

Este documento detalla la totalidad de los endpoints del backend de **Bookly** para facilitar el armado de la colección de Postman, la simulación de pruebas y la integración con el Frontend.

---

## 1. Configuración de Base y Autenticación

### 1.1 URL Base
La URL base del backend por defecto es:
```
http://localhost:8080/api/v1
```
*(Nota: En algunos entornos de desarrollo locales puede correr bajo el puerto `8083`. Ajustar según corresponda).*

### 1.2 Encabezados (Headers)
Para cualquier endpoint que requiera autenticación, se debe incluir el token JWT en las cabeceras HTTP de la siguiente forma:
* **Key**: `Authorization`
* **Value**: `Bearer <token_jwt_generado>`

### 1.3 Roles de Usuario Disponibles
* `ROLE_ADMIN`: Administrador con acceso completo de lectura y escritura.
* `ROLE_CLIENTE`: Cliente final con permisos para gestionar su propio carrito, envíos, y ver/crear sus órdenes de compra.
* `ROLE_VENDEDOR`: Empleado de venta física que puede iniciar ventas/checkout y listar catálogo.

---

## 2. Contrato Estándar de Errores (para el Frontend)
Cuando la API devuelve un error (códigos HTTP `400`, `401`, `403`, `404`, `409`, `500`), el cuerpo de la respuesta tiene la estructura consistente de `ApiError`:

```json
{
  "timestamp": "2026-06-22T15:04:05",
  "status": 403,
  "error": "Forbidden",
  "code": "INSUFFICIENT_ROLE",
  "message": "No tienes permisos para acceder a este recurso.",
  "path": "/api/v1/usuarios",
  "details": [
    "Se requiere rol ADMIN."
  ]
}
```

### Códigos de Error (`code`) comunes:
* **`VALIDATION_ERROR` (400)**: Errores en campos del Body (el arreglo `details` indicará qué campo falló y su motivo).
* **`AUTH_REQUIRED` (401)**: Falta el token JWT o no se ha provisto cabecera de autorización.
* **`TOKEN_INVALID` (401)**: El token JWT está mal formado o no es válido.
* **`TOKEN_EXPIRED` (401)**: El token ha expirado.
* **`INSUFFICIENT_ROLE` (403)**: El usuario está autenticado pero no posee el rol requerido (ej: Cliente queriendo entrar a `/usuarios`).
* **`RESOURCE_NOT_OWNED` (403)**: El recurso solicitado existe pero pertenece a otro cliente y no está autorizado a verlo.
* **`RESOURCE_NOT_FOUND` (404)**: El recurso por ID o path no existe en el sistema.
* **`DATA_CONFLICT` (409)**: Conflicto con datos existentes (ej: duplicado de email).
* **`INTERNAL_ERROR` (500)**: Error inesperado en el servidor.

---

## 3. Detalle de Módulos y Endpoints

---

### Módulo: Autenticación (`/api/v1/auth`)

#### 1. Registrar un Usuario Nuevo
* **Endpoint**: `POST /api/v1/auth/register`
* **Token requerido**: No (Público)
* **Body (JSON)**:
  * **Obligatorios**:
    * `nombre` (String, no vacío)
    * `apellido` (String, no vacío)
    * `email` (String, formato email válido, no vacío)
    * `password` (String, mínimo 6 caracteres, no vacío)
  * **Opcionales**:
    * `dni` (String)
    * `telefono` (String)
* **Ejemplo Request Body**:
  ```json
  {
    "nombre": "Juan",
    "apellido": "Perez",
    "email": "juan.perez@mail.com",
    "password": "segura123password",
    "dni": "12345678A",
    "telefono": "+54911223344"
  }
  ```
* **Respuesta Esperada (201 Created)**:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJq...",
    "idUsuario": "0a5d4d50-c2e7-4c07-a35a-8f767fcb38ee",
    "email": "juan.perez@mail.com",
    "nombre": "Juan",
    "apellido": "Perez",
    "rol": "CLIENTE"
  }
  ```

#### 2. Iniciar Sesión (Login)
* **Endpoint**: `POST /api/v1/auth/login`
* **Token requerido**: No (Público)
* **Body (JSON)**:
  * **Obligatorios**:
    * `email` (String, formato email válido, no vacío)
    * `password` (String, no vacío)
* **Ejemplo Request Body**:
  ```json
  {
    "email": "juan.perez@mail.com",
    "password": "segura123password"
  }
  ```
* **Respuesta Esperada (200 OK)**:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJq...",
    "idUsuario": "0a5d4d50-c2e7-4c07-a35a-8f767fcb38ee",
    "email": "juan.perez@mail.com",
    "nombre": "Juan",
    "apellido": "Perez",
    "rol": "CLIENTE"
  }
  ```

#### 3. Obtener Datos del Usuario Actual (Me)
* **Endpoint**: `GET /api/v1/auth/me`
* **Token requerido**: Sí (Cualquier rol autenticado)
* **Body**: Ninguno.
* **Respuesta Esperada (200 OK)**:
  ```json
  {
    "idUsuario": "0a5d4d50-c2e7-4c07-a35a-8f767fcb38ee",
    "email": "juan.perez@mail.com",
    "nombre": "Juan",
    "apellido": "Perez",
    "rol": "CLIENTE"
  }
  ```

---

### Módulo: Usuarios (`/api/v1/usuarios`)
*Todos los endpoints de este módulo requieren un token con rol **`ROLE_ADMIN`**.*

#### 1. Listar Todos los Usuarios
* **Endpoint**: `GET /api/v1/usuarios`
* **Token requerido**: Sí (`ADMIN`)
* **Respuesta Esperada (200 OK)**:
  ```json
  [
    {
      "idUsuario": "0a5d4d50-c2e7-4c07-a35a-8f767fcb38ee",
      "idPersona": "6f2e96bc-cbdf-4a69-9237-775678af43aa",
      "nombre": "Juan",
      "apellido": "Perez",
      "dni": "12345678A",
      "telefono": "+54911223344",
      "email": "juan.perez@mail.com",
      "rol": "CLIENTE",
      "activo": true
    }
  ]
  ```

#### 2. Obtener Usuario por ID
* **Endpoint**: `GET /api/v1/usuarios/{id}`
* **Token requerido**: Sí (`ADMIN`)
* **Respuesta Esperada (200 OK)**:
  *(Mismo formato de objeto individual de la lista anterior).*

#### 3. Crear Usuario (vía Admin)
* **Endpoint**: `POST /api/v1/usuarios`
* **Token requerido**: Sí (`ADMIN`)
* **Body (JSON)**:
  * **Obligatorios**:
    * `nombre`, `apellido`, `email`, `password` (mínimo 6 caracteres).
  * **Opcionales**:
    * `dni`, `telefono`, `activo` (Boolean).
* **Ejemplo Request Body**:
  ```json
  {
    "nombre": "Soporte",
    "apellido": "Bookly",
    "email": "soporte@bookly.com",
    "password": "adminSecretPassword",
    "dni": "99999999Z",
    "telefono": "111-111",
    "activo": true
  }
  ```
* **Respuesta Esperada (201 Created)**:
  *(Devuelve el `UsuarioResponseDTO` creado).*

#### 4. Actualizar Usuario Completo
* **Endpoint**: `PUT /api/v1/usuarios/{id}`
* **Token requerido**: Sí (`ADMIN`)
* **Body (JSON)**:
  * **Obligatorios**:
    * `nombre`, `apellido`, `email`.
  * **Opcionales**:
    * `password` (si se envía, debe tener al menos 6 caracteres), `dni`, `telefono`, `activo`.
* **Ejemplo Request Body**:
  ```json
  {
    "nombre": "Juan Modificado",
    "apellido": "Perez",
    "email": "juan.perez@mail.com",
    "password": "nuevaContrasena123",
    "dni": "12345678A",
    "telefono": "+54911999999",
    "activo": true
  }
  ```
* **Respuesta Esperada (200 OK)**:
  *(Devuelve el `UsuarioResponseDTO` actualizado).*

#### 5. Actualizar Rol de Usuario
* **Endpoint**: `PUT /api/v1/usuarios/{id}/rol`
* **Token requerido**: Sí (`ADMIN`)
* **Body (JSON)**:
  * **Obligatorios**:
    * `nombreRol` (String, no vacío, ej: `ADMIN`, `CLIENTE`, `VENDEDOR`)
* **Ejemplo Request Body**:
  ```json
  {
    "nombreRol": "ADMIN"
  }
  ```
* **Respuesta Esperada (200 OK)**:
  *(Devuelve el `UsuarioResponseDTO` con el rol actualizado).*

#### 6. Eliminar Usuario
* **Endpoint**: `DELETE /api/v1/usuarios/{id}`
* **Token requerido**: Sí (`ADMIN`)
* **Respuesta Esperada (204 No Content)**: Sin body en la respuesta.

---

### Módulo: Roles (`/api/v1/roles`)
*Todos los endpoints de este módulo requieren un token con rol **`ROLE_ADMIN`**.*

#### 1. Listar Roles
* **Endpoint**: `GET /api/v1/roles`
* **Respuesta Esperada (200 OK)**:
  ```json
  [
    { "idRol": 1, "nombreRol": "ADMIN" },
    { "idRol": 2, "nombreRol": "CLIENTE" }
  ]
  ```

#### 2. Obtener Rol por ID
* **Endpoint**: `GET /api/v1/roles/{id}`
* **Respuesta Esperada (200 OK)**:
  `{ "idRol": 1, "nombreRol": "ADMIN" }`

#### 3. Crear Rol
* **Endpoint**: `POST /api/v1/roles`
* **Body**: `{ "nombreRol": "VENDEDOR" }` (Obligatorio: `nombreRol`)
* **Respuesta Esperada (201 Created)**:
  `{ "idRol": 3, "nombreRol": "VENDEDOR" }`

#### 4. Actualizar Rol
* **Endpoint**: `PUT /api/v1/roles/{id}`
* **Body**: `{ "nombreRol": "VENDEDOR_MODIFICADO" }`
* **Respuesta Esperada (200 OK)**:
  `{ "idRol": 3, "nombreRol": "VENDEDOR_MODIFICADO" }`

#### 5. Eliminar Rol
* **Endpoint**: `DELETE /api/v1/roles/{id}`
* **Respuesta (204 No Content)**: Sin body.

---

### Módulo: Catálogo - Productos (`/api/v1/productos`)

#### 1. Listar Productos Activos
* **Endpoint**: `GET /api/v1/productos`
* **Token requerido**: No (Público)
* **Respuesta Esperada (200 OK)**:
  ```json
  [
    {
      "idProducto": "9a5e1810-e79e-4a4b-91cc-cf4e82414777",
      "codigoBarras": "9789504979128",
      "nombreProducto": "El Hobbit",
      "descripcion": "Novela fantástica de J.R.R. Tolkien",
      "precioCosto": 5000.00,
      "precioActual": 9500.00,
      "activo": true,
      "tipoProducto": "Libro",
      "editorialSello": "Minotauro",
      "rangoEtario": "Adultos",
      "categorias": ["Fantasía", "Aventura"],
      "atributosEspecificos": {
        "paginas": 310,
        "idioma": "Español",
        "encuadernacion": "Tapa blanda"
      },
      "autores": ["J.R.R. Tolkien"]
    }
  ]
  ```

#### 2. Obtener Producto Activo por ID
* **Endpoint**: `GET /api/v1/productos/{id}`
* **Token requerido**: No (Público)
* **Respuesta Esperada (200 OK)**:
  *(Devuelve el `ProductoResponseDTO` individual del producto).*

#### 3. Crear Producto
* **Endpoint**: `POST /api/v1/productos`
* **Token requerido**: Sí (`ADMIN`)
* **Body (JSON)**:
  * **Obligatorios**:
    * `codigoBarras` (String, entre 8 y 50 caracteres)
    * `nombreProducto` (String, no vacío)
    * `precioCosto` (Decimal > 0)
    * `precioActual` (Decimal > 0)
    * `idTipoProducto` (Integer, ID de la tabla tipo_producto)
    * `idEditorialSello` (Integer, ID de la tabla editorial_sello)
    * `idRangoEtario` (Integer, ID de la tabla rango_etario)
    * `idsCategorias` (List<Integer>, al menos una categoría válida)
    * `idsAutores` (List<Integer>, al menos un autor válido)
  * **Opcionales**:
    * `descripcion` (String)
    * `atributosEspecificos` (Map/JSON conteniendo pares clave-valor dinámicos)
* **Ejemplo Request Body**:
  ```json
  {
    "codigoBarras": "9789504979128",
    "nombreProducto": "El Hobbit",
    "descripcion": "Edición especial ilustrada",
    "precioCosto": 5000.00,
    "precioActual": 9500.00,
    "idTipoProducto": 1,
    "idEditorialSello": 2,
    "idRangoEtario": 3,
    "idsCategorias": [1, 2],
    "idsAutores": [1],
    "atributosEspecificos": {
      "paginas": 310,
      "idioma": "Español"
    }
  }
  ```
* **Respuesta Esperada (201 Created)**:
  *(Retorna el `ProductoResponseDTO` creado con su ID generado).*

#### 4. Eliminar / Desactivar Producto
* **Endpoint**: `DELETE /api/v1/productos/{id}`
* **Token requerido**: Sí (`ADMIN`)
* **Respuesta Esperada (204 No Content)**: Sin body.

---

### Módulo: Catálogo - Categorías (`/api/v1/categorias`)

#### 1. Listar Categorías Activas
* **Endpoint**: `GET /api/v1/categorias`
* **Token requerido**: No (Público)
* **Respuesta Esperada (200 OK)**:
  ```json
  [
    {
      "idCategoria": 1,
      "nombreCategoria": "Fantasía",
      "activa": true
    }
  ]
  ```

#### 2. Crear Categoría
* **Endpoint**: `POST /api/v1/categorias`
* **Token requerido**: Sí (`ADMIN`)
* **Body (JSON)**:
  * **Obligatorios**: `nombreCategoria` (String, no vacío)
  * **Opcionales**: `activa` (Boolean, por defecto `true`)
* **Ejemplo Request Body**:
  ```json
  {
    "nombreCategoria": "Fantasía Científica",
    "activa": true
  }
  ```
* **Respuesta Esperada (201 Created)**:
  *(Devuelve el objeto Categoria creado, incluyendo su `idCategoria`).*

#### 3. Eliminar / Desactivar Categoría
* **Endpoint**: `DELETE /api/v1/categorias/{id}`
* **Token requerido**: Sí (`ADMIN`)
* **Respuesta Esperada (204 No Content)**: Sin body.

---

### Módulo: Catálogo - Autores (`/api/v1/autores`)

#### 1. Listar Autores Activos
* **Endpoint**: `GET /api/v1/autores`
* **Token requerido**: No (Público)
* **Respuesta Esperada (200 OK)**:
  ```json
  [
    {
      "idAutorArtista": 1,
      "nombre": "J.R.R. Tolkien",
      "biografia": "Escritor y filólogo británico...",
      "activa": true
    }
  ]
  ```

#### 2. Crear Autor / Artista
* **Endpoint**: `POST /api/v1/autores`
* **Token requerido**: Sí (`ADMIN`)
* **Body (JSON)**:
  * **Obligatorios**: `nombre` (String)
  * **Opcionales**: `biografia` (String), `activa` (Boolean)
* **Ejemplo Request Body**:
  ```json
  {
    "nombre": "George R.R. Martin",
    "biografia": "Novelista y guionista estadounidense...",
    "activa": true
  }
  ```
* **Respuesta Esperada (201 Created)**:
  *(Devuelve el objeto creado con su `idAutorArtista`).*

#### 3. Eliminar / Desactivar Autor
* **Endpoint**: `DELETE /api/v1/autores/{id}`
* **Token requerido**: Sí (`ADMIN`)
* **Respuesta (204 No Content)**: Sin body.

---

### Módulo: Catálogo - Editoriales (`/api/v1/editoriales`)

#### 1. Listar Editoriales/Sellos Activos
* **Endpoint**: `GET /api/v1/editoriales`
* **Token requerido**: No (Público)
* **Respuesta Esperada (200 OK)**:
  ```json
  [
    {
      "idEditorialSello": 1,
      "nombreEditorial": "Minotauro",
      "activa": true
    }
  ]
  ```

#### 2. Crear Editorial / Sello
* **Endpoint**: `POST /api/v1/editoriales`
* **Token requerido**: Sí (`ADMIN`)
* **Body (JSON)**:
  * **Obligatorios**: `nombreEditorial` (String)
  * **Opcionales**: `activa` (Boolean)
* **Ejemplo Request Body**:
  ```json
  {
    "nombreEditorial": "Ediciones Bookly",
    "activa": true
  }
  ```
* **Respuesta Esperada (201 Created)**:
  *(Devuelve el objeto creado con su `idEditorialSello`).*

#### 3. Eliminar / Desactivar Editorial
* **Endpoint**: `DELETE /api/v1/editoriales/{id}`
* **Token requerido**: Sí (`ADMIN`)
* **Respuesta (204 No Content)**: Sin body.

---

### Módulo: Catálogo - Tipos de Producto (`/api/v1/tipos-producto`)

#### 1. Listar Tipos de Producto Activos
* **Endpoint**: `GET /api/v1/tipos-producto`
* **Token requerido**: No (Público)
* **Respuesta Esperada (200 OK)**:
  ```json
  [
    {
      "idTipoProducto": 1,
      "nombreTipoProducto": "Libro",
      "activa": true
    }
  ]
  ```

#### 2. Crear Tipo de Producto
* **Endpoint**: `POST /api/v1/tipos-producto`
* **Token requerido**: Sí (`ADMIN`)
* **Body (JSON)**:
  * **Obligatorios**: `nombreTipoProducto` (String)
  * **Opcionales**: `activa` (Boolean)
* **Ejemplo Request Body**:
  ```json
  {
    "nombreTipoProducto": "Merchandising",
    "activa": true
  }
  ```
* **Respuesta Esperada (201 Created)**:
  *(Devuelve el objeto creado).*

#### 3. Eliminar / Desactivar Tipo de Producto
* **Endpoint**: `DELETE /api/v1/tipos-producto/{id}`
* **Token requerido**: Sí (`ADMIN`)
* **Respuesta (204 No Content)**: Sin body.

---

### Módulo: Catálogo - Rangos Etarios (`/api/v1/rangos-etarios`)

#### 1. Listar Todos los Rangos Etarios
* **Endpoint**: `GET /api/v1/rangos-etarios`
* **Token requerido**: No (Público)
* **Respuesta Esperada (200 OK)**:
  ```json
  [
    {
      "idRangoEtario": 1,
      "descripcion": "Infantil (0-6 años)"
    }
  ]
  ```

#### 2. Crear Rango Etario
* **Endpoint**: `POST /api/v1/rangos-etarios`
* **Token requerido**: Sí (`ADMIN`)
* **Body (JSON)**:
  * **Obligatorios**: `descripcion` (String)
* **Ejemplo Request Body**:
  ```json
  {
    "descripcion": "Juvenil (12-18 años)"
  }
  ```
* **Respuesta Esperada (201 Created)**:
  *(Devuelve el objeto creado).*

#### 3. Eliminar Rango Etario
* **Endpoint**: `DELETE /api/v1/rangos-etarios/{id}`
* **Token requerido**: Sí (`ADMIN`)
* **Respuesta (204 No Content)**: Sin body.

---

### Módulo: Carrito de Compras (`/api/v1/carrito`)
*Los carritos se asocian de forma transparente al usuario autenticado mediante su Token JWT. No hace falta administrar un `idCarrito` en el Frontend.*

#### 1. Obtener Carrito del Usuario Actual
* **Endpoint**: `GET /api/v1/carrito/mio`
* **Token requerido**: Sí (`ADMIN` o `CLIENTE`)
* **Respuesta Esperada (200 OK)**:
  ```json
  {
    "idCarrito": "8f88c81d-e0cb-42a1-bd8c-529e31d904ab",
    "idCliente": "0a5d4d50-c2e7-4c07-a35a-8f767fcb38ee",
    "items": [
      {
        "idItem": "f8a0021c-43db-4e1b-9442-83b4009772ee",
        "idProducto": "9a5e1810-e79e-4a4b-91cc-cf4e82414777",
        "nombreProducto": "El Hobbit",
        "cantidad": 2,
        "precioUnitario": 9500.00,
        "subtotal": 19000.00
      }
    ],
    "total": 19000.00,
    "fechaCreacion": "2026-06-23T15:30:00"
  }
  ```

#### 2. Agregar Item al Carrito
* **Endpoint**: `POST /api/v1/carrito/items`
* **Token requerido**: Sí (`ADMIN` o `CLIENTE`)
* **Body (JSON)**:
  * **Obligatorios**:
    * `idProducto` (UUID)
    * `cantidad` (Integer, mínimo 1)
* **Ejemplo Request Body**:
  ```json
  {
    "idProducto": "9a5e1810-e79e-4a4b-91cc-cf4e82414777",
    "cantidad": 1
  }
  ```
* **Respuesta Esperada (201 Created)**:
  *(Devuelve el `CarritoResponseDTO` completo actualizado).*

#### 3. Actualizar Cantidad de un Item del Carrito
* **Endpoint**: `PATCH /api/v1/carrito/items/{idItem}`
* **Token requerido**: Sí (`ADMIN` o `CLIENTE`)
* **Body (JSON)**:
  * **Obligatorios**:
    * `cantidad` (Integer, mínimo 1, representa la nueva cantidad total del producto)
* **Ejemplo Request Body**:
  ```json
  {
    "cantidad": 3
  }
  ```
* **Respuesta Esperada (200 OK)**:
  *(Devuelve el `CarritoResponseDTO` completo actualizado).*

#### 4. Quitar un Item del Carrito
* **Endpoint**: `DELETE /api/v1/carrito/items/{idItem}`
* **Token requerido**: Sí (`ADMIN` o `CLIENTE`)
* **Respuesta Esperada (204 No Content)**: Sin body en la respuesta.

---

### Módulo: Ventas y Órdenes (`/api/v1/ventas`)

#### 1. Iniciar un Checkout / Registrar una Venta
* **Endpoint**: `POST /api/v1/ventas/checkout`
* **Token requerido**: Sí (`ADMIN`, `CLIENTE` o `VENDEDOR`)
* **Body (JSON)**:
  * **Obligatorios**:
    * `origenVenta` (String, ej: `"WEB"` o `"FISICO"`)
    * `items` (List, no vacío)
      * Cada item requiere: `idProducto` (UUID) y `cantidad` (Integer >= 1)
    * `pagos` (List, no vacío)
      * Cada pago requiere: `idFormaPago` (Integer) y `montoAbonado` (Decimal)
  * **Opcionales**:
    * `idEmpleado` (UUID, opcional para ventas en caja física)
    * `idCliente` (UUID, si un Administrador o Vendedor realiza la compra por cuenta de un Cliente)
    * `idEmpleado` (UUID, para registrar quién operó la venta en caja física)
    * `generarEnvio` (Boolean, si es `true` requiere definir tipo de envío)
    * `tipoEnvio` (String/Enum: `DOMICILIO`, `RETIRO_LOCAL`, `DIGITAL`)
    * `observacionesEnvio` (String)
  * *Nota interna de items/pagos*: Cada item puede opcionalmente recibir `idPromocion`.
* **Ejemplo Request Body**:
  ```json
  {
    "origenVenta": "WEB",
    "idCliente": "0a5d4d50-c2e7-4c07-a35a-8f767fcb38ee",
    "generarEnvio": true,
    "tipoEnvio": "DOMICILIO",
    "observacionesEnvio": "Entregar después de las 18 hs",
    "items": [
      {
        "idProducto": "9a5e1810-e79e-4a4b-91cc-cf4e82414777",
        "cantidad": 2
      }
    ],
    "pagos": [
      {
        "idFormaPago": 1,
        "montoAbonado": 19000.00
      }
    ]
  }
  ```
* **Respuesta Esperada (201 Created)**:
  ```json
  {
    "idVenta": "11a62d4c-e8cf-4328-9860-2ffcaee31b90",
    "fecha": "2026-06-23T15:45:00",
    "estadoVenta": "ARMANDO_PEDIDO",
    "origenVenta": "WEB",
    "idEmpleado": null,
    "idCliente": "0a5d4d50-c2e7-4c07-a35a-8f767fcb38ee",
    "idEmpleado": null,
    "subtotalSinDescuentos": 19000.00,
    "totalFinal": 19000.00,
    "totalPagado": 19000.00,
    "idEnvio": "5c9b2e1b-b4a1-4329-a1b6-ea57de214ff9",
    "tipoEnvio": "DOMICILIO",
    "detalles": [
      {
        "idProducto": "9a5e1810-e79e-4a4b-91cc-cf4e82414777",
        "nombreProducto": "El Hobbit",
        "cantidad": 2,
        "precioUnitario": 9500.00,
        "subtotalRenglon": 19000.00
      }
    ]
  }
  ```

#### 2. Listar Catálogo de Tipos de Venta
* **Endpoint**: `GET /api/v1/ventas/tipos-venta`
* **Token requerido**: Sí (`ADMIN`, `CLIENTE` o `VENDEDOR`)
* **Respuesta Esperada (200 OK)**:
  ```json
  [
    {
      "codigo": "WEB",
      "nombre": "Venta Online",
      "descripcion": "Orden realizada a través del sitio web",
      "requiereEmpleado": false,
      "generaEnvioAutomatico": true
    }
  ]
  ```

#### 3. Listar Catálogo de Formas de Pago
* **Endpoint**: `GET /api/v1/ventas/formas-pago`
* **Token requerido**: Sí (`ADMIN`, `CLIENTE` o `VENDEDOR`)
* **Respuesta Esperada (200 OK)**:
  ```json
  [
    {
      "idFormaPago": 1,
      "nombrePago": "Tarjeta de Crédito"
    },
    {
      "idFormaPago": 2,
      "nombrePago": "MercadoPago"
    }
  ]
  ```

#### 4. Listar Catálogo de Empleados (Vendedores/Cajeros)
* **Endpoint**: `GET /api/v1/ventas/empleados`
* **Token requerido**: Sí (`ADMIN` o `VENDEDOR`)
* **Respuesta Esperada (200 OK)**:
  ```json
  [
    {
      "idEmpleado": "1e2f8d8b-4b2a-4310-a9cb-b2098bfe15ee",
      "idPersona": "6f2e96bc-cbdf-4a69-9237-775678af43aa",
      "idSucursal": 1,
      "nombreCompleto": "Juan Perez",
      "nombre": "Juan",
      "apellido": "Perez",
      "dni": "12345678A",
      "telefono": "+54911223344"
    }
  ]
  ```

#### 5. Listar Todas las Ventas Globales
* **Endpoint**: `GET /api/v1/ventas`
* **Token requerido**: Sí (`ADMIN`)
* **Respuesta Esperada (200 OK)**:
  *(Devuelve un arreglo de objetos tipo `VentaResponseDTO`).*

#### 6. Listar Órdenes Propias (Mis Órdenes)
* **Endpoint**: `GET /api/v1/ventas/mis-ordenes`
* **Token requerido**: Sí (`CLIENTE`)
* **Respuesta Esperada (200 OK)**:
  *(Devuelve la lista filtrada únicamente con las compras del cliente autenticado).*

#### 7. Obtener una Venta / Órden por ID
* **Endpoint**: `GET /api/v1/ventas/{id}`
* **Token requerido**: Sí (`ADMIN` o `CLIENTE`)
* **Validación de Ownership**: Si es `CLIENTE`, el sistema verifica que la venta le pertenezca. De lo contrario, retorna un error `403 Forbidden` (`RESOURCE_NOT_OWNED`).
* **Respuesta Esperada (200 OK)**:
  *(Devuelve el objeto `VentaResponseDTO` correspondiente).*

---

### Módulo: Logística y Envíos (`/api/v1/envios`)

#### 1. Listar Envíos
* **Endpoint**: `GET /api/v1/envios`
* **Token requerido**: Sí (`ADMIN` o `CLIENTE`)
* **Validación de Ownership**: Si es `CLIENTE`, el listado se filtra automáticamente y sólo retorna los envíos asociados a sus compras. Si es `ADMIN`, lista todos de forma global.
* **Respuesta Esperada (200 OK)**:
  ```json
  [
    {
      "idEnvio": "5c9b2e1b-b4a1-4329-a1b6-ea57de214ff9",
      "idVenta": "11a62d4c-e8cf-4328-9860-2ffcaee31b90",
      "tipoEnvio": "DOMICILIO",
      "estadoLogistica": "PENDIENTE",
      "codigoRetiro": "RET-4819",
      "empresaCorreo": null,
      "numeroTracking": null,
      "fechaActualizacion": "2026-06-23T15:45:00"
    }
  ]
  ```

#### 2. Obtener Envío por ID de Venta
* **Endpoint**: `GET /api/v1/envios/venta/{idVenta}`
* **Token requerido**: Sí (`ADMIN` o `CLIENTE`)
* **Validación de Ownership**: Si es `CLIENTE`, debe ser dueño de la compra `idVenta` para acceder a sus datos.
* **Respuesta Esperada (200 OK)**:
  *(Retorna un objeto `EnvioResponseDTO`).*

#### 3. Inicializar un Envío (Manual)
* **Endpoint**: `POST /api/v1/envios`
* **Token requerido**: Sí (`ADMIN`)
* **Body (JSON)**:
  * **Obligatorios**:
    * `idVenta` (UUID)
    * `tipoEnvio` (String/Enum: `DOMICILIO` o `RETIRO_LOCAL`)
  * **Opcionales**:
    * `observaciones` (String)
* **Ejemplo Request Body**:
  ```json
  {
    "idVenta": "11a62d4c-e8cf-4328-9860-2ffcaee31b90",
    "tipoEnvio": "RETIRO_LOCAL",
    "observaciones": "Retirar por sucursal centro"
  }
  ```
* **Respuesta Esperada (201 Created)**:
  *(Devuelve el `EnvioResponseDTO` inicializado).*

#### 4. Actualizar Estado de un Envío
* **Endpoint**: `PATCH /api/v1/envios/{idEnvio}/estado`
* **Token requerido**: Sí (`ADMIN`)
* **Body (JSON)**:
  * **Obligatorios**:
    * `nuevoEstado` (String/Enum: `PENDIENTE`, `EN_PREPARACION`, `LISTO_PARA_RETIRO`, `DESPACHADO`, `ENTREGADO`, `CANCELADO`, `DEVUELTO`)
  * **Opcionales**:
    * `numeroTracking` (String)
    * `empresaCorreo` (String)
    * `idEmpleado` (UUID)
* **Ejemplo Request Body**:
  ```json
  {
    "nuevoEstado": "DESPACHADO",
    "numeroTracking": "TRK948104812",
    "empresaCorreo": "Correo Argentino"
  }
  ```
* **Respuesta Esperada (200 OK)**:
  *(Devuelve el `EnvioResponseDTO` actualizado).*

---

### Módulo: Inventario y Stock (`/api/v1/inventario`)
*Todos los endpoints de este módulo requieren un token con rol **`ROLE_ADMIN`** o **`ROLE_VENDEDOR`**.*

#### 1. Listar Estado de Stock Global
* **Endpoint**: `GET /api/v1/inventario`
* **Respuesta Esperada (200 OK)**:
  ```json
  [
    {
      "idSucursal": 1,
      "idProducto": "9a5e1810-e79e-4a4b-91cc-cf4e82414777",
      "stock": 45,
      "sucursal": {
        "idSucursal": 1,
        "nombre": "Sucursal Centro",
        "direccion": "Av. Corrientes 1234",
        "activa": true
      },
      "producto": {
        "idProducto": "9a5e1810-e79e-4a4b-91cc-cf4e82414777",
        "codigoBarras": "9789504979128",
        "nombreProducto": "El Hobbit",
        "precioActual": 9500.00,
        "activo": true
      }
    }
  ]
  ```

#### 2. Obtener Stock de un Producto
* **Endpoint**: `GET /api/v1/inventario/{idProducto}`
* **Respuesta Esperada (200 OK)**:
  *(Retorna un único objeto `InventarioResponseDTO` con los datos de stock).*

#### 3. Listar Todos los Movimientos de Stock
* **Endpoint**: `GET /api/v1/inventario/movimientos`
* **Respuesta Esperada (200 OK)**:
  ```json
  [
    {
      "idMovimiento": 12,
      "idSucursal": 1,
      "idProducto": "9a5e1810-e79e-4a4b-91cc-cf4e82414777",
      "cantidad": 50,
      "tipoMovimiento": "ENTRADA",
      "fecha": "2026-06-23T11:00:00",
      "idEmpleado": "1e2f8d8b-4b2a-4310-a9cb-b2098bfe15ee",
      "stockResultante": 45
    }
  ]
  ```

#### 4. Obtener un Movimiento de Stock por ID
* **Endpoint**: `GET /api/v1/inventario/movimientos/{idMovimiento}`
* **Respuesta Esperada (200 OK)**:
  *(Retorna un objeto `MovimientoStockResponseDTO`).*

#### 5. Registrar Movimiento Genérico de Stock
* **Endpoint**: `POST /api/v1/inventario/movimientos`
* **Body (JSON)**:
  * **Obligatorios**:
    * `idProducto` (UUID)
    * `cantidad` (Integer >= 1)
    * `tipoMovimiento` (String, ej: `"ENTRADA"`, `"SALIDA"`, `"AJUSTE"`)
    * `idEmpleado` (UUID)
  * **Opcionales**:
    * No requiere sucursal
* **Ejemplo Request Body**:
  ```json
  {
    "idSucursal": 1,
    "idProducto": "9a5e1810-e79e-4a4b-91cc-cf4e82414777",
    "cantidad": 10,
    "tipoMovimiento": "AJUSTE",
    "idEmpleado": "1e2f8d8b-4b2a-4310-a9cb-b2098bfe15ee"
  }
  ```
* **Respuesta Esperada (201 Created)**:
  *(Devuelve el `MovimientoStockResponseDTO` registrado).*

#### 6. Registrar Entrada de Stock (Suma)
* **Endpoint**: `POST /api/v1/inventario/movimientos/entrada`
* **Body (JSON)**:
  * Mismo formato que el movimiento genérico (se fuerza internamente que la cantidad sume al stock).
* **Respuesta Esperada (201 Created)**:
  *(Devuelve `MovimientoStockResponseDTO`).*

#### 7. Registrar Salida de Stock (Resta)
* **Endpoint**: `POST /api/v1/inventario/movimientos/salida`
* **Body (JSON)**:
  * Mismo formato que el movimiento genérico (se fuerza internamente que la cantidad reste al stock).
* **Respuesta Esperada (201 Created)**:
  *(Devuelve `MovimientoStockResponseDTO`).*

#### 8. Modificar un Registro de Movimiento de Stock
* **Endpoint**: `PUT /api/v1/inventario/movimientos/{idMovimiento}`
* **Body (JSON)**:
  * Mismo formato que el movimiento genérico.
* **Respuesta Esperada (200 OK)**:
  *(Devuelve el `MovimientoStockResponseDTO` actualizado).*

#### 9. Eliminar un Movimiento de Stock
* **Endpoint**: `DELETE /api/v1/inventario/movimientos/{idMovimiento}`
* **Respuesta Esperada (204 No Content)**: Sin body.
