package spring.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spring.spring.dao.entity.Users;

import java.rmi.ServerException;

@Slf4j
@RequestMapping(value = "/authentic")
@RestController
public class AuthenticationController {
    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    /**
     * 注册
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestBody Users users) throws ServerException {
        System.out.println(users);
        if (users == null) {
            throw new ServerException("用户信息必填");
        }
        log.info("今天天气好晴朗");
        return "今天是个好日子，docker部署成功";
    }
}
