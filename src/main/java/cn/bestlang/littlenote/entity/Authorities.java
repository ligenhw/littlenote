package cn.bestlang.littlenote.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Authorities {

    private String username;

    private String authority;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
