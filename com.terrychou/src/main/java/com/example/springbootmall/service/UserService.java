package com.example.springbootmall.service;

import com.example.springbootmall.dto.UserRegisterRequest;
import com.example.springbootmall.dto.UsersLoginRequest;
import com.example.springbootmall.model.User;

public interface UserService {



    Integer register(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);

    User login(UsersLoginRequest userLoginRequest);

}
