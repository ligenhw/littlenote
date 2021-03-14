package cn.bestlang.littlenote.mapper;

import cn.bestlang.littlenote.entity.User;
import cn.bestlang.littlenote.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase
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
    void testSelect() {
        List<User> users = userMapper.selectList(null);
        users.forEach(u -> log.info(JsonUtil.toJson(u)));
        assertThat(users).hasSize(1);
    }

    @Test
    void testInsert() {
        userMapper.insert(createUser());
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