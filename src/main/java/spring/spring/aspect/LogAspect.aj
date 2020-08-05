package spring.spring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(public * spring.spring.controller..*.* (..))")
    public void aspectPoint() {
        System.out.println("切点成功");
    }

    @AfterReturning("aspectPoint()")
    public void logAspect(JoinPoint joinPoint) {
        System.out.println("后置切面成功" + joinPoint.toString());
        log.info("%s:%s", joinPoint.getTarget(), joinPoint.getArgs());
    }
}
