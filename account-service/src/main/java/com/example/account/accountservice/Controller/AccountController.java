package com.example.account.accountservice.Controller;

import com.example.account.accountservice.rpc.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.account.accountservice.pojo.User;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountController {
    final static Map<Integer, User> userMap = new HashMap() {{
        put(1, new User(1, "张三"));
        put(2, new User(2, "李四"));
        put(3, new User(3, "王五"));
    }
    };

    @Autowired
    BalanceService balanceService;

    @RequestMapping("/acc/user")
    public User getUser(@RequestParam Integer id) {
        if(id != null && userMap.containsKey(id)) {
            User user = userMap.get(id);
            user.setBalance(balanceService.getBalance(id));
            return user;
        }
        return new User(0, "");
    }

}
