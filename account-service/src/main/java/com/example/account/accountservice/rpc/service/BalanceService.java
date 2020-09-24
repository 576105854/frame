package com.example.account.accountservice.rpc.service;

import com.example.account.accountservice.rpc.impl.BalanceServiceFallback;
import com.example.common.common.pojo.Balance;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment-service", fallback = BalanceServiceFallback.class)
public interface BalanceService {

    @RequestMapping(value = "/pay/balance", method = RequestMethod.GET)
    Balance getBalance(@RequestParam("id") Integer id);
}
