package cn.bestlang.littlenote.security;

import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;

/**
 * 由于 PersistentTokenBasedRememberMeServices 的 createSuccessfulAuthentication 方法 覆盖了 UserDetails 信息
 * 所以 override 加入UserDetails信息
 * 参考 AbstractUserDetailsAuthenticationProvider 的 createSuccessfulAuthentication 实现
 */
public class PersistentTokenBasedRememberMeServicesAuthentication extends PersistentTokenBasedRememberMeServices {

    public PersistentTokenBasedRememberMeServicesAuthentication(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
    }

    @Override
    protected Authentication createSuccessfulAuthentication(HttpServletRequest request, UserDetails user) {
        RememberMeAuthenticationToken successfulAuthentication = (RememberMeAuthenticationToken) super.createSuccessfulAuthentication(request, user);
        successfulAuthentication.setDetails(user);
        return successfulAuthentication;
    }
}
