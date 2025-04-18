package com.example.springbootmall.service.impl;

import com.example.springbootmall.dao.UserDao;
import com.example.springbootmall.dto.UserRegisterRequest;
import com.example.springbootmall.dto.UsersLoginRequest;
import com.example.springbootmall.model.User;
import com.example.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {


    private  final  static Logger log= LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    //獲取user by id
    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    //註冊
    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        //檢查email
        User user=userDao.getUserByEmail(userRegisterRequest.getEmail());

        if (user !=null){
            log.warn("該email{}已經被註冊",userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 使用MD5 生成密碼的雜湊直
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);
        System.out.println(userRegisterRequest.getPassword());


        //註冊帳號
        return  userDao.creatUser(userRegisterRequest);
    }



    //登入
    @Override
    public User login(UsersLoginRequest userLoginRequest) {
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());


        //檢查user是否存在
        if (user == null) {
            log.warn("該{}尚未註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        //使用MD5生成密碼雜湊直

        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());


        //比較密碼
        if (user.getPassword().equals(hashedPassword)) {
            return user;
        }
        else {
            log.warn("該{} 密碼錯誤", user.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        }


    }









}
