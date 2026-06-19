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

Todas las rutas de este módulo nacen del endpoint `/logistica`. Este módulo administra el flujo físico y digital de los pedidos una vez concretada una venta. Los IDs utilizados son de tipo **UUID**.

## Endpoints Disponibles

| Método   | Endpoint          | Descripción                                         | Requiere Body |
| :------- | :---------------- | :-------------------------------------------------- | :-----------: |
| `POST`   | `/logistica`      | Crea una nueva orden logística asociada a una venta |       Sí      |
| `GET`    | `/logistica/{id}` | Obtiene los detalles de un envío específico         |       No      |
| `GET`    | `/logistica`      | Obtiene el listado completo de envíos activos       |       No      |
| `PUT`    | `/logistica/{id}` | Actualiza datos o estado de un envío                |       Sí      |
| `DELETE` | `/logistica/{id}` | Realiza un borrado lógico (Soft Delete)             |       No      |

---

## Payload de Creación y Actualización

La estructura del JSON depende del tipo de envío seleccionado.

### Valores permitidos para `tipoEnvio`

* `DOMICILIO`
* `RETIRO_SUCURSAL`
* `DIGITAL`

---

### Escenario 1: Envío a Domicilio

Requiere información de la empresa de correo. El backend asigna automáticamente el estado inicial `PENDIENTE`.

#### Ejemplo de Request

```json
{
  "idVenta": "88888888-4444-4444-4444-123456789012",
  "tipoEnvio": "DOMICILIO",
  "empresaCorreo": "OCA",
  "numeroTracking": "OCA-999888777",
  "observaciones": "Entregar de 9 a 18hs"
}
```

#### Resultado Esperado

* Se crea la orden logística.
* El estado inicial se registra como `PENDIENTE`.
* Se almacenan los datos del correo y el número de tracking.

---

### Escenario 2: Retiro en Sucursal

No deben enviarse datos de correo ni número de seguimiento. El backend genera automáticamente un código de retiro.

#### Ejemplo de Request

```json
{
  "idVenta": "99999999-5555-5555-5555-987654321098",
  "tipoEnvio": "RETIRO_SUCURSAL",
  "observaciones": "Pasa el titular con DNI"
}
```

#### Resultado Esperado

* Se crea la orden logística.
* El estado inicial se registra como `PENDIENTE`.
* El backend genera automáticamente un código de retiro.
* Ejemplo de código generado: `BKL-1234`.

---

### Escenario 3: Producto Digital

No requiere datos de envío, correo ni seguimiento.

#### Ejemplo de Request

```json
{
  "idVenta": "77777777-3333-3333-3333-123456789012",
  "tipoEnvio": "DIGITAL"
}
```

#### Resultado Esperado

* Se crea la orden logística.
* El backend asigna automáticamente el estado `ENTREGADO`.
* No se genera tracking ni código de retiro.

---

## Consideraciones Generales

* Todos los identificadores utilizados por la API son UUID.
* Los endpoints `DELETE` realizan un borrado lógico (*Soft Delete*).
* Las referencias enviadas en los payloads deben existir previamente en la base de datos.
* Los estados logísticos son gestionados automáticamente según las reglas de negocio definidas para cada tipo de envío.
