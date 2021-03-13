package cn.bestlang.littlenote.controller;

import cn.bestlang.littlenote.entity.User;
import cn.bestlang.littlenote.mapper.UserMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/v1/admin")
@RestController
public class UserAdminController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/users")
    public Page<User> queryAllUser(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        Page<User> page = new Page<>(pageNum, pageSize);
        Page<User> userPage = userMapper.selectPage(page, Wrappers.<User>lambdaQuery());

        return userPage;
    }
}
