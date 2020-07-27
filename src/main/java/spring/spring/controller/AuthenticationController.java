package spring.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spring.spring.pojo.ao.UserInfoAo;

import java.rmi.ServerException;

@Slf4j
@RequestMapping(value = "/authentic")
@RestController
public class AuthenticationController {

    /**
     * 注册
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(@RequestBody UserInfoAo userInfoAo) throws ServerException {
        System.out.println(userInfoAo);
        if (userInfoAo == null) {
            throw new ServerException("用户信息必填");
        }

    }
}
