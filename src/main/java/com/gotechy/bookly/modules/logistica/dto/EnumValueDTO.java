package com.gotechy.bookly.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EnumValueDTO {

    private String value;
    private String label;
}
