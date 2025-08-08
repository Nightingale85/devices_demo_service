package com.devices.demo.controller.exception.handler;

import com.devices.demo.exception.DeviceInUseException;
import com.devices.demo.exception.DeviceNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DeviceNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleNotFound(DeviceNotFoundException ex) {
    return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(DeviceInUseException.class)
  public ResponseEntity<Map<String, Object>> handleInUse(DeviceInUseException ex) {
    return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
    return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
  }

  private ResponseEntity<Map<String, Object>> buildResponseEntity(HttpStatus status, String message) {

    Map<String, Object> body = new HashMap<>();

    body.put("timestamp", LocalDateTime.now());
    body.put("status", status.value());
    body.put("error", status.getReasonPhrase());
    body.put("message", message);

    return new ResponseEntity<>(body, status);
  }
}
