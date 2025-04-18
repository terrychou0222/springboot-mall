package com.example.springbootmall.controller;


import com.example.springbootmall.dto.UserRegisterRequest;
import com.example.springbootmall.dto.UsersLoginRequest;
import com.example.springbootmall.model.User;
import com.example.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {

        Integer userId=userService.register(userRegisterRequest);

        User user=userService.getUserById(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);


    }



    //登入
    @PostMapping("/users/login")
    public ResponseEntity<User> login(@RequestBody @Valid UsersLoginRequest userLoginRequest) {

      User user= userService.login(userLoginRequest);

      return ResponseEntity.status(HttpStatus.OK).body(user);


    }


}
