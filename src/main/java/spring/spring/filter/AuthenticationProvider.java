package spring.spring.filter;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private RedisTemplate redisTemplate;

    public AuthenticationProvider(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
        super.setPasswordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    @SneakyThrows
    public Authentication authenticate(Authentication authentication) {
        UserDetails user = null;
        String username = authentication.getPrincipal() == null ? "NOT FOUND USERNAME" : String.valueOf(authentication.getPrincipal());
        user = (UserDetails) redisTemplate.opsForValue().get(username);
        if (user == null) {
            user = getUserDetailsService().loadUserByUsername(username);
            redisTemplate.opsForValue().set(username, user);
        }
        super.additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) authentication);
        return super.createSuccessAuthentication(authentication.getPrincipal(), authentication, user);
    }
}
