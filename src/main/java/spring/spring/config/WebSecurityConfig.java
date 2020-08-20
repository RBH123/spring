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
import org.springframework.security.config.http.SessionCreationPolicy;
import spring.spring.filter.AuthenticationProvider;
import spring.spring.filter.JwtAuthenticationFilter;
import spring.spring.filter.JwtLoginFilter;
import spring.spring.service.AuthenticationUserDetailService;
import spring.spring.util.JwtUtil;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationUserDetailService authenticationUserDetailService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JwtUtil jwtUtil;

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
        httpSecurity.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/").permitAll()
                .antMatchers("/login", "/register").permitAll()
                .antMatchers("/auth/token").permitAll()
                .anyRequest()
                .permitAll()
                .and()
                .addFilter(new JwtLoginFilter(authenticationManager()))
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), authenticationUserDetailService, jwtUtil))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        httpSecurity
//                .addFilterAfter(new JwtLoginFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
//                .addFilterAfter(new JwtAuthenticationFilter(authenticationManager(), authenticationUserDetailService, jwtUtil), BasicAuthenticationFilter.class);
    }

    @Bean
    @SneakyThrows
    @Override
    public AuthenticationManager authenticationManager() {
        return super.authenticationManager();
    }

}
