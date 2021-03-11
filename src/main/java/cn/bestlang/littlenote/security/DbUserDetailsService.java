package cn.bestlang.littlenote.security;

import cn.bestlang.littlenote.entity.User;
import cn.bestlang.littlenote.mapper.UserMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
public class DbUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public DbUserDetailsService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetails user = loadUsersByUsername(username);

        return user;
    }

    private UserDetails loadUsersByUsername(String username) {
        List<User> users = userMapper.selectList(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        if (users.size() == 0) {
            log.debug("Query returned no results for user '" + username + "'");
            throw new UsernameNotFoundException("Username " + username + " not found");
        }

        User user = users.get(0);

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true, AuthorityUtils.NO_AUTHORITIES);
    }

    public void signUp(String username, String password) {
        String encodePassword = passwordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodePassword);
        userMapper.insert(user);
    }
}
