package com.gotechy.bookly.modules.logistica.dto;

import com.gotechy.bookly.core.enums.EstadoLogistica;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class ActualizarEstadoRequestDTO {

    @NotNull(message = "El nuevo estado es obligatorio")
    private EstadoLogistica nuevoEstado;

    private String numeroTracking;
    private String empresaCorreo;
    //se saca del token despues
    private UUID idEmpleado;
}
