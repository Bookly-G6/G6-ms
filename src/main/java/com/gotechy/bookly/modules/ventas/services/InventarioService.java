package com.gotechy.bookly.modules.ventas.services;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gotechy.bookly.modules.catalogo.model.Producto;
import com.gotechy.bookly.modules.catalogo.repository.ProductoRepository;
import com.gotechy.bookly.modules.catalogo.service.ProductoService;
import com.gotechy.bookly.modules.ventas.dto.InventarioResponseDTO;
import com.gotechy.bookly.modules.ventas.dto.MovimientoStockRequestDTO;
import com.gotechy.bookly.modules.ventas.dto.MovimientoStockResponseDTO;
import com.gotechy.bookly.modules.ventas.model.MovimientoStock;
import com.gotechy.bookly.modules.ventas.repository.EmpleadoRepository;
import com.gotechy.bookly.modules.ventas.repository.MovimientoStockRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private static final String TIPO_ENTRADA = "ENTRADA";
    private static final String TIPO_SALIDA = "SALIDA";
    private static final String MENSAJE_ID_MOVIMIENTO_OBLIGATORIO = "El idMovimiento es obligatorio";
    private static final String MENSAJE_MOVIMIENTO_NO_ENCONTRADO = "Movimiento de stock no encontrado: ";

    private final MovimientoStockRepository movimientoStockRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ProductoRepository productoRepository;
    private final ProductoService productoService;

    @Transactional(readOnly = true)
    public List<InventarioResponseDTO> listarInventario() {
        return productoRepository.findAll()
            .stream()
            .map(this::mapearInventario)
            .toList();
    }

    @Transactional(readOnly = true)
    public InventarioResponseDTO obtenerInventario(UUID idProducto) {
        Producto producto = productoRepository.findById(idProducto)
            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + idProducto));

        return mapearInventario(producto);
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
        MovimientoStock movimiento = movimientoStockRepository
            .findById(Objects.requireNonNull(idMovimiento, MENSAJE_ID_MOVIMIENTO_OBLIGATORIO))
            .orElseThrow(() -> new EntityNotFoundException(MENSAJE_MOVIMIENTO_NO_ENCONTRADO + idMovimiento));

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

        Producto producto = obtenerProducto(request.getIdProducto());
        int delta = calcularDelta(tipoMovimiento, cantidad);
        int stockResultante = aplicarDeltaConValidacion(producto, delta, request.getIdProducto());

        MovimientoStock movimiento = new MovimientoStock();
        movimiento.setIdProducto(request.getIdProducto());
        movimiento.setCantidad(cantidad);
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setFecha(LocalDateTime.now(ZoneOffset.UTC));
        movimiento.setIdEmpleado(request.getIdEmpleado());
        movimiento.setStockResultante(stockResultante);

        movimiento = movimientoStockRepository.save(movimiento);

        return mapearMovimientoConStock(movimiento, stockResultante);
    }

    @Transactional
    public MovimientoStockResponseDTO actualizarMovimiento(Integer idMovimiento, MovimientoStockRequestDTO request) {
        MovimientoStock movimientoExistente = movimientoStockRepository
            .findById(Objects.requireNonNull(idMovimiento, MENSAJE_ID_MOVIMIENTO_OBLIGATORIO))
            .orElseThrow(() -> new EntityNotFoundException(MENSAJE_MOVIMIENTO_NO_ENCONTRADO + idMovimiento));

        String tipoNuevo = normalizarYValidarTipoMovimiento(request.getTipoMovimiento());
        Integer cantidadNueva = Objects.requireNonNull(request.getCantidad(), "La cantidad es obligatoria");

        validarReferencias(request.getIdProducto(), request.getIdEmpleado());

        Producto productoOriginal = obtenerProducto(movimientoExistente.getIdProducto());
        int deltaOriginal = calcularDelta(movimientoExistente.getTipoMovimiento(), movimientoExistente.getCantidad());
        aplicarDeltaConValidacion(productoOriginal, -deltaOriginal, movimientoExistente.getIdProducto());

        Producto productoNuevo = obtenerProducto(request.getIdProducto());
        int deltaNuevo = calcularDelta(tipoNuevo, cantidadNueva);
        int stockResultante = aplicarDeltaConValidacion(productoNuevo, deltaNuevo, request.getIdProducto());

        movimientoExistente.setIdProducto(request.getIdProducto());
        movimientoExistente.setCantidad(cantidadNueva);
        movimientoExistente.setTipoMovimiento(tipoNuevo);
        movimientoExistente.setIdEmpleado(request.getIdEmpleado());
        movimientoExistente.setFecha(LocalDateTime.now(ZoneOffset.UTC));
        movimientoExistente.setStockResultante(stockResultante);

        movimientoExistente = movimientoStockRepository.save(movimientoExistente);

        return mapearMovimientoConStock(movimientoExistente, stockResultante);
    }

    @Transactional
    public void eliminarMovimiento(Integer idMovimiento) {
        MovimientoStock movimiento = movimientoStockRepository
            .findById(Objects.requireNonNull(idMovimiento, MENSAJE_ID_MOVIMIENTO_OBLIGATORIO))
            .orElseThrow(() -> new EntityNotFoundException(MENSAJE_MOVIMIENTO_NO_ENCONTRADO + idMovimiento));

        Producto producto = obtenerProducto(movimiento.getIdProducto());
        int delta = calcularDelta(movimiento.getTipoMovimiento(), movimiento.getCantidad());
        aplicarDeltaConValidacion(producto, -delta, movimiento.getIdProducto());

        movimientoStockRepository.delete(movimiento);
    }

    private Producto obtenerProducto(UUID idProducto) {
        return productoRepository.findById(idProducto)
            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + idProducto));
    }

    private int aplicarDeltaConValidacion(Producto producto, int delta, UUID idProducto) {
        int stockActual = Objects.requireNonNullElse(producto.getStock(), 0);
        int nuevoStock = stockActual + delta;

        if (nuevoStock < 0) {
            throw new IllegalArgumentException("Stock insuficiente para el producto: " + idProducto);
        }

        producto.setStock(nuevoStock);
        productoRepository.save(producto);
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

    private InventarioResponseDTO mapearInventario(Producto producto) {
        InventarioResponseDTO dto = new InventarioResponseDTO();
        dto.setIdProducto(producto.getIdProducto());
        dto.setStock(Objects.requireNonNullElse(producto.getStock(), 0));
        dto.setProducto(productoService.mapearAResponseDTO(producto));
        return dto;
    }

    private MovimientoStockResponseDTO mapearMovimiento(MovimientoStock movimiento) {
        Integer stockResultante = movimiento.getStockResultante();
        if (stockResultante == null) {
            Producto producto = productoRepository.findById(movimiento.getIdProducto()).orElse(null);
            if (producto != null) {
                stockResultante = Objects.requireNonNullElse(producto.getStock(), 0);
            }
        }
        return mapearMovimientoConStock(movimiento, stockResultante != null ? stockResultante : 0);
    }

    private MovimientoStockResponseDTO mapearMovimientoConStock(MovimientoStock movimiento, Integer stockResultante) {
        MovimientoStockResponseDTO dto = new MovimientoStockResponseDTO();
        dto.setIdMovimiento(movimiento.getIdMovimiento());
        dto.setIdProducto(movimiento.getIdProducto());
        dto.setCantidad(movimiento.getCantidad());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento());
        dto.setFecha(movimiento.getFecha());
        dto.setIdEmpleado(movimiento.getIdEmpleado());
        dto.setStockResultante(stockResultante);
        return dto;
    }
}
