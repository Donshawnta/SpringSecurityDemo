package com.example.demo.controller.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserModifyDTO {
    private Boolean active;
    private String address;
    private String email;
    private String name;
    private Boolean nextLoginChangePwd;
    private Boolean passwordExpired;
    private String phone;
    private String settlementId;
    private Boolean tempPasswordExpired;
    private String userType;
    private String settlementsBySettlementId;
    private String updated;
    private Timestamp updatedAt;

}
