package spring.spring.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import spring.spring.pojo.ao.UserInfoAo;
import spring.spring.service.AuthenticationUserDetailService;
import spring.spring.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private AuthenticationUserDetailService authenticationUserDetailService;
    private JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationUserDetailService authenticationUserDetailService, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.authenticationUserDetailService = authenticationUserDetailService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("token");
        if (StringUtils.isNotBlank(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = jwtUtil.getUsernameFromToken(token);
            UserDetails userDetails = authenticationUserDetailService.loadUserByUsername(username);
            if (jwtUtil.validateToken(token, userDetails)) {
                UserInfoAo userInfoAo = UserInfoAo.builder().username(userDetails.getUsername()).password(userDetails.getPassword()).build();
                SecurityContextHolder.getContext().setAuthentication(userInfoAo);
            }
        }
        String requestURI = request.getRequestURI();
        System.out.println(requestURI);
        chain.doFilter(request, response);
    }
}
