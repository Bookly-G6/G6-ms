# Endpoints Bookly para Postman

Base URL por defecto:

```http
http://localhost:8080
```

Headers comunes para endpoints con body:

```http
Content-Type: application/json
```

Headers para endpoints protegidos:

```http
Authorization: Bearer <JWT>
```

Los endpoints publicos no requieren token. Los protegidos requieren el rol indicado. Los UUID, IDs y valores entre `<...>` son placeholders: reemplazalos por datos reales de tu base.

## Formato de errores

Validaciones `@Valid` responden `400` con un mapa de campos:

```json
{
  "campo": "mensaje de validacion"
}
```

Errores de negocio, permisos, JSON invalido o no encontrado responden con estructura `ApiError`:

```json
{
  "timestamp": "<fecha_hora>",
  "status": 400,
  "error": "Bad Request",
  "code": "BUSINESS_RULE_ERROR",
  "message": "<mensaje>",
  "path": "<ruta>",
  "details": null
}
```

## Auth

### Registrar cliente

```http
POST /api/v1/auth/register
```

Publico. Crea usuario con rol `CLIENTE` y perfil de cliente.

Body:

```json
{
  "nombre": "<nombre>",
  "apellido": "<apellido>",
  "email": "<email>",
  "password": "<password>",
  "dni": "<dni>",
  "telefono": "<telefono>"
}
```

Obligatorios: `nombre`, `apellido`, `email`, `password`.

Opcionales: `dni`, `telefono`.

Validaciones: `email` debe ser valido; `password` minimo 6 caracteres; email no debe existir.

Responde `201`:

```json
{
  "token": "<jwt>",
  "idUsuario": "<uuid>",
  "email": "<email>",
  "nombre": "<nombre>",
  "apellido": "<apellido>",
  "rol": "CLIENTE"
}
```

### Login

```http
POST /api/v1/auth/login
```

Publico.

Body:

```json
{
  "email": "<email>",
  "password": "<password>"
}
```

Obligatorios: `email`, `password`.

Validaciones: `email` debe ser valido; usuario debe estar activo.

Responde `200`:

```json
{
  "token": "<jwt>",
  "idUsuario": "<uuid>",
  "email": "<email>",
  "nombre": "<nombre>",
  "apellido": "<apellido>",
  "rol": "<rol>"
}
```

### Usuario actual

```http
GET /api/v1/auth/me
```

Requiere usuario autenticado.

Body: no lleva.

Responde `200`:

```json
{
  "idUsuario": "<uuid>",
  "email": "<email>",
  "nombre": "<nombre>",
  "apellido": "<apellido>",
  "rol": "<rol>"
}
```

## Usuarios

Todos los endpoints requieren `ADMIN`.

### Listar usuarios

```http
GET /api/v1/usuarios
```

Body: no lleva.

Responde `200`: array de:

```json
{
  "idUsuario": "<uuid>",
  "idPersona": "<uuid>",
  "nombre": "<nombre>",
  "apellido": "<apellido>",
  "dni": "<dni>",
  "telefono": "<telefono>",
  "email": "<email>",
  "rol": "<rol>",
  "activo": true
}
```

### Obtener usuario

```http
GET /api/v1/usuarios/{id}
```

Body: no lleva.

Responde `200`: mismo objeto de usuario.

### Crear usuario

```http
POST /api/v1/usuarios
```

Crea usuario con rol `CLIENTE` por defecto. Si el rol queda `CLIENTE`, asegura perfil de cliente.

Body:

```json
{
  "nombre": "<nombre>",
  "apellido": "<apellido>",
  "email": "<email>",
  "password": "<password>",
  "dni": "<dni>",
  "telefono": "<telefono>",
  "activo": true
}
```

Obligatorios: `nombre`, `apellido`, `email`, `password`.

Opcionales: `dni`, `telefono`, `rol`, `activo`.

Validaciones: `email` valido y unico; `password` minimo 6 caracteres. Si `activo` no se envia, queda `true`.

Responde `201`: objeto de usuario.

### Actualizar usuario

```http
PUT /api/v1/usuarios/{id}
```

Body:

```json
{
  "nombre": "<nombre>",
  "apellido": "<apellido>",
  "email": "<email>",
  "password": "<password opcional>",
  "dni": "<dni>",
  "telefono": "<telefono>",
  "activo": true
}
```

Obligatorios: `nombre`, `apellido`, `email`.

Opcionales: `password`, `dni`, `telefono`, `activo`.

Validaciones: `email` valido y unico respecto de otros usuarios; si se envia `password`, minimo 6 caracteres. Si `password` es `null` o blanco, no cambia.

Responde `200`: objeto de usuario.

### Cambiar rol de usuario

```http
PUT /api/v1/usuarios/{id}/rol
```

Body:

```json
{
  "nombreRol": "<rol>"
}
```

Obligatorio: `nombreRol`.

Validaciones: el rol debe existir; se normaliza con `trim().toUpperCase()`. Si queda `CLIENTE`, crea perfil de cliente si falta. Si queda `ADMIN` o `VENDEDOR`, crea perfil de empleado si falta.

Responde `200`: objeto de usuario.

### Eliminar usuario

```http
DELETE /api/v1/usuarios/{id}
```

Body: no lleva.

Elimina el usuario y su persona asociada.

Responde `204` sin body.

## Roles

Todos los endpoints requieren `ADMIN`.

### Listar roles

```http
GET /api/v1/roles
```

Responde `200`: array de:

```json
{
  "idRol": 0,
  "nombreRol": "<rol>"
}
```

### Obtener rol

```http
GET /api/v1/roles/{id}
```

Responde `200`: objeto de rol.

### Crear rol

```http
POST /api/v1/roles
```

Body:

```json
{
  "nombreRol": "<rol>"
}
```

Obligatorio: `nombreRol`.

Validaciones: se normaliza a mayusculas; no debe existir otro rol con el mismo nombre.

Responde `201`: objeto de rol.

### Actualizar rol

```http
PUT /api/v1/roles/{id}
```

Body:

```json
{
  "nombreRol": "<rol>"
}
```

Obligatorio: `nombreRol`.

Validaciones: se normaliza a mayusculas; no debe existir otro rol con el mismo nombre.

Responde `200`: objeto de rol.

### Eliminar rol

```http
DELETE /api/v1/roles/{id}
```

Responde `204` sin body.

## Productos

`GET` es publico. `POST` y `DELETE` requieren `ADMIN`.

No hay endpoint `PUT` ni `PATCH` de productos implementado en el controller actual.

### Listar productos

```http
GET /api/v1/productos
```

Body: no lleva.

Si el token es de `ADMIN`, devuelve todos. Si no hay token o no es admin, devuelve solo activos.

Responde `200`: array de:

```json
{
  "idProducto": "<uuid>",
  "codigoBarras": "<codigo>",
  "nombreProducto": "<nombre>",
  "descripcion": "<descripcion>",
  "precioCosto": 0,
  "precioActual": 0,
  "stock": 0,
  "activo": true,
  "tipoProducto": "<nombre_tipo>",
  "editorialSello": "<nombre_editorial>",
  "rangoEtario": "<descripcion>",
  "categorias": ["<categoria>"],
  "atributosEspecificos": {
    "clave": "<valor>"
  },
  "autores": ["<autor>"]
}
```

### Obtener producto

```http
GET /api/v1/productos/{id}
```

Body: no lleva.

Si no es `ADMIN`, los productos inactivos responden como no encontrados.

Responde `200`: objeto de producto.

### Crear producto

```http
POST /api/v1/productos
```

Body:

```json
{
  "codigoBarras": "<codigo_8_a_50_caracteres>",
  "nombreProducto": "<nombre>",
  "descripcion": "<descripcion>",
  "precioCosto": 0.01,
  "precioActual": 0.01,
  "idTipoProducto": 0,
  "idEditorialSello": 0,
  "idRangoEtario": 0,
  "stock": 0,
  "idsCategorias": [0],
  "idsAutores": [0],
  "atributosEspecificos": {
    "clave": "valor",
    "numero": 1,
    "booleano": true
  }
}
```

Obligatorios: `codigoBarras`, `nombreProducto`, `precioCosto`, `precioActual`, `idTipoProducto`, `idEditorialSello`, `idRangoEtario`, `stock`, `idsCategorias`, `idsAutores`.

Opcionales: `descripcion`, `atributosEspecificos`.

Validaciones: `codigoBarras` entre 8 y 50 caracteres; precios mayores a 0; `stock` mayor o igual a 0; `idsCategorias` y `idsAutores` no vacios; referencias existentes. `atributosEspecificos` puede omitirse o estar vacio; maximo 15 claves; claves solo `a-z`, numeros y `_`; valores solo texto, numero o booleano, sin objetos anidados.

Responde `201`: objeto de producto.

### Eliminar producto

```http
DELETE /api/v1/productos/{id}
```

Marca `activo = false`.

Responde `204` sin body.

## Catalogo auxiliar

Los `GET` son publicos. Escritura y borrado requieren `ADMIN` por configuracion de seguridad.

En estos controllers auxiliares no hay endpoints `PUT` ni `PATCH` implementados.

### Categorias

```http
GET /api/v1/categorias
```

Lista categorias activas. Responde array de:

```json
{
  "idCategoria": 0,
  "nombreCategoria": "<nombre>",
  "activa": true
}
```

```http
POST /api/v1/categorias
```

Body:

```json
{
  "nombreCategoria": "<nombre>",
  "activa": true
}
```

Validaciones DTO: no hay `@Valid` ni restricciones declaradas en controller. En entidad, `nombreCategoria` es columna no nula.

Responde `201`: categoria creada.

```http
DELETE /api/v1/categorias/{id}
```

Borrado fisico por `deleteById`.

Responde `204`.

### Autores / artistas

```http
GET /api/v1/autores
```

Lista autores activos. Responde array de:

```json
{
  "idAutorArtista": 0,
  "nombre": "<nombre>",
  "biografia": "<biografia>",
  "activa": true
}
```

```http
POST /api/v1/autores
```

Body:

```json
{
  "nombre": "<nombre>",
  "biografia": "<biografia>",
  "activa": true
}
```

Validaciones DTO: no hay `@Valid` ni restricciones declaradas en controller. En entidad, `nombre` es columna no nula.

Responde `201`: autor creado.

```http
DELETE /api/v1/autores/{id}
```

Marca `activa = false`.

Responde `204`.

### Editoriales

```http
GET /api/v1/editoriales
```

Lista editoriales activas. Responde array de:

```json
{
  "idEditorialSello": 0,
  "nombreEditorial": "<nombre>",
  "activa": true
}
```

```http
POST /api/v1/editoriales
```

Body:

```json
{
  "nombreEditorial": "<nombre>",
  "activa": true
}
```

Validaciones DTO: no hay `@Valid` ni restricciones declaradas en controller. En entidad, `nombreEditorial` es columna no nula.

Responde `201`: editorial creada.

```http
DELETE /api/v1/editoriales/{id}
```

Borrado fisico por `deleteById`.

Responde `204`.

### Rangos etarios

```http
GET /api/v1/rangos-etarios
```

Lista todos los rangos. Responde array de:

```json
{
  "idRangoEtario": 0,
  "descripcion": "<descripcion>"
}
```

```http
POST /api/v1/rangos-etarios
```

Body:

```json
{
  "descripcion": "<descripcion>"
}
```

Validaciones DTO: no hay `@Valid` ni restricciones declaradas en controller. En entidad, `descripcion` es columna no nula.

Responde `201`: rango creado.

```http
DELETE /api/v1/rangos-etarios/{id}
```

Borrado fisico. Valida existencia antes de borrar.

Responde `204`.

### Tipos de producto

```http
GET /api/v1/tipos-producto
```

Lista tipos activos. Responde array de:

```json
{
  "idTipoProducto": 0,
  "nombreTipoProducto": "<nombre>",
  "activa": true
}
```

```http
POST /api/v1/tipos-producto
```

Body:

```json
{
  "nombreTipoProducto": "<nombre>",
  "activa": true
}
```

Validaciones DTO: no hay `@Valid` ni restricciones declaradas en controller. En entidad, `nombreTipoProducto` es columna no nula.

Responde `201`: tipo creado.

```http
DELETE /api/v1/tipos-producto/{id}
```

Marca `activa = false`.

Responde `204`.

## Clientes

### Listar clientes

```http
GET /api/v1/clientes
```

Roles: `ADMIN`, `VENDEDOR`.

Responde `200`: array de:

```json
{
  "idCliente": "<uuid>",
  "idPersona": "<uuid>",
  "nombre": "<nombre>",
  "apellido": "<apellido>",
  "dni": "<dni>",
  "telefono": "<telefono>",
  "puntosFidelidad": 0
}
```

### Obtener cliente

```http
GET /api/v1/clientes/{id}
```

Roles: `ADMIN`, `VENDEDOR`, `CLIENTE`. Si es `CLIENTE`, solo puede ver su propio perfil.

Responde `200`: objeto de cliente.

### Crear cliente

```http
POST /api/v1/clientes
```

Roles: `ADMIN`, `VENDEDOR`.

Body:

```json
{
  "nombre": "<nombre>",
  "apellido": "<apellido>",
  "dni": "<dni>",
  "telefono": "<telefono>",
  "puntosFidelidad": 0
}
```

Obligatorios: `nombre`, `apellido`.

Opcionales: `dni`, `telefono`, `puntosFidelidad`. Si `puntosFidelidad` no se envia, queda `0`.

Responde `201`: objeto de cliente.

### Actualizar cliente

```http
PUT /api/v1/clientes/{id}
```

Roles: `ADMIN`, `VENDEDOR`, `CLIENTE`. Si es `CLIENTE`, solo puede modificar su propio perfil.

Body:

```json
{
  "nombre": "<nombre>",
  "apellido": "<apellido>",
  "dni": "<dni>",
  "telefono": "<telefono>",
  "puntosFidelidad": 0
}
```

Obligatorios: `nombre`, `apellido`.

Opcionales: `dni`, `telefono`, `puntosFidelidad`. Si `puntosFidelidad` no se envia, conserva el valor anterior.

Responde `200`: objeto de cliente.

### Eliminar cliente

```http
DELETE /api/v1/clientes/{id}
```

Roles: `ADMIN`.

Elimina cliente y persona asociada.

Responde `204`.

## Empleados

Todos los endpoints requieren `ADMIN`.

### Listar empleados

```http
GET /api/v1/empleados
```

Responde `200`: array de:

```json
{
  "idEmpleado": "<uuid>",
  "idPersona": "<uuid>",
  "legajo": "<legajo>",
  "cargo": "<cargo>"
}
```

### Obtener empleado

```http
GET /api/v1/empleados/{id}
```

Responde `200`: objeto de empleado.

### Crear empleado

```http
POST /api/v1/empleados
```

Body:

```json
{
  "idPersona": "<uuid_persona>",
  "legajo": "<legajo>",
  "cargo": "<cargo>"
}
```

Obligatorios: `idPersona`, `legajo`, `cargo`.

Validaciones: `idPersona` debe existir; `legajo` no debe existir.

Responde `201`: objeto de empleado.

### Actualizar empleado

```http
PUT /api/v1/empleados/{id}
```

Body:

```json
{
  "idPersona": "<uuid_persona>",
  "legajo": "<legajo>",
  "cargo": "<cargo>"
}
```

Obligatorios: `idPersona`, `legajo`, `cargo`.

Validaciones: `idPersona` debe existir.

Responde `200`: objeto de empleado.

### Eliminar empleado

```http
DELETE /api/v1/empleados/{id}
```

Responde `204`.

## Carrito

Todos los endpoints requieren `ADMIN` o `CLIENTE`. El usuario autenticado debe tener perfil de cliente.

### Obtener mi carrito

```http
GET /api/v1/carrito/mio
```

Body: no lleva.

Crea un carrito activo si el cliente no tiene uno.

Responde `200`:

```json
{
  "idCarrito": "<uuid>",
  "idCliente": "<uuid>",
  "items": [
    {
      "idItem": "<uuid>",
      "idProducto": "<uuid>",
      "nombreProducto": "<nombre>",
      "cantidad": 1,
      "precioUnitario": 0,
      "subtotal": 0
    }
  ],
  "total": 0,
  "fechaCreacion": "<fecha_hora>"
}
```

### Agregar item

```http
POST /api/v1/carrito/items
```

Body:

```json
{
  "idProducto": "<uuid_producto>",
  "cantidad": 1
}
```

Obligatorios: `idProducto`, `cantidad`.

Validaciones: `cantidad` minimo 1; producto debe existir y estar activo. Si ya existe el producto en el carrito, suma la cantidad.

Responde `201`: carrito completo.

### Actualizar item

```http
PATCH /api/v1/carrito/items/{idItem}
```

Body:

```json
{
  "cantidad": 1
}
```

Obligatorio: `cantidad`.

Validaciones: `cantidad` minimo 1; el item debe pertenecer al carrito del cliente autenticado.

Responde `200`: carrito completo.

### Eliminar item

```http
DELETE /api/v1/carrito/items/{idItem}
```

El item debe pertenecer al carrito del cliente autenticado.

Responde `204`.

## Inventario y movimientos de stock

Todos los endpoints requieren `ADMIN` o `VENDEDOR`.

### Listar inventario

```http
GET /api/v1/inventario
```

Responde `200`: array de:

```json
{
  "idProducto": "<uuid>",
  "stock": 0,
  "producto": {
    "idProducto": "<uuid>",
    "codigoBarras": "<codigo>",
    "nombreProducto": "<nombre>",
    "descripcion": "<descripcion>",
    "precioCosto": 0,
    "precioActual": 0,
    "stock": 0,
    "activo": true,
    "tipoProducto": "<tipo>",
    "editorialSello": "<editorial>",
    "rangoEtario": "<rango>",
    "categorias": ["<categoria>"],
    "atributosEspecificos": {},
    "autores": ["<autor>"]
  }
}
```

### Obtener inventario por producto

```http
GET /api/v1/inventario/{idProducto}
```

Responde `200`: objeto de inventario.

### Listar movimientos

```http
GET /api/v1/inventario/movimientos
```

Responde `200`: array de:

```json
{
  "idMovimiento": 0,
  "idProducto": "<uuid>",
  "cantidad": 1,
  "tipoMovimiento": "ENTRADA",
  "fecha": "<fecha_hora>",
  "idEmpleado": "<uuid>",
  "stockResultante": 0
}
```

### Obtener movimiento

```http
GET /api/v1/inventario/movimientos/{idMovimiento}
```

Responde `200`: objeto de movimiento.

### Crear movimiento generico

```http
POST /api/v1/inventario/movimientos
```

Body:

```json
{
  "idProducto": "<uuid_producto>",
  "cantidad": 1,
  "tipoMovimiento": "ENTRADA",
  "idEmpleado": "<uuid_empleado>"
}
```

Obligatorios: `idProducto`, `cantidad`, `tipoMovimiento`, `idEmpleado`.

Validaciones: `cantidad` minimo 1; `tipoMovimiento` debe ser `ENTRADA` o `SALIDA`; producto y empleado deben existir; `SALIDA` no puede dejar stock negativo.

Efecto: `ENTRADA` suma stock; `SALIDA` resta stock.

Responde `201`: objeto de movimiento.

### Crear entrada

```http
POST /api/v1/inventario/movimientos/entrada
```

Body:

```json
{
  "idProducto": "<uuid_producto>",
  "cantidad": 1,
  "tipoMovimiento": "ENTRADA",
  "idEmpleado": "<uuid_empleado>"
}
```

Aunque el DTO exige `tipoMovimiento`, el servicio fuerza el movimiento a `ENTRADA`.

Responde `201`: objeto de movimiento.

### Crear salida

```http
POST /api/v1/inventario/movimientos/salida
```

Body:

```json
{
  "idProducto": "<uuid_producto>",
  "cantidad": 1,
  "tipoMovimiento": "SALIDA",
  "idEmpleado": "<uuid_empleado>"
}
```

Aunque el DTO exige `tipoMovimiento`, el servicio fuerza el movimiento a `SALIDA`.

Validacion extra: no puede dejar stock negativo.

Responde `201`: objeto de movimiento.

### Actualizar movimiento

```http
PUT /api/v1/inventario/movimientos/{idMovimiento}
```

Body:

```json
{
  "idProducto": "<uuid_producto>",
  "cantidad": 1,
  "tipoMovimiento": "SALIDA",
  "idEmpleado": "<uuid_empleado>"
}
```

Obligatorios: `idProducto`, `cantidad`, `tipoMovimiento`, `idEmpleado`.

Efecto: revierte el delta del movimiento anterior y aplica el nuevo. Valida que el stock no quede negativo.

Responde `200`: objeto de movimiento.

### Eliminar movimiento

```http
DELETE /api/v1/inventario/movimientos/{idMovimiento}
```

Efecto: revierte el delta del movimiento y elimina el registro.

Responde `204`.

## Ventas

### Checkout

```http
POST /api/v1/ventas/checkout
```

Roles: `ADMIN`, `CLIENTE`, `VENDEDOR`.

Body:

```json
{
  "origenVenta": "WEB",
  "idCliente": "<uuid_cliente>",
  "idEmpleado": "<uuid_empleado>",
  "items": [
    {
      "idProducto": "<uuid_producto>",
      "cantidad": 1,
      "idPromocion": 0
    }
  ],
  "pagos": [
    {
      "idFormaPago": 0,
      "montoAbonado": 0.01
    }
  ],
  "generarEnvio": true,
  "tipoEnvio": "DOMICILIO",
  "observacionesEnvio": "<observaciones>"
}
```

Obligatorios: `origenVenta`, `items`, `pagos`; dentro de item: `idProducto`, `cantidad`; dentro de pago: `idFormaPago`, `montoAbonado`.

Opcionales segun flujo: `idCliente`, `idEmpleado`, `idPromocion`, `generarEnvio`, `tipoEnvio`, `observacionesEnvio`.

Validaciones: `items` y `pagos` no vacios; `cantidad` minimo 1; `montoAbonado` mayor a 0; producto debe existir, estar activo y tener stock suficiente; forma de pago debe existir; debe existir estado de venta `CONFIRMADA`; el total abonado debe ser mayor o igual al total final.

Reglas por rol/origen: si es `CLIENTE`, ignora `idCliente` y usa el cliente del usuario autenticado. Si es `VENDEDOR`, fuerza `origenVenta = LOCAL`; si no manda cliente, usa o crea Consumidor Final; para venta no `WEB` necesita empleado existente o toma el primer empleado disponible. Si es `WEB` o `generarEnvio = true`, requiere `tipoEnvio`. Si es vendedor, no crea envio.

Valores de `tipoEnvio`: `DOMICILIO`, `RETIRO_LOCAL`, `DIGITAL`.

Efecto: crea venta, detalles, pagos; descuenta stock; registra movimiento `SALIDA` si hay empleado; puede crear envio.

Responde `201`:

```json
{
  "idVenta": "<uuid>",
  "fecha": "<fecha_hora>",
  "estadoVenta": "<estado>",
  "origenVenta": "WEB",
  "idCliente": "<uuid>",
  "idEmpleado": "<uuid>",
  "subtotalSinDescuentos": 0,
  "totalFinal": 0,
  "totalPagado": 0,
  "idEnvio": "<uuid>",
  "tipoEnvio": "<tipo>",
  "detalles": [
    {
      "idProducto": "<uuid>",
      "nombreProducto": "<nombre>",
      "cantidad": 1,
      "precioUnitario": 0,
      "subtotalRenglon": 0
    }
  ]
}
```

### Tipos de venta

```http
GET /api/v1/ventas/tipos-venta
```

Roles: `ADMIN`, `CLIENTE`, `VENDEDOR`.

Responde `200`: array de:

```json
{
  "codigo": "<codigo>",
  "nombre": "<nombre>",
  "descripcion": "<descripcion>",
  "requiereEmpleado": true,
  "generaEnvioAutomatico": true
}
```

El servicio devuelve codigos `WEB` y `LOCAL`.

### Formas de pago

```http
GET /api/v1/ventas/formas-pago
```

Roles: `ADMIN`, `CLIENTE`, `VENDEDOR`.

Responde `200`: array de:

```json
{
  "idFormaPago": 0,
  "nombrePago": "<nombre>"
}
```

### Empleados para venta

```http
GET /api/v1/ventas/empleados
```

Roles: `ADMIN`, `VENDEDOR`.

Responde `200`: array de:

```json
{
  "idEmpleado": "<uuid>",
  "idPersona": "<uuid>",
  "nombreCompleto": "<nombre apellido>",
  "nombre": "<nombre>",
  "apellido": "<apellido>",
  "dni": "<dni>",
  "telefono": "<telefono>"
}
```

### Listar todas las ventas

```http
GET /api/v1/ventas
```

Roles: `ADMIN`.

Responde `200`: array de ventas.

### Mis ordenes

```http
GET /api/v1/ventas/mis-ordenes
```

Roles: `CLIENTE`.

Responde `200`: ventas del cliente autenticado.

### Obtener venta

```http
GET /api/v1/ventas/{id}
```

Roles: `ADMIN`, `CLIENTE`. Si es `CLIENTE`, solo puede ver ventas propias.

Responde `200`: objeto de venta.

## Envios

### Listar envios

```http
GET /api/v1/envios
```

Roles: `ADMIN`, `CLIENTE`. Admin ve envios activos; cliente ve solo los envios de sus ventas.

Responde `200`: array de:

```json
{
  "idEnvio": "<uuid>",
  "idVenta": "<uuid>",
  "tipoEnvio": "<tipo>",
  "estadoLogistica": "<estado>",
  "codigoRetiro": "<codigo>",
  "empresaCorreo": "<empresa>",
  "numeroTracking": "<tracking>",
  "fechaActualizacion": "<fecha_hora>"
}
```

### Obtener envio por venta

```http
GET /api/v1/envios/venta/{idVenta}
```

Roles: `ADMIN`, `CLIENTE`. Si es cliente, la venta debe ser propia.

Responde `200`: objeto de envio.

### Inicializar envio

```http
POST /api/v1/envios
```

Roles: `ADMIN`.

Body:

```json
{
  "idVenta": "<uuid_venta>",
  "tipoEnvio": "RETIRO_LOCAL",
  "observaciones": "<observaciones>"
}
```

Obligatorios: `idVenta`, `tipoEnvio`.

Opcional: `observaciones`.

Validaciones: la venta no debe tener ya un envio activo.

Valores de `tipoEnvio`: `DOMICILIO`, `RETIRO_LOCAL`, `DIGITAL`.

Efecto: crea envio con estado `EN_PREPARACION`; si `tipoEnvio` es `RETIRO_LOCAL`, genera `codigoRetiro`; registra historial.

Responde `201`: objeto de envio.

### Actualizar estado de envio

```http
PATCH /api/v1/envios/{idEnvio}/estado
```

Roles: `ADMIN`.

Body:

```json
{
  "nuevoEstado": "DESPACHADO",
  "numeroTracking": "<tracking>",
  "empresaCorreo": "<empresa>",
  "idEmpleado": "<uuid_empleado>"
}
```

Obligatorio: `nuevoEstado`.

Opcionales: `numeroTracking`, `empresaCorreo`, `idEmpleado`.

Valores de `nuevoEstado`: `PENDIENTE`, `EN_PREPARACION`, `LISTO_PARA_RETIRO`, `DESPACHADO`, `ENTREGADO`, `CANCELADO`, `DEVUELTO`.

Efecto: actualiza estado; si se envian tracking/correo, los guarda; registra historial.

Responde `200`: objeto de envio.
