package com.gotechy.bookly.modules.ventas.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id_sucursal")
    private Integer idSucursal;

    @Column(name = "id_producto")
    private UUID idProducto;
}
