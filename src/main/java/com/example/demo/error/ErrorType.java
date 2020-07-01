package com.example.demo.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ErrorType {

    NOT_EXISTS(HttpStatus.NOT_FOUND),INTERNAL(HttpStatus.INTERNAL_SERVER_ERROR);

    @Getter
    HttpStatus httpStatus;

    ErrorType(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
