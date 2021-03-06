package com.example.account.pojo;

import lombok.Data;
import com.example.common.pojo.Balance;

@Data
public class User {
    private int id;
    private String name;
    private Balance balance;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public User() {

    }
}
