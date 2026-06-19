package com.gotechy.bookly.modules.logistica.service;

import com.gotechy.bookly.core.enums.EstadoLogistica;
import com.gotechy.bookly.core.enums.TipoEnvio;
import com.gotechy.bookly.modules.logistica.dto.EnvioRequestDTO;
import com.gotechy.bookly.modules.logistica.model.Envio;
import com.gotechy.bookly.modules.logistica.repository.EnvioRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnvioService {

    private final EnvioRepository envioRepository;

    public List<Envio> obtenerTodosLosEnvios() {
        return envioRepository.findByActivoTrue();
    }

    public Envio obtenerEnvio(UUID idEnvio) {
        Envio envio = envioRepository
            .findById(idEnvio)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "No se encontró el envío con ID: " + idEnvio
                )
            );

        // No devolver envíos dados de baja
        if (!envio.getActivo()) {
            throw new EntityNotFoundException(
                "El envío solicitado fue dado de baja del sistema."
            );
        }

        return envio;
    }

    public Envio crearEnvio(EnvioRequestDTO dto) {
        Envio envio = new Envio();
        envio.setIdVenta(dto.idVenta());
        envio.setTipoEnvio(dto.tipoEnvio());
        envio.setFechaActualizacion(LocalDateTime.now());
        envio.setObservaciones(dto.observaciones());

        // Lógica de negocio condicional
        if (dto.tipoEnvio() == TipoEnvio.RETIRO_SUCURSAL) {
            envio.setEstadoLogistica(EstadoLogistica.PENDIENTE);
            envio.setCodigoRetiro(generarCodigoRetiro());
            envio.setEmpresaCorreo(null);
            envio.setNumeroTracking(null);
        } else if (dto.tipoEnvio() == TipoEnvio.DIGITAL) {
            // Entrega inmediata sin movimiento físico
            envio.setEstadoLogistica(EstadoLogistica.ENTREGADO);
            envio.setCodigoRetiro(null);
            envio.setEmpresaCorreo(null);
            envio.setNumeroTracking(null);
            envio.setObservaciones("Despacho digital automático");
        } else {
            // Es a domicilio
            envio.setEstadoLogistica(EstadoLogistica.PENDIENTE);
            envio.setEmpresaCorreo(dto.empresaCorreo());
            envio.setNumeroTracking(dto.numeroTracking());
            envio.setCodigoRetiro(null);
        }

        return envioRepository.save(envio);
    }

    public Envio actualizarEnvio(UUID idEnvio, EnvioRequestDTO dto) {
        // Usamos método seguro, si está inactivo o no existe, corta y devuelve 404
        Envio envioExistente = obtenerEnvio(idEnvio);

        envioExistente.setTipoEnvio(dto.tipoEnvio());
        envioExistente.setFechaActualizacion(LocalDateTime.now());
        envioExistente.setObservaciones(dto.observaciones());

        // Repite la lógica condicional para asegurar coherencia de datos al editar
        if (dto.tipoEnvio() == TipoEnvio.RETIRO_SUCURSAL) {
            // Mantiene el código si ya tenía uno, si no, lo genera
            envioExistente.setCodigoRetiro(
                envioExistente.getCodigoRetiro() != null
                    ? envioExistente.getCodigoRetiro()
                    : generarCodigoRetiro()
            );
            envioExistente.setEmpresaCorreo(null);
            envioExistente.setNumeroTracking(null);
        } else if (dto.tipoEnvio() == TipoEnvio.DIGITAL) {
            envioExistente.setEstadoLogistica(EstadoLogistica.ENTREGADO);
            envioExistente.setCodigoRetiro(null);
            envioExistente.setEmpresaCorreo(null);
            envioExistente.setNumeroTracking(null);
        } else {
            // Es a DOMICILIO
            envioExistente.setEmpresaCorreo(dto.empresaCorreo());
            envioExistente.setNumeroTracking(dto.numeroTracking());
            envioExistente.setCodigoRetiro(null);
        }

        return envioRepository.save(envioExistente);
    }

    public void eliminarEnvio(UUID idEnvio) {
        // Valida que exista y que no esté ya dado de baja
        Envio envioExistente = obtenerEnvio(idEnvio);

        envioExistente.setActivo(false);
        envioExistente.setFechaActualizacion(LocalDateTime.now());

        envioRepository.save(envioExistente);
    }

    private String generarCodigoRetiro() {
        Random random = new Random();
        int numero = 1000 + random.nextInt(9000);
        return "BKL-" + numero;
    }
}
