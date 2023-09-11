package com.example.MyBookShopApp.aspects;

import com.example.MyBookShopApp.security.BookstoreUserDetails;
import com.example.MyBookShopApp.struct.user.UserEntity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.logging.Logger;

@Aspect
@Component
public class UserSecurityAspect {

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Around(value = "@annotation(com.example.MyBookShopApp.aspects.annotations.UserSecurity)")
    public Object userSecurityAspect(ProceedingJoinPoint proceedingJoinPoint) {
        Object result = null;
        logger.info(proceedingJoinPoint.toShortString() + " with parameters " + Arrays.toString(proceedingJoinPoint.getArgs()) + " starting");
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        logger.info(proceedingJoinPoint.toShortString() + " completed");
        return result;
    }

    @Pointcut(value = "execution(* com.example.MyBookShopApp.security.BookstoreUserDetailsService.loadUserByUsername(..))")
    public void loadUserFromStringAspect() {
    }

    @Before(value = "args(name) && loadUserFromStringAspect()")
    public void beforeLoadUserAspect(String name) {
        logger.info("Loading form string " + name);
    }

    @AfterReturning(pointcut = "loadUserFromStringAspect()", returning = "user")
    public void afterLoadUserAspect(BookstoreUserDetails user) {
        logger.info("Receiving data username " + user.getUsername());
    }
}
