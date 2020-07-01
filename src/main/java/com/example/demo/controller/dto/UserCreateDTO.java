package com.example.demo.controller.dto;

import com.example.demo.bo.UserRole;
import lombok.Data;

import java.util.Set;

@Data
public class UserCreateDTO {

    private Boolean active;
    private String address;
    private String email;
    private String name;
    private Boolean nextLoginChangePwd;
    private String phone;
    private String settlementId;
    private String password;
    private String userType;
    private String username;
    private String settlementsBySettlementId;
    private Set<UserRole> roles;

}
