package spring.spring.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationUserDetailService implements UserDetailsService {

    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) {
        return null;
    }
}
