package com.gotechy.bookly.modules.ventas.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gotechy.bookly.modules.catalogo.repository.ProductoRepository;
import com.gotechy.bookly.modules.ventas.dto.InventarioResponseDTO;
import com.gotechy.bookly.modules.ventas.dto.MovimientoStockRequestDTO;
import com.gotechy.bookly.modules.ventas.dto.MovimientoStockResponseDTO;
import com.gotechy.bookly.modules.ventas.model.Inventario;
import com.gotechy.bookly.modules.ventas.model.InventarioId;
import com.gotechy.bookly.modules.ventas.model.MovimientoStock;
import com.gotechy.bookly.modules.ventas.repository.EmpleadoRepository;
import com.gotechy.bookly.modules.ventas.repository.InventarioRepository;
import com.gotechy.bookly.modules.ventas.repository.MovimientoStockRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private static final String TIPO_ENTRADA = "ENTRADA";
    private static final String TIPO_SALIDA = "SALIDA";

    private final InventarioRepository inventarioRepository;
    private final MovimientoStockRepository movimientoStockRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<InventarioResponseDTO> listarInventario() {
        return inventarioRepository.findAllByOrderByIdIdSucursalAscIdIdProductoAsc()
            .stream()
            .map(this::mapearInventario)
            .toList();
    }

    @Transactional(readOnly = true)
    public InventarioResponseDTO obtenerInventario(Integer idSucursal, UUID idProducto) {
        Inventario inventario = inventarioRepository.findById(new InventarioId(idSucursal, idProducto))
            .orElseThrow(() -> new EntityNotFoundException("Inventario no encontrado para sucursal/producto"));

        return mapearInventario(inventario);
    }

    @Transactional(readOnly = true)
    public List<MovimientoStockResponseDTO> listarMovimientos() {
        return movimientoStockRepository.findAllByOrderByFechaDescIdMovimientoDesc()
            .stream()
            .map(this::mapearMovimiento)
            .toList();
    }

    @Transactional(readOnly = true)
    public MovimientoStockResponseDTO obtenerMovimiento(Integer idMovimiento) {
        MovimientoStock movimiento = movimientoStockRepository.findById(Objects.requireNonNull(idMovimiento, "El idMovimiento es obligatorio"))
            .orElseThrow(() -> new EntityNotFoundException("Movimiento de stock no encontrado: " + idMovimiento));

        return mapearMovimiento(movimiento);
    }

    @Transactional
    public MovimientoStockResponseDTO crearMovimiento(MovimientoStockRequestDTO request) {
        String tipoMovimiento = normalizarYValidarTipoMovimiento(request.getTipoMovimiento());
        return crearMovimientoInterno(request, tipoMovimiento);
    }

    @Transactional
    public MovimientoStockResponseDTO crearEntrada(MovimientoStockRequestDTO request) {
        return crearMovimientoInterno(request, TIPO_ENTRADA);
    }

    @Transactional
    public MovimientoStockResponseDTO crearSalida(MovimientoStockRequestDTO request) {
        return crearMovimientoInterno(request, TIPO_SALIDA);
    }

    private MovimientoStockResponseDTO crearMovimientoInterno(MovimientoStockRequestDTO request, String tipoMovimiento) {
        Integer cantidad = Objects.requireNonNull(request.getCantidad(), "La cantidad es obligatoria");

        validarReferencias(request.getIdProducto(), request.getIdEmpleado());

        Inventario inventario = obtenerOCrearInventario(request.getIdSucursal(), request.getIdProducto());
        int delta = calcularDelta(tipoMovimiento, cantidad);
        int stockResultante = aplicarDeltaConValidacion(inventario, delta, request.getIdProducto());

        MovimientoStock movimiento = new MovimientoStock();
        movimiento.setIdSucursal(request.getIdSucursal());
        movimiento.setIdProducto(request.getIdProducto());
        movimiento.setCantidad(cantidad);
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setIdEmpleado(request.getIdEmpleado());

        movimiento = movimientoStockRepository.save(movimiento);

        return mapearMovimientoConStock(movimiento, stockResultante);
    }

    @Transactional
    public MovimientoStockResponseDTO actualizarMovimiento(Integer idMovimiento, MovimientoStockRequestDTO request) {
        MovimientoStock movimientoExistente = movimientoStockRepository.findById(Objects.requireNonNull(idMovimiento, "El idMovimiento es obligatorio"))
            .orElseThrow(() -> new EntityNotFoundException("Movimiento de stock no encontrado: " + idMovimiento));

        String tipoNuevo = normalizarYValidarTipoMovimiento(request.getTipoMovimiento());
        Integer cantidadNueva = Objects.requireNonNull(request.getCantidad(), "La cantidad es obligatoria");

        validarReferencias(request.getIdProducto(), request.getIdEmpleado());

        Inventario inventarioOriginal = obtenerOCrearInventario(movimientoExistente.getIdSucursal(), movimientoExistente.getIdProducto());
        int deltaOriginal = calcularDelta(movimientoExistente.getTipoMovimiento(), movimientoExistente.getCantidad());
        aplicarDeltaConValidacion(inventarioOriginal, -deltaOriginal, movimientoExistente.getIdProducto());

        Inventario inventarioNuevo = obtenerOCrearInventario(request.getIdSucursal(), request.getIdProducto());
        int deltaNuevo = calcularDelta(tipoNuevo, cantidadNueva);
        int stockResultante = aplicarDeltaConValidacion(inventarioNuevo, deltaNuevo, request.getIdProducto());

        movimientoExistente.setIdSucursal(request.getIdSucursal());
        movimientoExistente.setIdProducto(request.getIdProducto());
        movimientoExistente.setCantidad(cantidadNueva);
        movimientoExistente.setTipoMovimiento(tipoNuevo);
        movimientoExistente.setIdEmpleado(request.getIdEmpleado());
        movimientoExistente.setFecha(LocalDateTime.now());

        movimientoExistente = movimientoStockRepository.save(movimientoExistente);

        return mapearMovimientoConStock(movimientoExistente, stockResultante);
    }

    @Transactional
    public void eliminarMovimiento(Integer idMovimiento) {
        MovimientoStock movimiento = movimientoStockRepository.findById(Objects.requireNonNull(idMovimiento, "El idMovimiento es obligatorio"))
            .orElseThrow(() -> new EntityNotFoundException("Movimiento de stock no encontrado: " + idMovimiento));

        Inventario inventario = obtenerOCrearInventario(movimiento.getIdSucursal(), movimiento.getIdProducto());
        int delta = calcularDelta(movimiento.getTipoMovimiento(), movimiento.getCantidad());
        aplicarDeltaConValidacion(inventario, -delta, movimiento.getIdProducto());

        movimientoStockRepository.delete(movimiento);
    }

    private Inventario obtenerOCrearInventario(Integer idSucursal, UUID idProducto) {
        InventarioId inventarioId = new InventarioId(idSucursal, idProducto);
        return inventarioRepository.findById(inventarioId)
            .orElseGet(() -> {
                Inventario nuevo = new Inventario();
                nuevo.setId(inventarioId);
                nuevo.setStock(0);
                return inventarioRepository.save(nuevo);
            });
    }

    private int aplicarDeltaConValidacion(Inventario inventario, int delta, UUID idProducto) {
        int stockActual = Objects.requireNonNullElse(inventario.getStock(), 0);
        int nuevoStock = stockActual + delta;

        if (nuevoStock < 0) {
            throw new IllegalArgumentException("Stock insuficiente para el producto: " + idProducto);
        }

        inventario.setStock(nuevoStock);
        inventarioRepository.save(inventario);
        return nuevoStock;
    }

    private int calcularDelta(String tipoMovimiento, Integer cantidad) {
        if (TIPO_ENTRADA.equals(tipoMovimiento)) {
            return cantidad;
        }

        return -cantidad;
    }

    private String normalizarYValidarTipoMovimiento(String tipoMovimiento) {
        String tipo = Objects.requireNonNull(tipoMovimiento, "El tipoMovimiento es obligatorio").trim().toUpperCase();

        if (!TIPO_ENTRADA.equals(tipo) && !TIPO_SALIDA.equals(tipo)) {
            throw new IllegalArgumentException("tipoMovimiento debe ser ENTRADA o SALIDA");
        }

        return tipo;
    }

    private void validarReferencias(UUID idProducto, UUID idEmpleado) {
        UUID productoId = Objects.requireNonNull(idProducto, "El idProducto es obligatorio");
        UUID empleadoId = Objects.requireNonNull(idEmpleado, "El idEmpleado es obligatorio");

        if (!productoRepository.existsById(productoId)) {
            throw new EntityNotFoundException("Producto no encontrado: " + productoId);
        }

        if (!empleadoRepository.existsById(empleadoId)) {
            throw new EntityNotFoundException("Empleado no encontrado: " + empleadoId);
        }
    }

    private InventarioResponseDTO mapearInventario(Inventario inventario) {
        InventarioResponseDTO dto = new InventarioResponseDTO();
        dto.setIdSucursal(inventario.getId().getIdSucursal());
        dto.setIdProducto(inventario.getId().getIdProducto());
        dto.setStock(inventario.getStock());
        return dto;
    }

    private MovimientoStockResponseDTO mapearMovimiento(MovimientoStock movimiento) {
        Inventario inventario = inventarioRepository.findById(new InventarioId(movimiento.getIdSucursal(), movimiento.getIdProducto()))
            .orElse(null);

        Integer stockResultante = inventario != null ? inventario.getStock() : 0;
        return mapearMovimientoConStock(movimiento, stockResultante);
    }

    private MovimientoStockResponseDTO mapearMovimientoConStock(MovimientoStock movimiento, Integer stockResultante) {
        MovimientoStockResponseDTO dto = new MovimientoStockResponseDTO();
        dto.setIdMovimiento(movimiento.getIdMovimiento());
        dto.setIdSucursal(movimiento.getIdSucursal());
        dto.setIdProducto(movimiento.getIdProducto());
        dto.setCantidad(movimiento.getCantidad());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento());
        dto.setFecha(movimiento.getFecha());
        dto.setIdEmpleado(movimiento.getIdEmpleado());
        dto.setStockResultante(stockResultante);
        return dto;
    }
}
