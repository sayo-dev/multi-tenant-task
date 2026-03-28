package org.example.multi_tenant_task.exception;

import io.jsonwebtoken.JwtException;
import org.example.multi_tenant_task.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BadCredentialsException.class,
            JwtExpiredException.class,
            UsernameNotFoundException.class})
    public ResponseEntity<ApiResponse<String>> handleInvalidCredentialException(Exception ex) {

        return new ResponseEntity<>(ApiResponse.fail(ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleEntityNotFoundException(EntityNotFoundException ex) {

        return new ResponseEntity<>(ApiResponse.fail(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<String>> handleConflictException(ConflictException ex) {

        return new ResponseEntity<>(ApiResponse.fail(ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(AccessDeniedException ex) {

        return new ResponseEntity<>(ApiResponse.fail(ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        String error = ex.getBindingResult().
                getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).
                findFirst().orElse("Validation failed");

        return new ResponseEntity<>(ApiResponse.fail(error), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleInternalServerException(Exception ex) {

        return new ResponseEntity<>(ApiResponse.fail(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
