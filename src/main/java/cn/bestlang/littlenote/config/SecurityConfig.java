package cn.bestlang.littlenote.config;

import cn.bestlang.littlenote.security.RestAccessDeniedHandler;
import cn.bestlang.littlenote.security.RestAuthenticationEntryPoint;
import cn.bestlang.littlenote.security.RestLoginAuthenticationFilter;
import cn.bestlang.littlenote.util.JsonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/api/v1/signup").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(restLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(c -> {
            c.accessDeniedHandler(restAccessDeniedHandler());
            c.authenticationEntryPoint(restAuthenticationEntryPoint());
        })
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    @Bean // share AuthenticationManager for web and oauth
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public RestLoginAuthenticationFilter restLoginAuthenticationFilter() throws Exception {
        RestLoginAuthenticationFilter restLoginAuthenticationFilter = new RestLoginAuthenticationFilter();
        restLoginAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());

        restLoginAuthenticationFilter
                .setAuthenticationSuccessHandler((req, resp, authentication) -> {
            Object principal = authentication.getPrincipal();
            resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter out = resp.getWriter();
            out.write(JsonUtil.toJson(principal));
        });
        restLoginAuthenticationFilter.setAuthenticationFailureHandler((req, resp, e) -> {
                    resp.setStatus(HttpStatus.FORBIDDEN.value());
                    resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    PrintWriter out = resp.getWriter();
                    out.write(JsonUtil.toJson(e.getMessage()));
                });
        return restLoginAuthenticationFilter;
    }

    @Bean
    public RestAccessDeniedHandler restAccessDeniedHandler() {
        return new RestAccessDeniedHandler();
    }

    @Bean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

}
