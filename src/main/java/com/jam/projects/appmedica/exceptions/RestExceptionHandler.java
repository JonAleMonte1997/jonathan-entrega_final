package com.jam.projects.appmedica.exceptions;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.SizeLimitExceededException;
import javax.persistence.EntityNotFoundException;
import java.util.Date;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {

        return buildResponseEntity(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {

        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleMaxSizeListExceededException(MaxSizeListExceededException ex) {

        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {

        return buildResponseEntity(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleException(Exception ex) {

        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    private ResponseEntity<ErrorResponse> buildResponseEntity(HttpStatus status, Exception ex) {

        return new ResponseEntity<>(new ErrorResponse(status.value(), ex.getMessage(), new Date()), status);
    }
}
