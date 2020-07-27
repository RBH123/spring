package spring.spring.filter;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import spring.spring.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    @SneakyThrows
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            return;
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = JwtUtil.parseToken(token);
        chain.doFilter(request, response);
    }
}
