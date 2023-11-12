package com.tshop.entity;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String userId;
    private String username;
    private String password;
    private String active;
    private String tel;
    private String email;
    private String address;
    private String regdate;
}
