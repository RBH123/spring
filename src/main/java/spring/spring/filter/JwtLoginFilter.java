package spring.spring.filter;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import spring.spring.pojo.ao.UserInfoAo;
import spring.spring.util.HttpUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private UserInfoAo userInfoAo;

    public JwtLoginFilter(AuthenticationManager manager) {
        setAuthenticationManager(manager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String body = HttpUtils.getBody(request);
        userInfoAo = JSON.parseObject(body, UserInfoAo.class);
        super.setDetails(request, userInfoAo);
        return super.getAuthenticationManager().authenticate(userInfoAo);
    }

    @Override
    @SneakyThrows
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        getRememberMeServices().loginSuccess(request, response, authResult);
        getRememberMeServices().autoLogin(request, response);
        if (this.eventPublisher != null) {
            eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
        }
        super.getRememberMeServices().loginSuccess(request, response, authResult);
        log.info("登录成功");
        super.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }
}
