package com.example.MyBookShopApp.aspects;

import com.example.MyBookShopApp.redis.JWTTokenEntity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Aspect
@Component
public class JWTTokenAspect {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Pointcut(value = "execution(* com.example.MyBookShopApp.redis.JWTTokenBlackListRedisRepository.findByToken(..))")
    public void findByTokenInBlackListPointcut() {
    }

    @AfterReturning(pointcut = "findByTokenInBlackListPointcut()", returning = "token")
    public void resultFromFindByTokenInBlackListPointcut(List<JWTTokenEntity> token) {
        if (!token.isEmpty()) {
            logger.info("find token in blacklist");
        } else {
            logger.info("not found token in blacklist");

        }
    }

    @Pointcut(value = "execution(* com.example.MyBookShopApp.security.jwt.JWTUtil.validateToken(..))")
    public void invalidTokenPointcut() {
    }

    @AfterReturning(pointcut = "invalidTokenPointcut()", returning = "isValid")
    public void receiveInvalidTokenPointcut(Boolean isValid) {
        if (!isValid) {
            logger.info("invalid token");
        } else  {
            logger.info("token is valid");
        }
    }

}
