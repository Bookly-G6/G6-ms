package com.gotechy.bookly.core.enums;

public enum EstadoLogistica {
    PENDIENTE,
    PREPARANDO,
    LISTO_PARA_RETIRO, // (Solo para retiro)
    DESPACHADO, // (Solo para correo)
    ENTREGADO,
    CANCELADO,
}
