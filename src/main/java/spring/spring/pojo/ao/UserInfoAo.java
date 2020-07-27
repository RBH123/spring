package spring.spring.pojo.ao;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
@Builder
public class UserInfoAo extends UsernamePasswordAuthenticationToken {

    private String username;
    private String password;
    private String mobile;
    private String email;

    public UserInfoAo(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public UserInfoAo(String username, String password, String mobile, String email) {
        super(null, null);
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.email = email;
    }
}
