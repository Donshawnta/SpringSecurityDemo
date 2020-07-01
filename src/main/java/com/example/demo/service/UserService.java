package com.example.demo.service;

import com.example.demo.error.DemoAppException;
import com.example.demo.bo.UserBO;
import com.example.demo.db.entity.User;
import com.example.demo.db.repository.UserRepository;
import com.example.demo.error.ErrorType;
import com.example.demo.util.LangUtils;
import com.example.demo.util.NullAwareBeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserEntityMapper userEntityMapper;

    @Autowired
    private UserRepository userRepository;

    public void add(UserBO userBo) {
        User user = userEntityMapper.boToEntity(userBo);
        user.getRoles().forEach(role -> role.setUser(user));
        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(passwordEncoder.encode(userBo.getPassword()));
        userRepository.save(user);
    }

    public void set(UserBO userBo) {
        User newUser = userEntityMapper.boToEntity(userBo);
        User old = getUser(userBo.getUsername());
        LangUtils.sneakyThrows(() -> new NullAwareBeanUtilsBean().copyProperties(old, newUser));
        if (StringUtils.hasText(userBo.getPassword())) {
            old.setPassword(passwordEncoder.encode(userBo.getPassword()));
        }

        old.getRoles().forEach(role -> role.setUser(old));
        old.setUsername(old.getUsername().toLowerCase());
        userRepository.save(old);
    }

    public void delete(String userName) {
        User old = getUser(userName);
        userRepository.delete(old);
    }

    public UserBO get(String userName) {
        User user = getUser(userName);
        return userEntityMapper.entityToBO(user);
    }

    public List<UserBO> list() {
        return userRepository.findAll().stream().map(user -> userEntityMapper.entityToBO(user)).sorted(Comparator.comparing(UserBO::getUsername)).collect(Collectors.toList());
    }


    private User getUser(String userName) {
        return userRepository.findByUsername(userName).orElseThrow(() -> new DemoAppException(ErrorType.NOT_EXISTS,"No User:" + userName));
    }


}
