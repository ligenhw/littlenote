package cn.bestlang.littlenote.security;

import cn.bestlang.littlenote.entity.Authorities;
import cn.bestlang.littlenote.entity.User;
import cn.bestlang.littlenote.mapper.AuthoritiesMapper;
import cn.bestlang.littlenote.mapper.UserMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * UserDetailsManager 继承自 UserDetailsService ， 并加入了 用户管理接口
 */
public class DbUserDetailsManager extends DbUserDetailsService implements UserDetailsManager {



    public DbUserDetailsManager(UserMapper userMapper,
                                AuthoritiesMapper authoritiesMapper,
                                PasswordEncoder passwordEncoder) {
        super(userMapper, authoritiesMapper, passwordEncoder);
    }

    @Transactional
    @Override
    public void createUser(final UserDetails userDetails) {
        validateUserDetails(userDetails);
        // 与 JdbcUserDetailsManager 区别， JdbcUserDetailsManager 中使用 UserDetails 的 password字段是加密后的
        String encodePassword = passwordEncoder.encode(userDetails.getPassword());

        User user = new User();
        user.setUsername(userDetails.getUsername());
        user.setPassword(encodePassword);
        userMapper.insert(user);

        if (getEnableAuthorities()) {
            insertUserAuthorities(userDetails);
        }
    }

    @Override
    public void updateUser(UserDetails userDetails) {

    }

    @Override
    public void deleteUser(String s) {

    }

    @Override
    public void changePassword(String s, String s1) {

    }

    @Override
    public boolean userExists(String s) {
        return false;
    }

    private void insertUserAuthorities(UserDetails user) {
        for (GrantedAuthority auth : user.getAuthorities()) {
            Authorities authorities = new Authorities();
            authorities.setUsername(user.getUsername());
            authorities.setAuthority(auth.getAuthority());
            authoritiesMapper.insert(authorities);
        }
    }

    private void validateUserDetails(UserDetails user) {
        Assert.hasText(user.getUsername(), "Username may not be empty or null");
        validateAuthorities(user.getAuthorities());
    }

    private void validateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Authorities list must not be null");
        for (GrantedAuthority authority : authorities) {
            Assert.notNull(authority, "Authorities list contains a null entry");
            Assert.hasText(authority.getAuthority(), "getAuthority() method must return a non-empty string");
        }
    }
}
