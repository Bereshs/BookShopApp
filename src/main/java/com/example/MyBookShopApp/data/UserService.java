package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.storage.UserRepository;
import com.example.MyBookShopApp.struct.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getById(Integer userId) {
        return  userRepository.getById(userId);
    }

    public UserEntity getByEmail(String email){ return userRepository.findByEmail(email);}
}
