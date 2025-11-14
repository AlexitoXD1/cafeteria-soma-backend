package com.cafeteriasoma.app.exception;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.toList());

        String message = details.isEmpty() ? "Solicitud inválida." : details.getFirst();
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, message, details, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        List<String> details = ex.getConstraintViolations().stream()
                .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                .collect(Collectors.toList());

        String message = details.isEmpty() ? "Parámetros inválidos." : details.getFirst();
        return buildResponse(HttpStatus.BAD_REQUEST, message, details, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud no es válido.", Collections.emptyList(), request);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParameter(MissingServletRequestParameterException ex, HttpServletRequest request) {
        String message = String.format("El parámetro '%s' es obligatorio.", ex.getParameterName());
        return buildResponse(HttpStatus.BAD_REQUEST, message, Collections.emptyList(), request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = String.format("El parámetro '%s' tiene un formato inválido.", ex.getName());
        return buildResponse(HttpStatus.BAD_REQUEST, message, Collections.emptyList(), request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), Collections.emptyList(), request);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiError> handleResourceConflict(ResourceConflictException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), Collections.emptyList(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado.", Collections.emptyList(), request);
    }

    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String message, List<String> details, HttpServletRequest request) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                details
        );
        return ResponseEntity.status(status).body(error);
    }

    private String formatFieldError(FieldError error) {
        return String.format("%s: %s", error.getField(), error.getDefaultMessage());
    }

    public record ApiError(
            LocalDateTime timestamp,
            int status,
            String error,
            String message,
            String path,
            List<String> details
    ) { }
}
