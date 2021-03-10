package cn.bestlang.littlenote.controller;

import cn.bestlang.littlenote.model.SignUpReq;
import cn.bestlang.littlenote.security.DbUserDetailsService;
import cn.bestlang.littlenote.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/v1")
@RestController
public class UserController {

    @Autowired
    private DbUserDetailsService dbUserDetailsService;

    @GetMapping("/user")
    public String userInfo() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();

        return username;
    }

    @PostMapping("/signup")
    public String signUp(@RequestBody SignUpReq signUpReq) {
        log.info("signup", JsonUtil.toJson(signUpReq));
        dbUserDetailsService.signUp(signUpReq.getUsername(), signUpReq.getPassword());
        return "ok";
    }
}
