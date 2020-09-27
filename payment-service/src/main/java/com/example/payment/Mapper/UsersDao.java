package com.example.payment.Mapper;

import com.example.payment.pojo.Users;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersDao {
    Users getUser();
}
