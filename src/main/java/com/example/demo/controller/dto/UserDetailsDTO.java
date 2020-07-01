package com.example.demo.controller.dto;

import com.example.demo.bo.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailsDTO {

    private Boolean active;
    private String address;
    private String created;
    private Timestamp createdAt;
    private String deleted;
    private Timestamp deletedAt;
    private Boolean deletedFlag;
    private String updated;
    private Timestamp updatedAt;
    private String email;
    private Timestamp lastLogin;
    private String name;
    private Boolean nextLoginChangePwd;
    private Boolean passwordExpired;
    private String phone;
    private String settlementId;
    private Boolean tempPasswordExpired;
    private String userType;
    private String username;
    private String settlementsBySettlementId;
    private Set<UserRole> roles;

}
