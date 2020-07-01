package com.example.demo.db;

import com.example.demo.bo.UserRole;
import com.example.demo.db.entity.Role;
import com.example.demo.db.entity.User;
import com.example.demo.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class DefaultDataLoader {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostConstruct
    public void init() {
        User adminUser = new User();
        adminUser.setActive(true);
        adminUser.setName("Admin");
        adminUser.setPassword(passwordEncoder.encode("admin"));
        adminUser.setUsername("admin");
        adminUser.setUserType("admin");
        Role adminRole = new Role();
        adminRole.setRole(UserRole.ADMIN);
        adminRole.setUser(adminUser);
        Role publicRole = new Role();
        publicRole.setRole(UserRole.PUBLIC);
        publicRole.setUser(adminUser);
        adminUser.setRoles(Arrays.asList(adminRole, publicRole).stream().collect(Collectors.toSet()));
        userRepository.save(adminUser);
    }


    public void reInit() {
        userRepository.deleteAll();
        init();
    }

}
