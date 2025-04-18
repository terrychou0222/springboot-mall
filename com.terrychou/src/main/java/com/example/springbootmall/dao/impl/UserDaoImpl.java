package com.example.springbootmall.dao.impl;

import com.example.springbootmall.dao.UserDao;
import com.example.springbootmall.dto.UserRegisterRequest;
import com.example.springbootmall.model.User;
import com.example.springbootmall.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class UserDaoImpl implements UserDao {


    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    //查詢 getuserby id
    @Override
    public User getUserById(Integer userId) {

        String sql="SELECT user_id,email,password,created_date,last_modified_date "
                + "FROM user WHERE user_id= :user_id";
        Map<String,Object> map =new HashMap<>();
        map.put("user_id",userId);


        List<User> userList=namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if(userList.size()>0){
            return userList.get(0);
        }
        else
            return  null;
    }


    // 新增帳號到DB
    @Override
    public Integer creatUser(UserRegisterRequest userRegisterRequest) {

        String sql="INSERT INTO user(email,password,created_date,last_modified_date)" +
                "VALUES(:email, :password ,:SSS,:CCC)";

        Map<String,Object>map =new HashMap<>();
        map.put("email",userRegisterRequest.getEmail());
        map.put("password",userRegisterRequest.getPassword());

        Date now=new Date();
        map.put("SSS",now);
        map.put("CCC",now);


        KeyHolder keyHolder=new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql,new MapSqlParameterSource(map),keyHolder);

        int userId=keyHolder.getKey().intValue();

        return userId;

    }
    //實作 使用EMAIL找USER
    @Override
    public User getUserByEmail(String email) {

        String sql ="SELECT user_id,email,password,created_date,last_modified_date "+
                "FROM user WHERE email= :email";

        Map<String,Object>map =new HashMap<>();

        map.put("email",email);

        List<User> userList=namedParameterJdbcTemplate.query(sql,map, new UserRowMapper());


        if (userList.size()>0){
            return  userList.get(0);
        }
        else
            return null;
    }


}
