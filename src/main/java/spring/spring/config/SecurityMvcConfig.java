/**
 * @DateTime：2020/8/18$ 14:03$
 * @ClassName：IntelliJ IDEA
 * @PackageName：spring
 * @Author：Rocky.Ruan
 * @Desc：描述一下
 */
package spring.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import spring.spring.handler.InterceptorJwtHandler;

/**
 * @DateTime：2020/8/18 14:03
 * @ClassName：SecurityMvcConfig
 * @PackageName：spring.spring.config
 * @Author：Rocky.Ruan
 * @Desc：描述一下
 */
@Configuration
@EnableWebMvc
public class SecurityMvcConfig implements WebMvcConfigurer {

    @Autowired
    private InterceptorJwtHandler interceptorJwtHandler;

    String EXCLUDE_PATH = "";

    /**
     * 跨域配置
     *
     * @return
     */
    private CorsConfiguration config() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        //设置你要允许的网站域名，如果全允许则设为 *
        corsConfiguration.addAllowedOrigin("*");
        //如果要限制 HEADER 或 METHOD 请自行更改
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config());
        return new CorsFilter(source);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptorJwtHandler)
                .excludePathPatterns("/api/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/register")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
