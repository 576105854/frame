package com.example.account.accountservice.rpc.impl;

import com.example.account.accountservice.rpc.service.BalanceService;
import com.example.common.common.pojo.Balance;
import org.springframework.stereotype.Component;

@Component
public class BalanceServiceFallback implements BalanceService {
    @Override
    public Balance getBalance(Integer id) {
        return new Balance(0,0,0,"降级");
    }
}
