
## Configuración Local Frontend

Para consumir esta API localmente:

**Base URL:** `http://localhost:8080/api/v1`

---

## Módulo: Catálogo de Productos

Todas las rutas de este módulo nacen del endpoint `/productos`. Los IDs manejados por el sistema son de tipo **UUID**.

### Tabla de Rutas Disponibles

| Método | Endpoint | Descripción | Requiere Body |
| :--- | :--- | :--- | :---: |
| `POST` | `/productos` | Crea un nuevo producto en el catálogo | Sí |
| `PUT` | `/productos/{id}` | Actualiza los datos de un producto existente | Sí |
| `DELETE`| `/productos/{id}` | Realiza un borrado lógico (Soft Delete) | No |

---

### Formato del Payload (Body)

Tanto para crear (`POST`) como para actualizar (`PUT`), el backend espera recibir un objeto JSON con la siguiente estructura estricta. 

**Importante:** Las claves foráneas (`idTipoProducto`, `idEditorialSello`, `idRangoEtario`) deben existir en la base de datos o si no la API devolverá un error.

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

---

## Módulo: Logística y Envíos

Todas las rutas de este módulo nacen del endpoint `/logistica`. Gestiona el flujo físico y digital de los pedidos una vez que la venta fue concretada. Los IDs manejados son **UUID**.

### Tabla de Rutas Disponibles

| Método | Endpoint | Descripción | Requiere Body |
| :--- | :--- | :--- | :---: |
| `POST` | `/logistica` | Crea una nueva orden de logística atada a una venta | Sí |
| `GET` | `/logistica/{id}` | Obtiene los detalles de un envío específico | No |
| `PUT` | `/logistica/{id}` | Actualiza el estado o datos de un envío | Sí |
| `DELETE`| `/logistica/{id}` | Realiza un borrado lógico (Soft Delete) | No |

---

### Formato del Payload (Body)

Tanto para crear (`POST`) como para actualizar (`PUT`), el backend espera recibir un objeto JSON. La estructura varía dependiendo de la regla de negocio atada al campo `tipoEnvio`.

**Valores permitidos para `tipoEnvio`:** `"DOMICILIO"`, `"RETIRO_SUCURSAL"`, `"DIGITAL"`.

#### Escenario 1: Envío a Domicilio
Requiere los datos de la empresa de correo. El backend setea el estado inicial como `PENDIENTE`.
```json
{
  "idVenta": "88888888-4444-4444-4444-123456789012",
  "tipoEnvio": "DOMICILIO",
  "empresaCorreo": "OCA",
  "numeroTracking": "OCA-999888777",
  "observaciones": "Entregar de 9 a 18hs"
}
```

#### Escenario 2: Retiro en Sucursal
No enviar datos de correo. El backend autogenera un codigoRetiro (Ej: "BKL-1234") y lo devuelve en la respuesta. Setea el estado como PENDIENTE.
```
{
  "idVenta": "99999999-5555-5555-5555-987654321098",
  "tipoEnvio": "RETIRO_SUCURSAL",
  "observaciones": "Pasa el titular con DNI"
}
```

#### Escenario 3: Producto Digital
Entrega inmediata. El backend setea el estado logístico automáticamente a ENTREGADO. No requiere datos de envío ni tracking.
```
{
  "idVenta": "77777777-3333-3333-3333-123456789012",
  "tipoEnvio": "DIGITAL"
}
```
