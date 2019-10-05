package com.olrox.chat.rest;

import com.olrox.chat.dto.ExceptionDto;
import com.olrox.chat.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.olrox.chat.rest")
public class MyRestControllerAdvice {
    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handleException(UserNotFoundException exc) {
        ExceptionDto response =
                new ExceptionDto(HttpStatus.NOT_FOUND.value(), exc.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handleException(Exception exc) {
        ExceptionDto response =
                new ExceptionDto(HttpStatus.BAD_REQUEST.value(), exc.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
