package com.interstellar.equipmentmanager.exception.handler;

import com.interstellar.equipmentmanager.exception.ForbiddenActionException;
import com.interstellar.equipmentmanager.exception.ResourceConflictException;
import com.interstellar.equipmentmanager.exception.ResourceNotFoundException;
import com.interstellar.equipmentmanager.model.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return buildErrorResponse("Validation error", HttpStatus.UNPROCESSABLE_ENTITY, ex.getFieldErrors());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return buildErrorResponse(STR."Missing parameter: \{ex.getMessage()}.", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return buildErrorResponse(STR."Message not readable: \{ex.getMessage()}.", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest webRequest) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ForbiddenActionException ex, WebRequest webRequest) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(AuthenticationException ex, WebRequest webRequest) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceConflictException ex, WebRequest webRequest) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtException(Exception ex, WebRequest webRequest) {
        log.error("Exception: {}", ex.getClass().getName());
        log.error("Message: {}", ex.getMessage());
        if (ex.getCause() != null) {
            log.error("Cause: {}", ex.getCause().toString());
        }
        log.error("Stack trace: {}", Arrays.toString(ex.getStackTrace()));

        return buildErrorResponse("Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Object> buildErrorResponse(String message, HttpStatus httpStatus, List<FieldError> fieldErrors) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);

        for (FieldError fieldError : fieldErrors) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    public ResponseEntity<Object> buildErrorResponse(String message, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(
                new ErrorResponse(httpStatus.value(), message)
        );
    }
}
