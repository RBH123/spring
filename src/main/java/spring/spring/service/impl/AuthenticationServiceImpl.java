package spring.spring.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.spring.dao.UsersMapper;
import spring.spring.dao.entity.Users;
import spring.spring.pojo.ao.UserInfoAo;
import spring.spring.service.AuthenticationService;
import spring.spring.util.JwtUtil;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    UsersMapper usersMapper;

    @Override
    public void registerUser(UserInfoAo ao, HttpServletResponse response) {
        Users users = new Users();
        BeanUtils.copyProperties(ao, users);
        usersMapper.insert(users);
        response.setHeader("token", JwtUtil.generateToken(ao));
    }
}
