package spring.spring.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 生成密码
 */
public class BCryptUtil {

    static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    /**
     * 生成密码
     *
     * @param value
     * @return
     */
    public static String generateBCryPassword(String value) {
        return bCryptPasswordEncoder.encode(value);
    }
}
