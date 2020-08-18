/**
 * @DateTime：2020/8/18$ 10:16$
 * @ClassName：IntelliJ IDEA
 * @PackageName：spring
 * @Author：Rocky.Ruan
 * @Desc：描述一下
 */
package spring.spring.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @DateTime：2020/8/18 10:16
 * @ClassName：JwtConfig
 * @PackageName：spring.spring.config
 * @Author：Rocky.Ruan
 * @Desc：描述一下
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "config.jwt")
public class JwtConfig {
    private String secret;
    private int expire;
    private String header;
}
