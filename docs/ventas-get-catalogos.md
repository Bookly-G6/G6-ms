# GET de Ventas para Frontend (datos legibles)

Esta guía resume los endpoints GET para mostrar datos completos en UI (no solo IDs).

## Base

- URL base: `http://localhost:8080/api/v1`
- Header: `Authorization: Bearer <token>`

## 1) Tipos de venta

- Metodo: `GET`
- Endpoint: `/ventas/tipos-venta`
- Roles: `ADMIN`, `CLIENTE`, `VENDEDOR`

### Respuesta ejemplo

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
    "descripcion": "Venta presencial atendida por personal de sucursal",
    "requiereEmpleado": true,
    "generaEnvioAutomatico": false
  }
]
```

Uso recomendado en frontend:
- Mostrar `nombre` en un select.
- Enviar `codigo` en checkout como `origenVenta`.

## 2) Formas de pago

- Metodo: `GET`
- Endpoint: `/ventas/formas-pago`
- Roles: `ADMIN`, `CLIENTE`, `VENDEDOR`

### Respuesta ejemplo

```json
[
  {
    "idFormaPago": 1,
    "nombrePago": "EFECTIVO"
  },
  {
    "idFormaPago": 2,
    "nombrePago": "TARJETA DE CREDITO"
  }
]
```

Uso recomendado en frontend:
- Renderizar `nombrePago`.
- En checkout enviar `idFormaPago` en cada pago.

## 3) Empleados

- Metodo: `GET`
- Endpoint: `/ventas/empleados`
- Roles: `ADMIN`, `VENDEDOR`

### Respuesta ejemplo

```json
[
  {
    "idEmpleado": "e2e2e2e2-e2e2-e2e2-e2e2-e2e2e2e2e2e2",
    "idPersona": "a1a1a1a1-a1a1-a1a1-a1a1-a1a1a1a1a1a1",
    "idSucursal": 1,
    "nombreCompleto": "Carlos Logistico",
    "nombre": "Carlos",
    "apellido": "Logistico",
    "dni": "20111222",
    "telefono": null
  }
]
```

Uso recomendado en frontend:
- Mostrar `nombreCompleto` (o `nombre` + `apellido`).
- Guardar y enviar `idEmpleado` en las operaciones que lo requieran.

## Cambio funcional aplicado: sucursal fija en backend

Para simplificar el frontend:
- En `POST /ventas/checkout`, el backend fija `idSucursal = 1`.
- En movimientos de stock (`/inventario/movimientos`), el backend fija `idSucursal = 1`.

Esto significa que el frontend ya no necesita pedir ni enviar sucursal para esos flujos.
