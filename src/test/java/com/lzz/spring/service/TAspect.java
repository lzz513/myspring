package com.lzz.spring.service;

import com.lzz.spring.aop.After;
import com.lzz.spring.aop.Aspect;
import com.lzz.spring.aop.Before;
import com.lzz.spring.aop.PointCut;

public class TAspect {

    @Before
    public void before(){
        System.out.println("before...");
    }

    @After
    public void after(){
        System.out.println("after...");
    }

    @PointCut("com.lzz.spring.service.TService.f")
    public void f(){

    }

}
