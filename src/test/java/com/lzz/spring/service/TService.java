package com.lzz.spring.service;

import com.lzz.spring.Bean.User;
import com.lzz.spring.core.AutoWired;
import com.lzz.spring.dao.TDao;
import com.lzz.spring.mvc.Component;

import java.util.*;

@Component
public class TService implements IService{


    @AutoWired
    private TDao dao;


    public List<User> findAll(){
        return dao.select();
    }

    public void insertByInfo(String name, int age){
        dao.insert(name, age);
    }
    public void insertUser(User user){
        dao.insert(user);
    }
    @Override
    public void f() {

    }
}
