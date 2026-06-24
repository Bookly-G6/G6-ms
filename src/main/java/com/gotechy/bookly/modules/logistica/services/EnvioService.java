package com.gotechy.bookly.modules.logistica.services;

import com.gotechy.bookly.core.enums.EstadoLogistica;
import com.gotechy.bookly.modules.accesos.model.Usuario;
import com.gotechy.bookly.modules.accesos.repository.UsuarioRepository;
import com.gotechy.bookly.modules.logistica.dto.EnvioRequestDTO;
import com.gotechy.bookly.modules.logistica.dto.EnvioResponseDTO;
import com.gotechy.bookly.modules.logistica.model.Envio;
import com.gotechy.bookly.modules.logistica.model.HistorialEnvio;
import com.gotechy.bookly.modules.logistica.repository.EnvioRepository;
import com.gotechy.bookly.modules.logistica.repository.HistorialEnvioRepository;
import com.gotechy.bookly.modules.ventas.model.Cliente;
import com.gotechy.bookly.modules.ventas.model.Venta;
import com.gotechy.bookly.modules.ventas.repository.ClienteRepository;
import com.gotechy.bookly.modules.ventas.repository.VentaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnvioService {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private final EnvioRepository envioRepository;
    private final HistorialEnvioRepository historialEnvioRepository;
    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public EnvioResponseDTO inicializarEnvio(EnvioRequestDTO requestDTO) {
        if (
            envioRepository
                .findByIdVentaAndActivoTrue(requestDTO.getIdVenta())
                .isPresent()
        ) {
            throw new IllegalArgumentException(
                "La venta especificada ya tiene un envío en curso."
            );
        }

        Envio nuevoEnvio = new Envio();
        nuevoEnvio.setIdVenta(requestDTO.getIdVenta());
        nuevoEnvio.setTipoEnvio(requestDTO.getTipoEnvio());
        nuevoEnvio.setEstadoLogistica(EstadoLogistica.EN_PREPARACION);
        nuevoEnvio.setObservaciones(requestDTO.getObservaciones());

        if (requestDTO.getTipoEnvio().name().equals("RETIRO_SUCURSAL")) {
            nuevoEnvio.setCodigoRetiro(generarCodigoRetiro());
        }

        Envio envioGuardado = envioRepository.save(nuevoEnvio);
        registrarHistorial(
            envioGuardado,
            null,
            EstadoLogistica.EN_PREPARACION,
            "Envío inicializado por el sistema",
            null
        );

        return mapearAResponseDTO(envioGuardado);
    }

    @Transactional
    public EnvioResponseDTO actualizarEstado(
        UUID idEnvio,
        EstadoLogistica nuevoEstado,
        String tracking,
        String correo,
        UUID idEmpleado
    ) {
        Envio envio = envioRepository
            .findById(idEnvio)
            .orElseThrow(() ->
                new EntityNotFoundException("Envío no encontrado")
            );

        EstadoLogistica estadoAnterior = envio.getEstadoLogistica();
        envio.setEstadoLogistica(nuevoEstado);
        if (tracking != null) envio.setNumeroTracking(tracking);
        if (correo != null) envio.setEmpresaCorreo(correo);

        Envio envioActualizado = envioRepository.save(envio);
        registrarHistorial(
            envioActualizado,
            estadoAnterior,
            nuevoEstado,
            "Cambio de estado logístico",
            idEmpleado
        );

        return mapearAResponseDTO(envioActualizado);
    }

    @Transactional(readOnly = true)
    public List<EnvioResponseDTO> listarEnvios() {
        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();
        if (esAdmin(auth)) {
            return envioRepository
                .findByActivoTrue()
                .stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
        }

        UUID idCliente = resolverIdCliente(auth.getName());
        Set<UUID> idsVentasCliente = ventaRepository
            .findByIdClienteOrderByFechaDesc(idCliente)
            .stream()
            .map(Venta::getIdVenta)
            .collect(Collectors.toSet());

        return envioRepository
            .findByIdVentaInAndActivoTrue(idsVentasCliente)
            .stream()
            .map(this::mapearAResponseDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EnvioResponseDTO obtenerPorIdVenta(UUID idVenta) {
        Envio envio = envioRepository
            .findByIdVentaAndActivoTrue(idVenta)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "No se encontró un envío activo para la venta solicitada"
                )
            );

        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();
        if (!esAdmin(auth)) {
            verificarPropiedadVenta(idVenta, auth.getName());
        }

        return mapearAResponseDTO(envio);
    }

    @Transactional(readOnly = true)
    public Page<EnvioResponseDTO> obtenerPendientes(
        UUID idVenta,
        String terminoBusqueda,
        int page,
        int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        EstadoLogistica estado = EstadoLogistica.EN_PREPARACION;

        Page<Envio> enviosPage = envioRepository.buscarEnviosDinamicos(
            estado,
            idVenta,
            terminoBusqueda,
            pageable
        );

        return enviosPage.map(this::mapearAResponseDTO);
    }

    private void verificarPropiedadVenta(UUID idVenta, String email) {
        Venta venta = ventaRepository
            .findById(idVenta)
            .orElseThrow(() ->
                new EntityNotFoundException("Venta no encontrada")
            );
        UUID idCliente = resolverIdCliente(email);
        if (!idCliente.equals(venta.getIdCliente())) {
            throw new AccessDeniedException(
                "No tienes permiso para ver el envío de esta venta."
            );
        }
    }

    private UUID resolverIdCliente(String email) {
        Usuario usuario = usuarioRepository
            .findByEmail(email)
            .orElseThrow(() ->
                new EntityNotFoundException("Usuario no encontrado")
            );
        return clienteRepository
            .findByIdPersona(usuario.getPersona().getIdPersona())
            .map(Cliente::getIdCliente)
            .orElseThrow(() ->
                new AccessDeniedException(
                    "El usuario no tiene perfil de cliente."
                )
            );
    }

    private boolean esAdmin(Authentication auth) {
        return (
            auth != null &&
            auth
                .getAuthorities()
                .stream()
                .anyMatch(a -> ROLE_ADMIN.equals(a.getAuthority()))
        );
    }

    private void registrarHistorial(
        Envio envio,
        EstadoLogistica estadoAnterior,
        EstadoLogistica estadoNuevo,
        String observaciones,
        UUID idEmpleado
    ) {
        HistorialEnvio historial = new HistorialEnvio();
        historial.setEnvio(envio);
        historial.setEstadoAnterior(estadoAnterior);
        historial.setEstadoNuevo(estadoNuevo);
        historial.setObservaciones(observaciones);
        historial.setIdEmpleado(idEmpleado);
        historialEnvioRepository.save(historial);
    }

    private EnvioResponseDTO mapearAResponseDTO(Envio envio) {
        EnvioResponseDTO dto = new EnvioResponseDTO();
        dto.setIdEnvio(envio.getIdEnvio());
        dto.setIdVenta(envio.getIdVenta());

        dto.setTipoEnvio(
            envio.getTipoEnvio() != null
                ? envio.getTipoEnvio().name()
                : "NO_ASIGNADO"
        );
        dto.setEstadoLogistica(
            envio.getEstadoLogistica() != null
                ? envio.getEstadoLogistica().name()
                : "DESCONOCIDO"
        );

        dto.setCodigoRetiro(envio.getCodigoRetiro());
        dto.setEmpresaCorreo(envio.getEmpresaCorreo());
        dto.setNumeroTracking(envio.getNumeroTracking());
        dto.setFechaActualizacion(envio.getFechaActualizacion());
        return dto;
    }

    private String generarCodigoRetiro() {
        return (
            "RET-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase()
        );
    }
}
