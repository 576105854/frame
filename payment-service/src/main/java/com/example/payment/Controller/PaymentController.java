package com.example.payment.Controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.common.pojo.Balance;
import com.example.payment.Service.UserService;
import com.example.payment.Utils.redis.RedisUtils;
import com.example.payment.pojo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RefreshScope
public class PaymentController {
    @Value("${sleep:0}")
    private int sleep;

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private UserService userService;

    final static Map<Integer, Balance> balanceMap = new HashMap() {
        {
            put(1, new Balance(1, 10, 1000));
            put(2, new Balance(2, 0, 10000));
            put(3, new Balance(3, 100, 0));
        }
    };

    @RequestMapping("/pay/balance")
    @SentinelResource(value = "protected-resource", blockHandler = "handleBlock")
    public Balance getBalance(Integer id) {

        boolean hasKey = redisUtils.exists(id.toString());
        if(hasKey){
            //获取缓存
            Object object =  redisUtils.get(id.toString());
            System.out.println("从缓存获取的数据"+ object.toString());
        }else{
            Users user = userService.getUser();
            //数据插入缓存（set中的参数含义：key值，user对象，缓存存在时间10（long类型），时间单位）
            redisUtils.set(id.toString(),"测试",10L, TimeUnit.MINUTES);
        }
        System.out.println("request: /pay/balance?id=" + id + ", sleep: " + sleep);
        if (sleep > 0) {
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (id != null && balanceMap.containsKey(id)) {
            return balanceMap.get(id);
        }
        return new Balance(0, 0, 0);
    }

    public Balance handleBlock(Integer id, BlockException e) {
        return new Balance(0, 0, 0, "限流");
    }

}
