package com.develop.app.ws.exception;

import com.develop.app.ws.ui.model.response.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@ControllerAdvice
public class AppExceptionsHandler {
    @ExceptionHandler(value = UserServiceException.class)
    public ResponseEntity<Object> handleUserServiceException(UserServiceException exception,
                                                             WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(Instant.now(), exception.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}