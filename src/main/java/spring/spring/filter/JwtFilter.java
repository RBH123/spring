package spring.spring.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import spring.spring.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
public class JwtFilter extends UsernamePasswordAuthenticationFilter {

    private UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;

    public JwtFilter(AuthenticationManager manager) {
        setAuthenticationManager(manager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String body = getBody(request);
        JSONObject jsonObject = JSON.parseObject(body);
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }
        usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        super.setDetails(request, usernamePasswordAuthenticationToken);
        return super.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
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
        response.addHeader("token", JwtUtil.generateToken(usernamePasswordAuthenticationToken));
    }

    public String getBody(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String body = "";
            while ((body = reader.readLine()) != null) {
                sb.append(body);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return sb.toString();
    }
}
