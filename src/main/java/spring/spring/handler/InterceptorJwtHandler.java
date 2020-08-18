/**
 * @DateTime：2020/8/18$ 10:33$
 * @ClassName：IntelliJ IDEA
 * @PackageName：spring
 * @Author：Rocky.Ruan
 * @Desc：描述一下
 */
package spring.spring.handler;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import spring.spring.anno.IgnoreToken;
import spring.spring.config.JwtConfig;
import spring.spring.constant.CheckTokenEnum;
import spring.spring.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @DateTime：2020/8/18 10:33
 * @ClassName：InterceptorJWT
 * @PackageName：spring.spring.handler
 * @Author：Rocky.Ruan
 * @Desc：描述一下
 */
@Slf4j
@Component
public class InterceptorJwtHandler extends HandlerInterceptorAdapter {

    final String PRE_TOKEN = "token";

    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedisTemplate redisTemplate;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            IgnoreToken ignoreToken = ((HandlerMethod) handler).getMethodAnnotation(IgnoreToken.class);
            if (ignoreToken != null && ignoreToken.value() == CheckTokenEnum.NO_NEED_TOKEN) {
                return true;
            }
        } else {
            String token = request.getHeader(jwtConfig.getHeader());
            if (StringUtils.isBlank(token)) {
                return false;
            }
            Claims claims = jwtUtil.getTokenClaim(token);
            if (claims == null) {
                throw new Exception("token无效，请重新登录");
            }
            Long aopsID = (Long) claims.get("aopsID");
            String redisToken = (String) redisTemplate.opsForValue().get(PRE_TOKEN + aopsID);
            if (StringUtils.isBlank(redisToken)) {
                throw new Exception("token已过期，请重新登录");
            }
            Boolean tokenExpired = jwtUtil.isTokenExpired(token);
            if (!token.equals(redisToken) || BooleanUtils.isTrue(tokenExpired)) {
                throw new Exception("token已过期，请重新登录");
            }
            request.setAttribute("aopsID", aopsID);
        }
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }
}
