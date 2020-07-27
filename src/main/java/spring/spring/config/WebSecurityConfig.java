package spring.spring.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import spring.spring.filter.AuthenticationProvider;
import spring.spring.filter.JwtAuthenticationFilter;
import spring.spring.filter.JwtLoginFilter;
import spring.spring.service.AuthenticationUserDetailService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationUserDetailService authenticationUserDetailService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthenticationProvider(authenticationUserDetailService);
    }

    @Override
    @SneakyThrows
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    @SneakyThrows
    public void configure(HttpSecurity httpSecurity) {
        httpSecurity
                .cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/").permitAll()
                .antMatchers("/login", "/register").permitAll()
                .anyRequest()
                .authenticated();
        httpSecurity.addFilterBefore(new JwtLoginFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(new JwtAuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class);
    }

    @Bean
    @SneakyThrows
    @Override
    public AuthenticationManager authenticationManager() {
        return super.authenticationManager();
    }

}
