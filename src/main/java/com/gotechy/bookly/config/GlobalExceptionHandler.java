package com.gotechy.bookly.config;

import com.gotechy.bookly.core.exception.ApiError; // Asegurate de que esta ruta coincida donde creaste ApiError
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Manejo de validaciones de DTOs (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
        MethodArgumentNotValidException ex
    ) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult()
            .getAllErrors()
            .forEach(error -> {
                String nombreCampo = ((FieldError) error).getField();
                String mensajeError = error.getDefaultMessage();
                errores.put(nombreCampo, mensajeError);
            });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    // 2. Manejo de recursos no encontrados (Ej: ID incorrecto)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(
        EntityNotFoundException ex,
        WebRequest request
    ) {
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 3. Manejo genérico "Atrapa todo"
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(
        Exception ex,
        WebRequest request
    ) {
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "Ocurrió un error inesperado en el servidor.",
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(
        IllegalArgumentException ex,
        WebRequest request
    ) {
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            ex.getMessage(), // Acá viaja tu mensaje de error personalizado
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
