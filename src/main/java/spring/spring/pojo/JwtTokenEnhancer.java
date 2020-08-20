/**
 * @DateTime：2020/8/19$ 10:34$
 * @ClassName：IntelliJ IDEA
 * @PackageName：spring
 * @Author：Rocky.Ruan
 * @Desc：描述一下
 */
package spring.spring.pojo;

import com.google.common.collect.Maps;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;
import spring.spring.pojo.ao.UserInfoAo;

import java.util.Map;

/**
 * @DateTime：2020/8/19 10:34
 * @ClassName：JwtTokenEnhancer
 * @PackageName：spring.spring.pojo
 * @Author：Rocky.Ruan
 * @Desc：描述一下
 */
@Component
public class JwtTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        UserInfoAo ao = (UserInfoAo) oAuth2Authentication.getPrincipal();
        Map<String, Object> map = Maps.newHashMap();
        map.put("aopsID", ao.getAopsID());
        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(map);
        return oAuth2AccessToken;
    }
}
