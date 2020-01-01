package com.example.CustomerAccount.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Builder
@RedisHash

@Setter
public class Customer implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private String login;
    private String password;


}
