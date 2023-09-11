package com.example.MyBookShopApp.controllers.api;

import com.example.MyBookShopApp.data.dto.ApiSimpleResponse;
import com.example.MyBookShopApp.data.dto.ProfileDataTransfer;
import com.example.MyBookShopApp.security.BookstoreUserRepository;
import com.example.MyBookShopApp.security.jwt.JWTUtil;
import com.example.MyBookShopApp.struct.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@Controller
@RestController
@RequestMapping("/profile")
public class RestApiProfileController {

    private final BookstoreUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JWTUtil jwtUtil;


    @Autowired
    public RestApiProfileController(BookstoreUserRepository userRepository, PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/save")
    public ApiSimpleResponse saveProfileHandle(@RequestBody ProfileDataTransfer receiveData, @CookieValue String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApiSimpleResponse response = new ApiSimpleResponse(false);
        String currentPrincipalName = authentication.getName();
        UserEntity user = userRepository.findUserByEmail(currentPrincipalName);

        Logger.getLogger(RestApiProfileController.class.getName()).info("username " + currentPrincipalName + " requested to change profile information");

        if (user == null) {
            return response;
        }
        if (!receiveData.getPassword().isEmpty() && receiveData.getPassword().equals(receiveData.getPasswordReply())) {
            Logger.getLogger(RestApiProfileController.class.getName()).info("username " + currentPrincipalName + " requested to change profile password");
            user.setPassword(passwordEncoder.encode(receiveData.getPassword()));
            userRepository.save(user);
            response.setResult(true);
            return response;
        }
        if (!receiveData.getMail().equals(user.getEmail()) || !receiveData.getPhone().equals(user.getPhone())) {
            Logger.getLogger(RestApiProfileController.class.getName()).info("username " + currentPrincipalName + " requested to change mail/phone");
            user.setEmail(receiveData.getMail());
            user.setPhone(receiveData.getPhone());
            userRepository.save(user);
            response.setResult(true);
            return response;
        }

        if (!receiveData.getName().equals(user.getName())) {
            Logger.getLogger(RestApiProfileController.class.getName()).info("username " + currentPrincipalName + " requested to change name");
            user.setName(receiveData.getName());
            userRepository.save(user);
            response.setResult(true);
            return response;
        }
        return response;
    }
}
