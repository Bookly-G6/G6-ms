package com.gotechy.bookly.config;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.gotechy.bookly.core.exception.ApiError;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 - Validaciones de DTOs (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(
        MethodArgumentNotValidException ex, WebRequest request
    ) {
        List<String> details = ex.getBindingResult().getAllErrors().stream()
            .map(err -> {
                String field = (err instanceof FieldError fe) ? fe.getField() : err.getObjectName();
                return field + ": " + err.getDefaultMessage();
            })
            .collect(Collectors.toList());

        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            "VALIDATION_ERROR",
            "Los datos enviados contienen errores de validación.",
            extractPath(request),
            details
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 400 - Argumento inválido de negocio
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(
        IllegalArgumentException ex, WebRequest request
    ) {
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            "VALIDATION_ERROR",
            ex.getMessage(),
            extractPath(request),
            null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 400 - Body JSON no legible
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex, WebRequest request
    ) {
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            "VALIDATION_ERROR",
            "El cuerpo de la solicitud no es un JSON válido.",
            extractPath(request),
            null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 401 - Token expirado
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiError> handleExpiredJwt(
        ExpiredJwtException ex, WebRequest request
    ) {
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.UNAUTHORIZED.value(),
            "Unauthorized",
            "TOKEN_EXPIRED",
            "El token ha expirado. Por favor, inicia sesión nuevamente.",
            extractPath(request),
            null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // 401 - Token inválido
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(
        JwtException ex, WebRequest request
    ) {
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.UNAUTHORIZED.value(),
            "Unauthorized",
            "TOKEN_INVALID",
            "El token proporcionado no es válido.",
            extractPath(request),
            null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // 401 - Credenciales incorrectas
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(
        BadCredentialsException ex, WebRequest request
    ) {
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.UNAUTHORIZED.value(),
            "Unauthorized",
            "TOKEN_INVALID",
            "Email o contraseña incorrectos.",
            extractPath(request),
            null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // 401 - Sin autenticación (Spring Security)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(
        AuthenticationException ex, WebRequest request
    ) {
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.UNAUTHORIZED.value(),
            "Unauthorized",
            "AUTH_REQUIRED",
            "Debes autenticarte para acceder a este recurso.",
            extractPath(request),
            null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // 403 - Sin permisos
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(
        AccessDeniedException ex, WebRequest request
    ) {
        String message = ex.getMessage() != null && !ex.getMessage().isBlank()
            ? ex.getMessage()
            : "No tienes permisos para acceder a este recurso.";

        String code = message.toLowerCase().contains("propietario") || message.toLowerCase().contains("permiso para ver")
            ? "RESOURCE_NOT_OWNED"
            : "ACCESS_DENIED";

        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.FORBIDDEN.value(),
            "Forbidden",
            code,
            message,
            extractPath(request),
            List.of("Se requiere rol ADMIN o ser el propietario del recurso.")
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    // 404 - Entidad no encontrada
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(
        EntityNotFoundException ex, WebRequest request
    ) {
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            "RESOURCE_NOT_FOUND",
            ex.getMessage(),
            extractPath(request),
            null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // 409 - Conflicto de datos
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(
        DataIntegrityViolationException ex, WebRequest request
    ) {
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            "Conflict",
            "DATA_CONFLICT",
            "No se pudo completar la operación por un conflicto en los datos.",
            extractPath(request),
            null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // 500 - Error genérico
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(
        Exception ex, WebRequest request
    ) {
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "INTERNAL_ERROR",
            "Ocurrió un error inesperado. Por favor, intenta más tarde.",
            extractPath(request),
            null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}

