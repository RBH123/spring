package spring.spring.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import spring.spring.dao.UsersMapper;
import spring.spring.dao.entity.Users;
import spring.spring.pojo.ao.UserInfoAo;

@Service
@Slf4j
public class AuthenticationUserDetailService implements UserDetailsService {

    @Autowired
    private UsersMapper usersMapper;

    /**
     * 更具用户名拿到用户信息
     *
     * @param username
     * @return
     */
    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Users users = usersMapper.selectOne(queryWrapper);
        if (users == null) {
            return null;
        }
        return User.builder().username(users.getUsername()).password(users.getPassword()).authorities(Lists.newArrayList(new SimpleGrantedAuthority(users.getRole() == 0 ? "Normal" : "Admin"))).build();
    }

    public UserDetails getUserInfo(UserInfoAo ao) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(ao.getUsername())) {
            queryWrapper.eq("username", ao.getUsername());
        }
        if (StringUtils.isNotBlank(ao.getEmail())) {
            queryWrapper.eq("email", ao.getEmail());
        }
        if (StringUtils.isNotBlank(ao.getMobile())) {
            queryWrapper.eq("mobile", ao.getMobile());
        }
        Users users = usersMapper.selectOne(queryWrapper);
        if (users == null) {
            return null;
        }
        return User.builder().username(users.getUsername()).password(users.getPassword()).authorities(Lists.newArrayList()).build();
    }
}
