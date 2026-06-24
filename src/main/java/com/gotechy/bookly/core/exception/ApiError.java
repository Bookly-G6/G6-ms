package com.gotechy.bookly.core.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError(
    LocalDateTime timestamp,
    Integer status,
    String error,
    String code,
    String message,
    String path,
    List<String> details
) {}
