package com.example.payment.Service.Impl;

import com.example.payment.Mapper.UsersDao;
import com.example.payment.Service.UserService;
import com.example.payment.pojo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersDao usersDao;

    @Override
    public Users getUser() {
        return usersDao.getUser();
    }
}
