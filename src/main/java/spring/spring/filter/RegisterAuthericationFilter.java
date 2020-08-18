package spring.spring.filter;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import spring.spring.dao.UsersMapper;
import spring.spring.dao.entity.Users;
import spring.spring.pojo.ao.UserInfoAo;
import spring.spring.util.BCryptUtil;
import spring.spring.util.HttpUtils;
import spring.spring.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterAuthericationFilter extends UsernamePasswordAuthenticationFilter {

    private UsersMapper usersMapper;

    public RegisterAuthericationFilter(AuthenticationManager authenticationManager, UsersMapper usersMapper) {
        setAuthenticationManager(authenticationManager);
        this.usersMapper = usersMapper;
        super.setFilterProcessesUrl("/register");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String body = HttpUtils.getBody((HttpServletRequest) servletRequest);
        if (StringUtils.isBlank(body)) {
            return;
        }
        UserInfoAo userInfoAo = JSON.parseObject(body, UserInfoAo.class);
        if (userInfoAo == null) {
            return;
        }
        Users users = new Users();
        BeanUtils.copyProperties(userInfoAo, users);
        users.setPassword(BCryptUtil.generateBCryPassword(userInfoAo.getPassword()));
        usersMapper.insert(users);
        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        response.setHeader("token", JwtUtil.generateToken(userInfoAo));
    }
}
