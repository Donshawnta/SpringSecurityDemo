package com.example.demo.error;

import lombok.Getter;

public class DemoAppException extends RuntimeException {

    @Getter
    private ErrorType errorType;

    public DemoAppException(ErrorType errorType) {
        this.errorType=errorType;
    }


    public DemoAppException(ErrorType errorType,String message) {
        super(message);
        this.errorType=errorType;
    }

    public DemoAppException(ErrorType errorType,Throwable e) {
        super(e);
        this.errorType=errorType;
    }


}
