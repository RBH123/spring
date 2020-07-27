package spring.spring.service;

import spring.spring.pojo.ao.UserInfoAo;

import javax.servlet.http.HttpServletResponse;

public interface AuthenticationService {

    void registerUser(UserInfoAo ao, HttpServletResponse response);
}
