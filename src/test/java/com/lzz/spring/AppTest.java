package com.lzz.spring;

import static org.junit.Assert.assertTrue;

import com.lzz.spring.Bean.User;
import com.lzz.spring.controller.TController;
import com.lzz.spring.core.AutoWired;
import com.lzz.spring.core.BeanFactory;
import com.lzz.spring.dao.TDao;
import com.lzz.spring.db.Insert;
import com.lzz.spring.db.Param;
import com.lzz.spring.db.Select;
import com.lzz.spring.mvc.Controller;
import com.lzz.spring.service.IService;
import com.lzz.spring.service.TService;
import com.lzz.spring.start.MainApplication;
import com.lzz.spring.utils.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.Test;

import java.awt.List;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.ArrayList;

/**
 * Unit test for simple App.
 */

public class AppTest 
{



    public static void main(String[] args) throws Exception {
        MainApplication.run(AppTest.class);
        System.out.println("master");



    }








}
