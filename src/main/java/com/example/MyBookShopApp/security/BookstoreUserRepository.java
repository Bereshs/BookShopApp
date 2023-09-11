package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.aspects.annotations.UserSecurity;
import com.example.MyBookShopApp.struct.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@UserSecurity
public interface BookstoreUserRepository extends JpaRepository<UserEntity, Integer> {
    @UserSecurity
    UserEntity findUserByEmail(String email);
}
