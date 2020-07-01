/**
 * = = = = = = = = = = = = = = = = =
 * Copyright (C) 2019 - Sagemcom Energy & Telecom SAS All rights reserved
 * Date 2020-01-28
 * This Copyright notice should not be removed
 * = = = = = = = = = = = = = = = = =
 */
package com.example.demo.db.repository;

import com.example.demo.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
