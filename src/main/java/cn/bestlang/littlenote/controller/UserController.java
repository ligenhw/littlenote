package cn.bestlang.littlenote.controller;

import cn.bestlang.littlenote.context.UserContext;
import cn.bestlang.littlenote.entity.User;
import cn.bestlang.littlenote.mapper.UserMapper;
import cn.bestlang.littlenote.model.SignUpReq;
import cn.bestlang.littlenote.security.DbUserDetailsManager;
import cn.bestlang.littlenote.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/v1")
@RestController
public class UserController {

    @Autowired
    private DbUserDetailsManager dbUserDetailsManager;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/user")
    public User userInfo() {
        User user = userMapper.selectById(UserContext.getId());
        user.setPassword(null);
        return user;
    }

    // TODO: 使用 spring security filter 实现
    @PostMapping("/signup")
    public void signUp(@Validated @RequestBody SignUpReq signUpReq) {
        log.info("signup", JsonUtil.toJson(signUpReq));
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(signUpReq.getUsername())
                .password(signUpReq.getPassword())
                .roles("USER") // 注册用户的默认角色
                .build();

        dbUserDetailsManager.createUser(userDetails);
    }
}
