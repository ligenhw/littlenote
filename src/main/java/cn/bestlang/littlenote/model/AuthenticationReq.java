package cn.bestlang.littlenote.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthenticationReq {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
