package com.example.demo.db.entity;

import lombok.Data;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@SQLDelete(sql = "update user set deleted_Flag=true where id=?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted_Flag=false")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean active;
    private String address;
    @Column(nullable = false)
    private Boolean deletedFlag = Boolean.FALSE;
    private String email;
    private String emailToken;
    private Timestamp lastLogin;
    private String name;
    @Column(nullable = false)
    private Boolean nextLoginChangePwd = Boolean.FALSE;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private Boolean passwordExpired = Boolean.FALSE;
    private String phone;
    private String settlementId;
    private String tempPassword;
    @Column(nullable = false)
    private Boolean tempPasswordExpired = Boolean.FALSE;
    private String userType;

    @Column(unique = true, nullable = false)
    private String username;
    private String settlementsBySettlementId;

    private String created;
    private Timestamp createdAt;

    private String deleted;
    private Timestamp deletedAt;

    private String updated;
    private Timestamp updatedAt;

    @OneToMany(targetEntity = Role.class, mappedBy = "user", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();


    private void transfer() {
        username = username == null ? null : username.toLowerCase();
    }

    @PrePersist
    public void onPrePersist() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            setCreated(authentication.getName());
        }
        setCreatedAt(new Timestamp(System.currentTimeMillis()));
        transfer();
    }

    @PreUpdate
    public void onPreUpdate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        setUpdated(authentication.getName());
        setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        transfer();
    }

    @PreRemove
    public void deleteUser() {
        this.deletedFlag = Boolean.TRUE;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        setDeleted(authentication.getName());
        setDeletedAt(new Timestamp(System.currentTimeMillis()));
    }
}
