package com.example.MyBookShopApp.redis;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@RedisHash("bookstoreTokensBlackList")
public class JWTTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String token;

    @TimeToLive
    private Long expiration;

    public JWTTokenEntity(String token, long expiration) {
        this.token = token;
        this.expiration=expiration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }
}
