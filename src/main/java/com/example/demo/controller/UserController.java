package com.example.demo.controller;

import com.example.demo.bo.UserBO;
import com.example.demo.controller.dto.UserCreateDTO;
import com.example.demo.controller.dto.UserDetailsDTO;
import com.example.demo.controller.dto.UserListDTO;
import com.example.demo.controller.dto.UserModifyDTO;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/demo")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDTOMapper userDTOMapper;

    @Secured("ROLE_PUBLIC")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<UserListDTO> getUser() {
        return userService.list().stream().map(userBO1 -> userDTOMapper.boToList(userBO1)).collect(Collectors.toList());
    }

    @Secured("ROLE_PUBLIC")
    @RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
    public UserDetailsDTO getUser(@PathVariable() String username) {
        UserBO userBO = userService.get(username);
        UserDetailsDTO userDetailsDTO = userDTOMapper.boToDetails(userBO);
        return userDetailsDTO;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/user/{username}", method = RequestMethod.PUT)
    public void setUser(@RequestBody UserModifyDTO modifyDTO, @PathVariable() String username) {
        UserBO userBO = userDTOMapper.modifyToBO(modifyDTO);
        userBO.setUsername(username);
        userService.set(userBO);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/user/{username}", method = RequestMethod.DELETE)
    public void removeUser(@PathVariable() String username) {
        userService.delete(username);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public void addUser(@RequestBody UserCreateDTO createDTO) {
        UserBO userBO = userDTOMapper.createToBO(createDTO);
        userService.add(userBO);
    }

}
