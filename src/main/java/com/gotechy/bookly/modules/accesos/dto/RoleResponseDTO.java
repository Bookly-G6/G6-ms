package com.gotechy.bookly.modules.accesos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RoleResponseDTO {

    private Integer idRol;
    private String nombreRol;
}
