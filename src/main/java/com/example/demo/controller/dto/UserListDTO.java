package com.example.demo.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserListDTO {
    private String username;
    private String userType;
    private String name;
    private String email;
}
