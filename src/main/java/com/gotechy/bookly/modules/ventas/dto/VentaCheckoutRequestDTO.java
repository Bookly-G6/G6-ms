package com.gotechy.bookly.modules.ventas.dto;

import java.util.List;
import java.util.UUID;

import com.gotechy.bookly.core.enums.TipoEnvio;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class VentaCheckoutRequestDTO {

    @NotBlank(message = "El origen de venta es obligatorio")
    private String origenVenta;

    private UUID idCliente;

    private UUID idEmpleado;

    @Valid
    @NotEmpty(message = "Debe incluir al menos un item")
    private List<VentaItemRequestDTO> items;

    @Valid
    @NotEmpty(message = "Debe incluir al menos un pago")
    private List<VentaPagoRequestDTO> pagos;

    private Boolean generarEnvio;

    private TipoEnvio tipoEnvio;

    private String observacionesEnvio;
}
