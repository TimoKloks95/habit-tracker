package com.timokloks.habit_tracker.controllers;

import com.timokloks.habit_tracker.dtos.ErrorDto;
import com.timokloks.habit_tracker.exceptions.HabitNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleUnreadableMessage() {
        return ResponseEntity.badRequest().body(
                new ErrorDto("Invalid request body")
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex
    ) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getFieldErrors().forEach(er -> errors.put(er.getField(), er.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HabitNotFoundException.class)
    public ResponseEntity<ErrorDto> handleHabitNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorDto("Habit not found.")
        );
    }
}
