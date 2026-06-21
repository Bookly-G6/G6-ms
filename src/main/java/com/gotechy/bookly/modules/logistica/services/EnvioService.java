package com.gotechy.bookly.modules.logistica.services;

import com.gotechy.bookly.core.enums.EstadoLogistica;
import com.gotechy.bookly.modules.logistica.dto.EnvioRequestDTO;
import com.gotechy.bookly.modules.logistica.dto.EnvioResponseDTO;
import com.gotechy.bookly.modules.logistica.model.Envio;
import com.gotechy.bookly.modules.logistica.model.HistorialEnvio;
import com.gotechy.bookly.modules.logistica.repository.EnvioRepository;
import com.gotechy.bookly.modules.logistica.repository.HistorialEnvioRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnvioService {

    private final EnvioRepository envioRepository;
    private final HistorialEnvioRepository historialEnvioRepository;

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

        // Actualiza los datos del envío
        envio.setEstadoLogistica(nuevoEstado);
        if (tracking != null) envio.setNumeroTracking(tracking);
        if (correo != null) envio.setEmpresaCorreo(correo);

        Envio envioActualizado = envioRepository.save(envio);

        // Guarda el movimiento en el historial
        registrarHistorial(
            envioActualizado,
            estadoAnterior,
            nuevoEstado,
            "Cambio de estado logístico",
            idEmpleado
        );

        return mapearAResponseDTO(envioActualizado);
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
        historial.setIdEmpleado(idEmpleado); // Quién hizo el cambio
        historialEnvioRepository.save(historial);
    }

    private EnvioResponseDTO mapearAResponseDTO(Envio envio) {
        EnvioResponseDTO dto = new EnvioResponseDTO();
        dto.setIdEnvio(envio.getIdEnvio());
        dto.setIdVenta(envio.getIdVenta());
        dto.setTipoEnvio(envio.getTipoEnvio().name());
        dto.setEstadoLogistica(envio.getEstadoLogistica().name());
        dto.setCodigoRetiro(envio.getCodigoRetiro());
        dto.setEmpresaCorreo(envio.getEmpresaCorreo());
        dto.setNumeroTracking(envio.getNumeroTracking());
        dto.setFechaActualizacion(envio.getFechaActualizacion());
        return dto;
    }

    public List<EnvioResponseDTO> listarEnviosActivos() {
        return envioRepository
            .findByActivoTrue()
            .stream()
            .map(this::mapearAResponseDTO)
            .collect(Collectors.toList());
    }

    public EnvioResponseDTO obtenerPorIdVenta(UUID idVenta) {
        Envio envio = envioRepository
            .findByIdVentaAndActivoTrue(idVenta)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "No se encontró un envío activo para la venta solicitada"
                )
            );
        return mapearAResponseDTO(envio);
    }

    private String generarCodigoRetiro() {
        return (
            "RET-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase()
        );
    }
}
