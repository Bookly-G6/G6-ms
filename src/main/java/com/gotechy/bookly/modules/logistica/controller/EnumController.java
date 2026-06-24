package com.gotechy.bookly.modules.core.controller;

import com.gotechy.bookly.core.dto.EnumValueDTO;
import com.gotechy.bookly.core.enums.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/enums")
public class EnumController {

    @GetMapping("/logistica")
    public List<EnumValueDTO> getEstadosLogistica() {
        return Arrays.stream(EstadoLogistica.values())
            .map(e -> new EnumValueDTO(e.name(), format(e.name())))
            .collect(Collectors.toList());
    }

    @GetMapping("/venta")
    public List<EnumValueDTO> getEstadosVenta() {
        return Arrays.stream(EstadoVenta.values())
            .map(e -> new EnumValueDTO(e.name(), format(e.name())))
            .collect(Collectors.toList());
    }

    @GetMapping("/envio")
    public List<EnumValueDTO> getTiposEnvio() {
        return Arrays.stream(TipoEnvio.values())
            .map(e -> new EnumValueDTO(e.name(), format(e.name())))
            .collect(Collectors.toList());
    }

    private String format(String name) {
        if (name == null || name.isEmpty()) return name;
        return (
            name.substring(0, 1).toUpperCase() +
            name.substring(1).toLowerCase().replace("_", " ")
        );
    }
}
