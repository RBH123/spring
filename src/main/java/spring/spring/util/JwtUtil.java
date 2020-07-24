package spring.spring.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class JwtUtil {

    final static String SECRET = "qwer";
    final static long EXPIRE_TIME = 7 * 24 * 60 * 60;

    /**
     * 生成token
     *
     * @param jwtToken
     * @return
     */
    public static String generateToken(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
        String token = JWT.create()
                .withClaim("username", String.valueOf(usernamePasswordAuthenticationToken.getPrincipal()))
                .withClaim("password", String.valueOf(usernamePasswordAuthenticationToken.getCredentials()))
                .withExpiresAt(new Date(System.currentTimeMillis() / 1000 + EXPIRE_TIME))
                .withIssuedAt(new Date())
                .withIssuer("ruan")
                .withJWTId(UUID.randomUUID().toString())
                .sign(Algorithm.HMAC512(SECRET));
        return token;
    }

    /**
     * 解析token
     *
     * @param token
     * @return
     */
    public static UsernamePasswordAuthenticationToken parseToken(String token) {
        Map<String, Claim> claims = JWT.decode(token).getClaims();
        String username = claims.get("username").asString();
        String password = claims.get("password").asString();
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}
