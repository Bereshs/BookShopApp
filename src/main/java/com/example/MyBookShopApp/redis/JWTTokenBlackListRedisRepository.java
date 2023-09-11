package com.example.MyBookShopApp.redis;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface JWTTokenBlackListRedisRepository extends JpaRepository<JWTTokenEntity, Integer> {
    public List<JWTTokenEntity> findByToken(String token);

}
