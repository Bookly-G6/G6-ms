package com.gotechy.bookly.core.utils;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class JsonAtributosValidator {

    public void validarAtributos(Map<String, Object> atributos) {
        if (atributos == null || atributos.isEmpty()) {
            return;
        }

        if (atributos.size() > 15) {
            throw new IllegalArgumentException(
                "No se pueden enviar más de 15 atributos específicos"
            );
        }

        for (Map.Entry<String, Object> entry : atributos.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (!key.matches("^[a-z0-9_]+$")) {
                throw new IllegalArgumentException(
                    "La clave del atributo '" +
                        key +
                        "' es inválida. Use solo minúsculas y guiones bajos."
                );
            }

            if (
                !(value instanceof String ||
                    value instanceof Number ||
                    value instanceof Boolean)
            ) {
                throw new IllegalArgumentException(
                    "El valor para '" +
                        key +
                        "' debe ser un texto, número o booleano plano. No se permiten objetos anidados."
                );
            }
        }
    }
}
