package spring.spring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public aspect LogAspect {

    @Pointcut("execution(* spring.spring.controller..*.*(..))")
    public void aspectPoint() {

    }

    @Before("aspectPoint()")
    public void logAspect(JoinPoint joinPoint) {
        log.info("%s:%s", joinPoint.getTarget(), joinPoint.getArgs());
    }
}
