package com.gotechy.bookly.core.exception;

import java.time.LocalDateTime;

public record ApiError(
    LocalDateTime timestamp,
    Integer status,
    String error,
    String message,
    String path
) {}
