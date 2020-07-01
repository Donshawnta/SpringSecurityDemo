package com.example.demo.controller.dto;

import lombok.Value;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

@Value
public class ResponseErrorModel {

    private Long timestamp;
    private String errorMessage;

    public ResponseErrorModel(String errorMessage) {
        this.errorMessage = errorMessage;
        timestamp = System.currentTimeMillis();
    }

    @JsonCreator
    public ResponseErrorModel(@JsonProperty("timestamp") Long timestamp, @JsonProperty("errorMessage") String errorMessage) {
        this.timestamp = timestamp;
        this.errorMessage = errorMessage;
    }
}
