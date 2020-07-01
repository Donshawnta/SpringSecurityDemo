package com.example.demo.controller;

import com.example.demo.controller.dto.ResponseErrorModel;
import com.example.demo.error.DemoAppException;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


@ControllerAdvice
public class RestControllerAdvice implements ResponseBodyAdvice<ResponseErrorModel> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return ResponseErrorModel.class.isAssignableFrom(methodParameter.getParameterType());
    }

    @Override
    public ResponseErrorModel beforeBodyWrite(ResponseErrorModel responseErrorModel, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        return responseErrorModel;
    }

    @ExceptionHandler(DemoAppException.class)
    public ResponseEntity<ResponseErrorModel> handleException(DemoAppException ex) {
        ResponseErrorModel responseErrorModel = new ResponseErrorModel(ex.getMessage());
        return new ResponseEntity<>(responseErrorModel, ex.getErrorType().getHttpStatus());
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseErrorModel> handleException(AccessDeniedException ex) {
        ResponseErrorModel responseErrorModel = new ResponseErrorModel(ex.getMessage());
        return new ResponseEntity<>(responseErrorModel, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ResponseErrorModel> handleException(NullPointerException ex) {
        ResponseErrorModel responseErrorModel = new ResponseErrorModel(ex.getMessage());
        return new ResponseEntity<>(responseErrorModel, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseErrorModel> handleException(DataIntegrityViolationException ex) {
        ResponseErrorModel responseErrorModel = new ResponseErrorModel(ex.getMessage());
        return new ResponseEntity<>(responseErrorModel, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseErrorModel> handleException(Exception ex) {
        ResponseErrorModel responseErrorModel = new ResponseErrorModel(ex.getMessage());
        return new ResponseEntity<>(responseErrorModel, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
