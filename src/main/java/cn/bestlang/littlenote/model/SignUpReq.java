package cn.bestlang.littlenote.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SignUpReq {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
