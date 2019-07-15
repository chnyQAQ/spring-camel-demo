package com.dah.camel.controller;

import com.dah.camel.domain.user.User;
import com.dah.camel.domain.user.UserMapper;
import com.dah.camel.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @GetMapping(path="/users/{id}", produces = "application/json")
    public User getById(@PathVariable("id") String id){
        return userMapper.getById(id);
    }

    @GetMapping(path = "/users", produces = "application/json")
    public List<User> getAll(){
        return userMapper.getAll();
    }

    @PutMapping(path="/users/{id}", produces = "application/json")
    public User update(@PathVariable("id") String id, User user){
        int i =0;
        user.setId(id);
        userMapper.update(user);
        return user;
    }

    @PostMapping(path="/users", produces = "application/json")
    public User save(User user){
        user.setId(UUID.randomUUID().toString());
        userMapper.save(user);
        return user;
    }

}
