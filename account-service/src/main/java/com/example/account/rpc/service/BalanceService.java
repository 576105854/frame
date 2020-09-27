package com.example.account.rpc.service;

import com.example.account.rpc.config.FeignConfiguration;
import com.example.account.rpc.impl.BalanceServiceFallback;
import com.example.common.pojo.Balance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@FeignClient(name = "payment-service", fallback = BalanceServiceFallback.class, configuration = FeignConfiguration.class)
public interface BalanceService {

    @RequestMapping(value = "/pay/balance", method = RequestMethod.GET)
    Balance getBalance(@RequestParam("id") Integer id);
}
