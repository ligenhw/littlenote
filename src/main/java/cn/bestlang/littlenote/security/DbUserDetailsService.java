package cn.bestlang.littlenote.security;

import cn.bestlang.littlenote.entity.Authorities;
import cn.bestlang.littlenote.entity.User;
import cn.bestlang.littlenote.mapper.AuthoritiesMapper;
import cn.bestlang.littlenote.mapper.UserMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * AuthenticationProvider 使用 UserDetailsService 认证
 *
 * 与 JdbcDaoImpl 区别
 * 用户表结构不同
 * 使用mybatis代替jdbc
 */
@Slf4j
public class DbUserDetailsService implements UserDetailsService {

    protected final UserMapper userMapper;
    protected final AuthoritiesMapper authoritiesMapper;

    protected final PasswordEncoder passwordEncoder;

    private boolean enableAuthorities = true;
    private String rolePrefix = "";

    public DbUserDetailsService(UserMapper userMapper,
                                AuthoritiesMapper authoritiesMapper,
                                PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.authoritiesMapper = authoritiesMapper;
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
        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true, AuthorityUtils.NO_AUTHORITIES);

        Set<GrantedAuthority> dbAuthsSet = new HashSet<>();
        if (this.enableAuthorities) {
            dbAuthsSet.addAll(loadUserAuthorities(userDetails.getUsername()));
        }

        List<GrantedAuthority> dbAuths = new ArrayList<>(dbAuthsSet);
        return createUserDetails(userDetails, user.getId(), dbAuths);
    }

    /**
     * 返回自定义 RestUser 携带userId
     * @param userFromUserQuery
     * @param combinedAuthorities
     * @return
     */
    protected UserDetails createUserDetails(UserDetails userFromUserQuery,
                                            Integer userId,
                                            List<GrantedAuthority> combinedAuthorities) {
        String returnUsername = userFromUserQuery.getUsername();
        return new RestUser(userId, returnUsername, userFromUserQuery.getPassword(), userFromUserQuery.isEnabled(),
                userFromUserQuery.isAccountNonExpired(), userFromUserQuery.isCredentialsNonExpired(),
                userFromUserQuery.isAccountNonLocked(), combinedAuthorities);
    }

    /**
     * Loads authorities by executing the SQL from <tt>authoritiesByUsernameQuery</tt>.
     * @return a list of GrantedAuthority objects for the user
     */
    protected List<GrantedAuthority> loadUserAuthorities(String username) {
        List<Authorities> authorities = authoritiesMapper.selectList(Wrappers.<Authorities>lambdaQuery().eq(Authorities::getUsername, username));
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(rolePrefix + authority.getAuthority()))
                .collect(Collectors.toList());
    }

    protected boolean getEnableAuthorities() {
        return this.enableAuthorities;
    }

}
