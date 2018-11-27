package com.staffgenics.training.banking;

import com.staffgenics.training.banking.exception.ClientBadRequestException;
import com.staffgenics.training.banking.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Komponent obsługujący wyjątki.
 */
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ClientBadRequestException.class)
  public ResponseEntity<Object> handleClientBadRequestException(Exception exception, WebRequest request) {
    return handlerAction(exception, request, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Object> handleNotFoundException(Exception exception, WebRequest request) {
    return handlerAction(exception, request, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleAllOtherException(Exception exception, WebRequest request) {
    return handlerAction(exception, request, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<Object> handlerAction(Exception exception, WebRequest request, HttpStatus status){
    return handleExceptionInternal(exception, new ExceptionInformation(exception.getMessage()), new HttpHeaders(), status, request);
  }
}
