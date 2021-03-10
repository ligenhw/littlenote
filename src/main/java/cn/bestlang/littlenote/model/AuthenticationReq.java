package cn.bestlang.littlenote.model;

import lombok.Data;

@Data
public class AuthenticationReq {
    private String username;
    private String password;
}
