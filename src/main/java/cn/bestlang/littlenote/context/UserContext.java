package cn.bestlang.littlenote.context;

import cn.bestlang.littlenote.security.RestUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserContext {

    public static Integer getId() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        RestUser restUser = (RestUser) authentication.getDetails();

        return restUser.getUserId();
    }
}
