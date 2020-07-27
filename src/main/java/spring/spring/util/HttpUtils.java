package spring.spring.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
public class HttpUtils {

    public static String getBody(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String body = "";
            while ((body = reader.readLine()) != null) {
                sb.append(body);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return sb.toString();
    }
}
