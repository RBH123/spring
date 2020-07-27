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
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import spring.spring.dao.UsersMapper;
import spring.spring.filter.AuthenticationProvider;
import spring.spring.filter.JwtLoginFilter;
import spring.spring.filter.RegisterAuthericationFilter;
import spring.spring.service.AuthenticationUserDetailService;

import javax.sql.DataSource;

import static spring.spring.constant.SecurityConstant.REMEMBER_ME_KEY;
import static spring.spring.constant.SecurityConstant.REMEMBER_ME_PARAMETER;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationUserDetailService authenticationUserDetailService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UsersMapper usersMapper;

    @Bean
    public RememberMeServices rememberMeServices() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices = new PersistentTokenBasedRememberMeServices(REMEMBER_ME_KEY, authenticationUserDetailService, jdbcTokenRepository);
        persistentTokenBasedRememberMeServices.setParameter(REMEMBER_ME_PARAMETER);
        persistentTokenBasedRememberMeServices.setTokenValiditySeconds(3600);
        return persistentTokenBasedRememberMeServices;
    }

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
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/").permitAll()
                .antMatchers("/login").permitAll()
                .anyRequest()
                .authenticated();
        RememberMeAuthenticationFilter rememberMeAuthenticationFilter = new RememberMeAuthenticationFilter(authenticationManager(), rememberMeServices());
        httpSecurity
                .addFilterAfter(new JwtLoginFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new RegisterAuthericationFilter(authenticationManager(), usersMapper), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(rememberMeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @SneakyThrows
    @Override
    public AuthenticationManager authenticationManager() {
        return super.authenticationManager();
    }

}
