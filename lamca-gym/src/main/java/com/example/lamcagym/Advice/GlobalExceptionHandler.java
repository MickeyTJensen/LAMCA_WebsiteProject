package com.example.lamcagym.Advice;

// Importerar nödvändiga bibliotek och klasser
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.http.ResponseEntity;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// Klass som fungerar som global exception handler för hela applikationen
@ControllerAdvice
public class GlobalExceptionHandler {

    // Skapar en logger för att logga information och fel
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Dataklass för att strukturera felmeddelanden
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiError {
        private Date timestamp;  // Tidsstämpel för när felet inträffade
        private HttpStatus status;  // HTTP-statuskoden för svaret
        private String message;  // Användarvänligt felmeddelande
        private Map<String, String> errors;  // Detaljerade fel för specifika fält
        private String path;  // Sökvägen till endpointen där felet inträffade
    }

    // Metod för att bygga ett felresponsobjekt
    private ResponseEntity<ApiError> buildErrorResponse(Exception e, HttpStatus status, String path) {
        Map<String, String> errorMap = new HashMap<>();
        String userFriendlyMessage = "An unexpected error occurred. Please try again later.";

        if (e instanceof MethodArgumentNotValidException) {
            userFriendlyMessage = "Validation errors occurred.";
            ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errorMap.put(fieldName, errorMessage);
            });
        } else if (e instanceof ConstraintViolationException) {
            userFriendlyMessage = "Validation error in your request.";
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) e).getConstraintViolations();
            constraintViolations.forEach(violation -> {
                String fieldName = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                errorMap.put(fieldName, message);
            });
        }

        ApiError apiError = new ApiError(new Date(), status, userFriendlyMessage, errorMap, path);
        logError(e, apiError);
        return ResponseEntity.status(status).body(apiError);
    }

    // Metod för att logga detaljer om felet
    private void logError(Exception e, ApiError apiError) {
        log.error("An error occurred: {}, Status: {}, Path: {}, Details: {}", e.toString(), apiError.getStatus(), apiError.getPath(), apiError.getErrors());
    }

    // Exception handlers som fångar och hanterar olika typer av undantag
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllExceptions(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI());
    }
}



