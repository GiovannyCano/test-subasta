package cl.tuxpan.pruebaingreso.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("status", 400);
    body.put("error", "Bad Request");
    List<Map<String, String>> fields = ex.getBindingResult().getFieldErrors().stream()
      .map(err -> Map.of("field", err.getField(), "message", err.getDefaultMessage()))
      .toList();
    body.put("fields", fields);
    return body;
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> handleConstraint(ConstraintViolationException ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("status", 400);
    body.put("error", "Bad Request");
    body.put("violations", ex.getConstraintViolations().stream()
      .map(cv -> Map.of("path", cv.getPropertyPath().toString(), "message", cv.getMessage()))
      .toList());
    return body;
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Map<String, Object>> handleRSE(ResponseStatusException ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("status", ex.getStatusCode().value());
    body.put("error", ex.getReason());
    return ResponseEntity.status(ex.getStatusCode()).body(body);
  }
}
