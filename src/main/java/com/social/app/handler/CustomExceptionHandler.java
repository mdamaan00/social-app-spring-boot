package com.social.app.handler;

import com.social.app.dto.ApiResponse;
import com.social.app.util.StatusMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse> handleCustomExceptions(
      HttpServletRequest request, Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.builder().status(StatusMessage.ERROR).message(ex.getMessage()).build());
  }
}
