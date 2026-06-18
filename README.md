
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
