package com.gotechy.bookly.config;

import com.gotechy.bookly.core.exception.ApiError;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
            "RESOURCE_NOT_FOUND",

            ex.getMessage(),
            request.getDescription(false).replace("uri=", ""),
            List.of()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 3. Manejo genérico de errores no controlados
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException ex,
        WebRequest request
    ) {
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            "INVALID_JSON",

            "El cuerpo de la solicitud no es un JSON válido.",
            request.getDescription(false).replace("uri=", ""),
            List.of(ex.getMessage())
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(
        DataIntegrityViolationException ex,
        WebRequest request
    ) {
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            "Conflict",
            "DATA_CONFLICT",
            "No se pudo completar la operación por un conflicto en los datos.",
            request.getDescription(false).replace("uri=", ""),
            List.of()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentialsException(
        BadCredentialsException ex,
        WebRequest request
    ) {
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.UNAUTHORIZED.value(),
            "Unauthorized",
            "BAD_CREDENTIALS",
            "Email o contraseña incorrectos.",
            request.getDescription(false).replace("uri=", ""),
            List.of()
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(
        AccessDeniedException ex,
        WebRequest request
    ) {
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.FORBIDDEN.value(),
            "Forbidden",
            "ACCESS_DENIED",
            ex.getMessage() == null || ex.getMessage().isBlank()
                ? "No tienes permisos para acceder a este recurso."
                : ex.getMessage(),
            request.getDescription(false).replace("uri=", ""),
            java.util.List.of(
                "Se requiere rol ADMIN o perfil de cliente según el recurso."
            )
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(
        Exception ex,
        WebRequest request
    ) {
        ex.printStackTrace();
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "INTERNAL_ERROR",
            "Ocurrió un error inesperado.",
            request.getDescription(false).replace("uri=", ""),
            null
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
            "BUSINESS_RULE_ERROR",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", ""),
            null
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(
        MethodArgumentTypeMismatchException ex,
        WebRequest request
    ) {
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            "INVALID_FORMAT",
            "El formato del ID proporcionado no es válido. Se esperaba un UUID.",
            request.getDescription(false).replace("uri=", ""),
            null
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalState(
        IllegalStateException ex,
        WebRequest request
    ) {
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            "Conflict",
            "ILLEGAL_STATE",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", ""),
            null
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
