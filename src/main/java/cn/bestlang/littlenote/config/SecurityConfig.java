package cn.bestlang.littlenote.config;

import cn.bestlang.littlenote.mapper.UserMapper;
import cn.bestlang.littlenote.security.DbUserDetailsService;
import cn.bestlang.littlenote.security.RestAccessDeniedHandler;
import cn.bestlang.littlenote.security.RestAuthenticationEntryPoint;
import cn.bestlang.littlenote.security.RestLoginAuthenticationFilter;
import cn.bestlang.littlenote.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

import javax.sql.DataSource;
import java.io.PrintWriter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/api/v1/signup").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(restLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(rememberMeAuthenticationProvider())
                .rememberMe((rememberMe) ->
                        rememberMe
                                .tokenRepository(persistentTokenRepository())
                        .rememberMeServices(rememberMeServices(userDetailsService(), persistentTokenRepository()))
                )
                .exceptionHandling(c -> {
                    c.accessDeniedHandler(restAccessDeniedHandler());
                    c.authenticationEntryPoint(restAuthenticationEntryPoint());
                })
        ;
    }

    @Bean
    public UserDetailsService userDetailsService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        return new DbUserDetailsService(userMapper, passwordEncoder);
    }

    @Bean
    public RememberMeAuthenticationFilter rememberMeFilter(RememberMeServices rememberMeServices, AuthenticationManager authenticationManager) {
        return new RememberMeAuthenticationFilter(authenticationManager, rememberMeServices);
    }

    @Bean
    public RememberMeServices rememberMeServices(UserDetailsService userDetailsService, PersistentTokenRepository persistentTokenRepository) {
        return new PersistentTokenBasedRememberMeServices("springRocks", userDetailsService, persistentTokenRepository);
    }

//    @Bean
    public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
        return new RememberMeAuthenticationProvider("springRocks");
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);

        return jdbcTokenRepository;
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
        restLoginAuthenticationFilter.setRememberMeServices(getApplicationContext().getBean(RememberMeServices.class));

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

            e.printStackTrace();
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
