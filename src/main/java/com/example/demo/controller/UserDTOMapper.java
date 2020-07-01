package com.example.demo.controller;

import com.example.demo.bo.UserBO;
import com.example.demo.controller.dto.UserCreateDTO;
import com.example.demo.controller.dto.UserDetailsDTO;
import com.example.demo.controller.dto.UserListDTO;
import com.example.demo.controller.dto.UserModifyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserDTOMapper {
    UserListDTO boToList(UserBO userBO);

    UserDetailsDTO boToDetails(UserBO userBO);

    UserBO createToBO(UserCreateDTO createDTO);

    UserBO modifyToBO(UserModifyDTO modifyDTO);

}
