package cn.bestlang.littlenote.security;

import cn.bestlang.littlenote.model.AuthenticationReq;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class RestLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/api/v1/login", "POST");

    public RestLoginAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        //use jackson to deserialize json
        ObjectMapper mapper = new ObjectMapper();
        UsernamePasswordAuthenticationToken authRequest = null;
        try (InputStream is = request.getInputStream()){
            AuthenticationReq authenticationBean = mapper.readValue(is,AuthenticationReq.class);
            authRequest = new UsernamePasswordAuthenticationToken(
                    authenticationBean.getUsername(), authenticationBean.getPassword());
        }catch (IOException e) {
            e.printStackTrace();
            authRequest = new UsernamePasswordAuthenticationToken(
                    "", "");
        } finally {
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
