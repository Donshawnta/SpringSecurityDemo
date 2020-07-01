package com.example.demo.bo;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;

@Data
public class UserBO {

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
    private String emailToken;
    private Timestamp lastLogin;
    private String name;
    private Boolean nextLoginChangePwd;
    private String password;
    private Boolean passwordExpired;
    private String phone;
    private String settlementId;
    private String tempPassword;
    private Boolean tempPasswordExpired;
    private String userType;
    private String username;
    private String settlementsBySettlementId;
    private Set<UserRole> roles;

}
