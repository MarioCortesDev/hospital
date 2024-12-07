package com.hospital.hospital.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppointmentValidationException.class)
    public ResponseEntity<String> handleAppointmentValidation(AppointmentValidationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
