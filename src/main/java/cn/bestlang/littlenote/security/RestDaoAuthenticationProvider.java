package cn.bestlang.littlenote.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public class RestDaoAuthenticationProvider extends DaoAuthenticationProvider {

    /**
     * 将 RestUser 设置到 UsernamePasswordAuthenticationToken 中
     * @param principal
     * @param authentication
     * @param user
     * @return
     */
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        UsernamePasswordAuthenticationToken successAuthentication = (UsernamePasswordAuthenticationToken) super.createSuccessAuthentication(principal, authentication, user);
        successAuthentication.setDetails(user);
        return successAuthentication;
    }
}
