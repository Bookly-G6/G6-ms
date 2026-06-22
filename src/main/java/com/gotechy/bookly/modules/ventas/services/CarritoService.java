package com.gotechy.bookly.modules.ventas.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gotechy.bookly.modules.accesos.model.Usuario;
import com.gotechy.bookly.modules.accesos.repository.UsuarioRepository;
import com.gotechy.bookly.modules.catalogo.model.Producto;
import com.gotechy.bookly.modules.catalogo.repository.ProductoRepository;
import com.gotechy.bookly.modules.ventas.dto.CarritoItemRequestDTO;
import com.gotechy.bookly.modules.ventas.dto.CarritoItemResponseDTO;
import com.gotechy.bookly.modules.ventas.dto.CarritoItemUpdateRequestDTO;
import com.gotechy.bookly.modules.ventas.dto.CarritoResponseDTO;
import com.gotechy.bookly.modules.ventas.model.Carrito;
import com.gotechy.bookly.modules.ventas.model.CarritoItem;
import com.gotechy.bookly.modules.ventas.model.Cliente;
import com.gotechy.bookly.modules.ventas.repository.CarritoItemRepository;
import com.gotechy.bookly.modules.ventas.repository.CarritoRepository;
import com.gotechy.bookly.modules.ventas.repository.ClienteRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public CarritoResponseDTO obtenerCarritoMio() {
        UUID idCliente = resolverIdCliente();
        Carrito carrito = obtenerOCrearCarrito(idCliente);
        return mapearCarrito(carrito);
    }

    @Transactional
    public CarritoResponseDTO agregarItem(CarritoItemRequestDTO request) {
        UUID idCliente = resolverIdCliente();
        Carrito carrito = obtenerOCrearCarrito(idCliente);

        Producto producto = productoRepository.findById(request.getIdProducto())
            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + request.getIdProducto()));

        if (Boolean.FALSE.equals(producto.getActivo())) {
            throw new IllegalArgumentException("El producto no está disponible: " + request.getIdProducto());
        }

        // Si el producto ya existe en el carrito, sumar cantidad
        List<CarritoItem> itemsExistentes = carritoItemRepository.findByIdCarrito(carrito.getIdCarrito());
        CarritoItem itemExistente = itemsExistentes.stream()
            .filter(i -> i.getIdProducto().equals(request.getIdProducto()))
            .findFirst()
            .orElse(null);

        if (itemExistente != null) {
            itemExistente.setCantidad(itemExistente.getCantidad() + request.getCantidad());
            carritoItemRepository.save(itemExistente);
        } else {
            CarritoItem nuevoItem = new CarritoItem();
            nuevoItem.setIdCarrito(carrito.getIdCarrito());
            nuevoItem.setIdProducto(request.getIdProducto());
            nuevoItem.setCantidad(request.getCantidad());
            nuevoItem.setPrecioUnitario(producto.getPrecioActual());
            carritoItemRepository.save(nuevoItem);
        }

        return mapearCarrito(carrito);
    }

    @Transactional
    public CarritoResponseDTO actualizarItem(UUID idItem, CarritoItemUpdateRequestDTO request) {
        UUID idCliente = resolverIdCliente();
        Carrito carrito = obtenerOCrearCarrito(idCliente);

        CarritoItem item = carritoItemRepository.findByIdItemAndIdCarrito(idItem, carrito.getIdCarrito())
            .orElseThrow(() -> new EntityNotFoundException("Item no encontrado en el carrito"));

        item.setCantidad(request.getCantidad());
        carritoItemRepository.save(item);

        return mapearCarrito(carrito);
    }

    @Transactional
    public void eliminarItem(UUID idItem) {
        UUID idCliente = resolverIdCliente();
        Carrito carrito = obtenerOCrearCarrito(idCliente);

        CarritoItem item = carritoItemRepository.findByIdItemAndIdCarrito(idItem, carrito.getIdCarrito())
            .orElseThrow(() -> new EntityNotFoundException("Item no encontrado en el carrito"));

        carritoItemRepository.delete(item);
    }

    private Carrito obtenerOCrearCarrito(UUID idCliente) {
        return carritoRepository.findByIdClienteAndActivoTrue(idCliente)
            .orElseGet(() -> {
                Carrito nuevo = new Carrito();
                nuevo.setIdCliente(idCliente);
                nuevo.setActivo(true);
                return carritoRepository.save(nuevo);
            });
    }

    private CarritoResponseDTO mapearCarrito(Carrito carrito) {
        List<CarritoItem> items = carritoItemRepository.findByIdCarrito(carrito.getIdCarrito());

        List<CarritoItemResponseDTO> itemDTOs = items.stream().map(item -> {
            String nombreProducto = productoRepository.findById(item.getIdProducto())
                .map(Producto::getNombreProducto)
                .orElse("Producto no disponible");

            BigDecimal subtotal = item.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(item.getCantidad()));

            return CarritoItemResponseDTO.builder()
                .idItem(item.getIdItem())
                .idProducto(item.getIdProducto())
                .nombreProducto(nombreProducto)
                .cantidad(item.getCantidad())
                .precioUnitario(item.getPrecioUnitario())
                .subtotal(subtotal)
                .build();
        }).collect(Collectors.toList());

        BigDecimal total = itemDTOs.stream()
            .map(CarritoItemResponseDTO::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CarritoResponseDTO.builder()
            .idCarrito(carrito.getIdCarrito())
            .idCliente(carrito.getIdCliente())
            .items(itemDTOs)
            .total(total)
            .fechaCreacion(carrito.getFechaCreacion())
            .build();
    }

    private UUID resolverIdCliente() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Usuario autenticado no encontrado"));

        return clienteRepository.findByIdPersona(usuario.getPersona().getIdPersona())
            .map(Cliente::getIdCliente)
            .orElseThrow(() -> new AccessDeniedException("El usuario no tiene perfil de cliente."));
    }
}
