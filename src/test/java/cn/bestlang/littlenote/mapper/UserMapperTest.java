package cn.bestlang.littlenote.mapper;

import cn.bestlang.littlenote.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    User createUser() {
        User user = new User();
        user.setUsername("gen");
        user.setPassword("pass");
        user.setNickname("xx");
        user.setAvatarUrl("https://www.bestlang.cn");

        return user;
    }

    @Test
    void testInsert() {
        userMapper.insert(createUser());
    }

    @Test
    void testSelect() {
        List<User> users = userMapper.selectList(null);

        users.forEach(u -> log.info("" + u));
    }

    @Test
    void testUpdate() {
        User user = new User();
        user.setId(2);
        user.setPassword("pass2");
        userMapper.updateById(user);
    }

    @Test
    void testDelete() {
        int d = userMapper.deleteById(1);
        log.info("" + d);
    }
}