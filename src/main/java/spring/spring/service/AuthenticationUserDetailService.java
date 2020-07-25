package spring.spring.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import spring.spring.dao.UserMapper;
import spring.spring.dao.entity.Users;

@Service
@Slf4j
public class AuthenticationUserDetailService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Users users = userMapper.selectOne(queryWrapper);
        if (users == null) {
            return null;
        }
        return User.builder().username(users.getUsername()).password(users.getPassword()).authorities(Lists.newArrayList(new SimpleGrantedAuthority(users.getRole() == 0 ? "Normal" : "Admin"))).build();
    }
}
