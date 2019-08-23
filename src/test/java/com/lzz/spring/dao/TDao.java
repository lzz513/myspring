package com.lzz.spring.dao;


import com.lzz.spring.Bean.User;
import com.lzz.spring.db.Insert;
import com.lzz.spring.db.Mapper;
import com.lzz.spring.db.Param;
import com.lzz.spring.db.Select;

import java.util.*;

@Mapper
public interface TDao {


    @Insert("insert into t values(#{name}, #{age})")
    public void insert(User user);

    @Insert("insert into t values(#{name}, #{age})")
    public void insert(@Param("name") String name,@Param("age") int age);

    @Select("select * from t where age")
    public List<User> select();

}
