/**
 * @DateTime：2020/8/19$ 11:30$
 * @ClassName：IntelliJ IDEA
 * @PackageName：spring
 * @Author：Rocky.Ruan
 * @Desc：描述一下
 */
package spring.spring.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @DateTime：2020/8/19 11:30
 * @ClassName：Oauth2TokenDto
 * @PackageName：spring.spring.pojo
 * @Author：Rocky.Ruan
 * @Desc：描述一下
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Oauth2TokenDto {
    /**
     * 访问令牌
     */
    private String token;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 访问令牌头前缀
     */
    private String tokenHead;
    /**
     * 有效时间（秒）
     */
    private int expiresIn;
}
