package spring.spring.filter;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import spring.spring.pojo.ao.UserInfoAo;
import spring.spring.service.AuthenticationUserDetailService;

@Slf4j
@Service
public class AuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AuthenticationUserDetailService authenticationUserDetailService;

    public AuthenticationProvider(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
        super.setPasswordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * 登录认证
     *
     * @param auth
     * @return
     */
    @Override
    @SneakyThrows
    public Authentication authenticate(Authentication auth) {
        UserInfoAo ao = null;
        if (auth != null) {
            ao = (UserInfoAo) auth;
        }
        UserDetails user = null;
        String username = ao.getUsername() == null ? "NOT FOUND USERNAME" : String.valueOf(ao.getUsername());
        JSONObject jsonObject = (JSONObject) redisTemplate.opsForValue().get(username);
        if (jsonObject == null) {
            user = authenticationUserDetailService.getUserInfo(ao);
            redisTemplate.opsForValue().set(username, user);
        } else {
            user = JSONObject.toJavaObject(jsonObject, UserDetails.class);
        }
        //密码校验
        super.additionalAuthenticationChecks(user, ao);
        return super.createSuccessAuthentication(ao.getUsername(), ao, user);
    }

    /**
     * 验证密码
     *
     * @param userDetails
     * @param auth
     */
    @Override
    public void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken auth) {
        UserInfoAo ao = (UserInfoAo) auth;
        if (StringUtils.isBlank(ao.getPassword())) {
            log.info("Authentication failed: no credentials provided");
            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        String password = userDetails.getPassword();
        String inPassword = ao.getPassword();
        if (!super.getPasswordEncoder().matches(password, inPassword)) {
            log.info("Authentication failed: no credentials provided");
            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }
}
