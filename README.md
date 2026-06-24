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
