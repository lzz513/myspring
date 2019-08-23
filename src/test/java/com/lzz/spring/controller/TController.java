package com.lzz.spring.controller;


import com.lzz.spring.Bean.User;
import com.lzz.spring.core.AutoWired;
import com.lzz.spring.db.Param;
import com.lzz.spring.mvc.Controller;
import com.lzz.spring.mvc.RequestMapping;
import com.lzz.spring.service.IService;
import com.lzz.spring.service.TService;

import java.util.List;

@Controller
@RequestMapping("/user")
public class TController {

    @AutoWired
    public TService service;


    @RequestMapping("findAll")
    public String findAll(){
        System.out.println(service.findAll());
        return "findAll";
    }

    @RequestMapping("insertByInfo")
    public String insertByInfo(@Param("name") String name,@Param("age") String age){
        service.insertByInfo(name, Integer.valueOf(age));
        return "insert cg";
    }

    @RequestMapping("insertUser")
    public String insertByUser(@Param("name") String name,@Param("age") String age){
        User user = new User();
        user.setName(name);
        user.setAge(Integer.valueOf(age));
        service.insertUser(user);
        return "insert cg";
    }


}
