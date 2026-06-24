# Cambios de Seguridad, Autorizacion y Contrato de Respuestas

## 1) Objetivo del ajuste

Este documento resume y explica los cambios aplicados en backend para alinear la API con frontend.

Punto clave funcional:
- No existe ruta de catalogo general.
- El catalogo se consume desde /api/v1/productos.

Se ajusto:
- Seguridad de rutas (publicas, CLIENTE, ADMIN).
- Contrato de respuestas de autenticacion.
- Contrato de errores HTTP con body consistente.
- Reglas de ownership para recursos de CLIENTE.
- Flujo de carrito (nuevos endpoints y nuevas tablas).

---

## 2) Resumen de archivos principales modificados

Seguridad y JWT:
- src/main/java/com/gotechy/bookly/config/SecurityConfig.java
- src/main/java/com/gotechy/bookly/config/JwtAuthenticationFilter.java
- src/main/java/com/gotechy/bookly/config/JwtService.java

Errores API:
- src/main/java/com/gotechy/bookly/core/exception/ApiError.java
- src/main/java/com/gotechy/bookly/config/GlobalExceptionHandler.java

Auth:
- src/main/java/com/gotechy/bookly/modules/accesos/controller/AuthController.java
- src/main/java/com/gotechy/bookly/modules/accesos/service/AuthService.java
- src/main/java/com/gotechy/bookly/modules/accesos/dto/AuthResponseDTO.java
- src/main/java/com/gotechy/bookly/modules/accesos/dto/MeResponseDTO.java

Envios y ownership:
- src/main/java/com/gotechy/bookly/modules/logistica/controller/EnvioController.java
- src/main/java/com/gotechy/bookly/modules/logistica/services/EnvioService.java
- src/main/java/com/gotechy/bookly/modules/logistica/repository/EnvioRepository.java

Carrito (nuevo):
- src/main/java/com/gotechy/bookly/modules/ventas/controller/CarritoController.java
- src/main/java/com/gotechy/bookly/modules/ventas/services/CarritoService.java
- src/main/java/com/gotechy/bookly/modules/ventas/model/Carrito.java
- src/main/java/com/gotechy/bookly/modules/ventas/model/CarritoItem.java
- src/main/java/com/gotechy/bookly/modules/ventas/repository/CarritoRepository.java
- src/main/java/com/gotechy/bookly/modules/ventas/repository/CarritoItemRepository.java
- DTOs de carrito en src/main/java/com/gotechy/bookly/modules/ventas/dto

---

## 3) Como quedo la seguridad de rutas

### 3.1 Publicas (sin token)

- /api/v1/auth/**
- GET /api/v1/productos
- GET /api/v1/productos/{id}
- GET /api/v1/categorias
- GET /api/v1/categorias/**
- GET /api/v1/autores
- GET /api/v1/autores/**
- GET /api/v1/editoriales
- GET /api/v1/editoriales/**
- GET /api/v1/tipos-producto
- GET /api/v1/tipos-producto/**
- GET /api/v1/rangos-etarios
- GET /api/v1/rangos-etarios/**

### 3.2 Solo ADMIN

- /api/v1/usuarios/** completo.
- Escritura en catalogo (POST, PUT, PATCH, DELETE) para:
  - /api/v1/productos/**
  - /api/v1/categorias/**
  - /api/v1/autores/**
  - /api/v1/editoriales/**
  - /api/v1/tipos-producto/**
  - /api/v1/rangos-etarios/**
- POST /api/v1/envios
- PATCH /api/v1/envios/**
- GET /api/v1/ventas (listado global)

### 3.3 CLIENTE autenticado (y ADMIN donde corresponda)

- POST /api/v1/ventas/checkout
- GET /api/v1/ventas/mis-ordenes
- GET /api/v1/ventas/{id}
- GET /api/v1/envios
- GET /api/v1/envios/venta/{idVenta}
- /api/v1/carrito/**

Nota de negocio:
- En envios y ventas de detalle para CLIENTE se aplica ownership en servicio.
- ADMIN puede ver global; CLIENTE solo recursos propios.

---

## 4) JWT y autenticacion

### 4.1 Claims del token

Ahora el JWT incluye claim de roles:
- roles: ["ROLE_CLIENTE"] o ["ROLE_ADMIN"] segun el usuario.

Esto permite:
- Autorizacion por rol en filtros y anotaciones.
- Mayor trazabilidad de permisos por token.

### 4.2 Respuesta de register/login

AuthResponseDTO ahora devuelve:
- token
- idUsuario
- email
- nombre
- apellido
- rol

Ejemplo esperado:

    {
      "token": "jwt",
      "idUsuario": "0a5d4d50-c2e7-4c07-a35a-8f767fcb38ee",
      "email": "ana@mail.com",
      "nombre": "Ana",
      "apellido": "Perez",
      "rol": "CLIENTE"
    }

### 4.3 Nuevo endpoint de sesion actual

- GET /api/v1/auth/me

Devuelve:
- idUsuario
- email
- nombre
- apellido
- rol

Uso recomendado frontend:
- Al iniciar app con token vigente, consultar /auth/me para hidratar estado de sesion y rol sin reloguear.

---

## 5) Contrato estandar de errores

Se unifico el formato de error en ApiError:
- timestamp
- status
- error
- code
- message
- path
- details

Ejemplo:

    {
      "timestamp": "2026-06-22T15:04:05",
      "status": 403,
      "error": "Forbidden",
      "code": "ACCESS_DENIED",
      "message": "No tienes permisos para acceder a este recurso.",
      "path": "/api/v1/usuarios",
      "details": [
        "Se requiere rol ADMIN."
      ]
    }

### 5.1 Mapeos implementados

- 400
  - VALIDATION_ERROR
  - Incluye details por campo en validaciones DTO.
- 401
  - AUTH_REQUIRED (sin token)
  - TOKEN_INVALID (token mal formado o invalido)
  - TOKEN_EXPIRED (token expirado)
- 403
  - INSUFFICIENT_ROLE (handler de seguridad)
  - ACCESS_DENIED o RESOURCE_NOT_OWNED en errores de dominio/ownership
- 404
  - RESOURCE_NOT_FOUND
- 409
  - DATA_CONFLICT
- 500
  - INTERNAL_ERROR
  - Sin exponer stack trace al cliente.

### 5.2 Diferencia 401 vs 403

- 401: no autenticado o token no usable.
- 403: autenticado pero sin autorizacion suficiente.

Esto corrige el problema de 403 vacios y facilita el manejo en frontend.

---

## 6) Ownership en envios

Implementacion principal:
- EnvioService.listarEnvios()
- EnvioService.obtenerPorIdVenta(idVenta)
- EnvioRepository.findByIdVentaInAndActivoTrue(...)

Reglas:
- ADMIN:
  - puede ver todos los envios activos.
- CLIENTE:
  - en listado, solo ve envios de sus ventas.
  - en detalle por idVenta, valida propiedad.
  - si no pertenece la venta, se retorna 403 con mensaje claro.

Por que:
- Evita exposicion horizontal de datos entre clientes.
- Cumple principio de minimo privilegio.

---

## 7) Flujo de carrito agregado

Endpoints implementados:
- GET /api/v1/carrito/mio
- POST /api/v1/carrito/items
- PATCH /api/v1/carrito/items/{idItem}
- DELETE /api/v1/carrito/items/{idItem}

Comportamiento:
- El carrito se resuelve por cliente autenticado.
- Si no existe carrito activo, se crea automaticamente.
- Al agregar item:
  - valida existencia de producto.
  - valida producto activo.
  - si ya existe item del mismo producto, acumula cantidad.
- Al actualizar o eliminar item:
  - se valida que el item pertenezca al carrito del usuario.

Por que:
- Simplifica UX: frontend no necesita administrar idCarrito manualmente.
- Fortalece seguridad: evita que un usuario manipule items de otro.

---

## 8) SQL para nuevas tablas

A continuacion se incluye SQL recomendado para PostgreSQL, alineado con entidades JPA agregadas.

### 8.1 Script DDL

    BEGIN;

    CREATE TABLE IF NOT EXISTS carrito (
      id_carrito UUID PRIMARY KEY,
      id_cliente UUID NOT NULL,
      activo BOOLEAN NOT NULL DEFAULT TRUE,
      fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      CONSTRAINT fk_carrito_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES cliente(id_cliente)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
    );

    CREATE TABLE IF NOT EXISTS carrito_item (
      id_item UUID PRIMARY KEY,
      id_carrito UUID NOT NULL,
      id_producto UUID NOT NULL,
      cantidad INTEGER NOT NULL CHECK (cantidad > 0),
      precio_unitario NUMERIC(10,2) NOT NULL CHECK (precio_unitario >= 0),
      CONSTRAINT fk_carrito_item_carrito
        FOREIGN KEY (id_carrito)
        REFERENCES carrito(id_carrito)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
      CONSTRAINT fk_carrito_item_producto
        FOREIGN KEY (id_producto)
        REFERENCES producto(id_producto)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
    );

    CREATE INDEX IF NOT EXISTS idx_carrito_cliente_activo
      ON carrito(id_cliente, activo);

    CREATE INDEX IF NOT EXISTS idx_carrito_item_carrito
      ON carrito_item(id_carrito);

    CREATE INDEX IF NOT EXISTS idx_carrito_item_producto
      ON carrito_item(id_producto);

    COMMIT;

### 8.2 Justificacion de diseno SQL

- id_carrito y id_item como UUID:
  - consistente con el resto del dominio.
  - reduce colision en escenarios distribuidos.

- FK carrito -> cliente:
  - garantiza integridad referencial.
  - no permite carrito para cliente inexistente.

- FK carrito_item -> carrito con DELETE CASCADE:
  - si se elimina carrito, se limpian items huerfanos.

- FK carrito_item -> producto:
  - evita items con productos inexistentes.

- Check cantidad > 0:
  - evita inconsistencias de negocio en base.

- Check precio_unitario >= 0:
  - protege de valores invalidos.

- Indices:
  - idx_carrito_cliente_activo: acelera busqueda del carrito actual del usuario.
  - idx_carrito_item_carrito: acelera carga de items del carrito.
  - idx_carrito_item_producto: acelera joins y validaciones por producto.

---

## 9) Impacto en frontend

Beneficios directos:
- Puede consumir catalogo sin autenticacion.
- Recibe rol explicito en login/register.
- Puede recuperar perfil con /auth/me.
- Recibe body de error consistente para UI y mensajes.
- Puede implementar carrito completo con contrato estable.
- Se evita filtrado manual inseguro de envios/ventas en cliente.

---

## 10) Recomendaciones de despliegue

1. Ejecutar script SQL de carrito en entorno de prueba.
2. Verificar datos de roles en tabla rol: ADMIN y CLIENTE.
3. Probar matriz minima:
   - anonimo: GET productos OK, POST productos 401.
   - CLIENTE: mis-ordenes OK, usuarios 403.
   - ADMIN: usuarios OK, gestion de catalogo y envios OK.
4. Validar que frontend mapee code y message para UX de errores.

---

## 11) Nota de consistencia

Este documento describe el estado actual del codigo en la rama local al momento de su generacion.
