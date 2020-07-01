package com.example.demo.service;

import com.example.demo.bo.UserBO;
import com.example.demo.bo.UserRole;
import com.example.demo.db.entity.Role;
import com.example.demo.db.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserEntityMapper {

    UserBO entityToBO(User user);

    default String entityToString(User user) {
        return user.getUsername();
    }


    User boToEntity(UserBO userBO);

    default Role enumToRole(UserRole userRole) {
        Role role = new Role();
        role.setRole(userRole);
        return role;
    }

    default UserRole roleToEnum(Role role) {
        return role.getRole();
    }

}
