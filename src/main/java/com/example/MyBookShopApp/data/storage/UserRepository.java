package com.example.MyBookShopApp.data.storage;

import com.example.MyBookShopApp.struct.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    public UserEntity findByEmail(String email);
}
