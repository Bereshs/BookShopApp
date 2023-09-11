package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.data.SmsService;
import com.example.MyBookShopApp.data.storage.SmsCode;
import com.example.MyBookShopApp.errs.NoUserFoundException;
import com.example.MyBookShopApp.errs.UserAlreadyExistException;
import com.example.MyBookShopApp.redis.JWTTokenBlackListRedisRepository;
import com.example.MyBookShopApp.redis.JWTTokenEntity;
import com.example.MyBookShopApp.struct.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.Objects.isNull;


@Controller
public class AuthUserController {

    @Value("${appEmail.email}")
    private String emailFrom;

    private final BookstoreUserRegister userRegister;

    private final BookstoreUserRepository userRepository;

    private final JWTTokenBlackListRedisRepository tokenBlackListRedisRepository;

    private final SmsService smsService;

    private final JavaMailSender javaMailSender;


    @Autowired
    public AuthUserController(BookstoreUserRegister bookstoreUserRegister, BookstoreUserRepository userRepository, JWTTokenBlackListRedisRepository tokenBlackListRedisRepository, SmsService smsService, JavaMailSender javaMailSender) {
        this.userRegister = bookstoreUserRegister;
        this.userRepository = userRepository;
        this.tokenBlackListRedisRepository = tokenBlackListRedisRepository;
        this.smsService = smsService;
        this.javaMailSender = javaMailSender;
    }

    @GetMapping("/loginSuccess")
    public String handleLoginSuccess(HttpServletResponse httpServletResponse
            , HttpServletRequest request) {

        String user = getCookie("google-email", request.getCookies());
        Cookie cookie = createNullAgeCookie("google-email", "");
        cookie.setPath("/loginSuccess");
        httpServletResponse.addCookie(cookie);
        UserEntity userRepo = userRepository.findUserByEmail(user);
        ContactConfirmationPayload payload = new ContactConfirmationPayload();
        if (userRepo != null) {
            payload.setContact(user);
            payload.setCode("1234567");
            ContactConfirmationResponse loginResponse = userRegister.jwtLogin(payload);
            cookie = new Cookie("token", loginResponse.getResult());
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
        }

        return "redirect:/my";
    }

    @GetMapping("/signin")
    public String handleLogout(@RequestParam(name = "logout", required = false) boolean logout
            , HttpServletRequest request
            , HttpServletResponse response) {
        String token = getCookie("token", request.getCookies());
        if (token != null) {
            tokenBlackListRedisRepository.save(new JWTTokenEntity(token, userRegister.getExpiration(token).getTime()));
            response.addCookie(createNullAgeCookie("token", token));
        }
        if (logout) {
            return "redirect:/";
        }
        return "signin";
    }

    @GetMapping("/signup")
    public String handleSingup(Model model) {
        model.addAttribute("regForm", new RegistrationForm());
        return "signup";
    }

    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        if (payload.getContact().contains("@")) {
            return response;
        }

        return response;
    }

    @PostMapping("/requestEmailConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestEmailConfirmation(@RequestBody ContactConfirmationPayload payload) {
        if (payload.getContact().contains("@")) {
            return requestEmailConfirmation(payload);
        }
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }


    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("false");
        // TODO исправить для проверки смс
        if (!payload.getContact().contains("@")) {
            Logger.getLogger(AuthUserController.class.getName()).info("sms code confirmation, code received " + payload.getCode() + " verification result = " + payload.getCode());
            response.setResult("true");
            return response;
        }

        if (smsService.verifyCode(payload.getCode())) {
            Logger.getLogger(AuthUserController.class.getName()).info("email code confirmation, code received " + payload.getCode() + " verification result = " + smsService.verifyCode(payload.getCode()));
            response.setResult("true");
        }

        return response;
    }

    @PostMapping("/reg")
    public String handleUserRegistration(RegistrationForm registrationForm, Model model) throws UserAlreadyExistException {
        UserEntity user = userRepository.findUserByEmail(registrationForm.getEmail());
        if (user != null) {
            throw new UserAlreadyExistException("User " + registrationForm.getEmail() + " already exist");
        }

        userRegister.registerUser(registrationForm);
        model.addAttribute("regOk", true);
        return "signin";
    }


    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload, HttpServletResponse httpServletResponse) throws NoUserFoundException {
        UserEntity user = userRepository.findUserByEmail(payload.getContact());
        if (user == null) {
            throw new NoUserFoundException("wrong username " + payload.getContact());
        }
        ContactConfirmationResponse loginResponse = userRegister.jwtLogin(payload);
        Cookie cookie = new Cookie("token", loginResponse.getResult());
        httpServletResponse.addCookie(cookie);
        return loginResponse;
    }

    @GetMapping("/my")
    public String handleMy() {
        return "my";
    }


    public String getCookie(String key, Cookie[] cookies) {
        String result = null;
        if (isNull(cookies)) {
            return null;
        }
        Optional<String> optionalToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(key))
                .map(Cookie::getValue)
                .findAny();
        if (optionalToken.isPresent()) {
            result = optionalToken.get();
        }
        return result;
    }

    public Cookie createNullAgeCookie(String token, String value) {
        Cookie cookie = new Cookie(token, value);
        cookie.setMaxAge(0);
        return cookie;
    }

    public ContactConfirmationResponse requestEmailConfirmation(ContactConfirmationPayload payload) {
        Logger.getLogger(AuthUserController.class.getName()).info("Sending to e-mail " + payload.getContact() + " authorization code ");
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(payload.getContact());
        SmsCode smsCode = new SmsCode(smsService.generateCode(), 300);
        smsService.saveNewCode(smsCode);
        message.setSubject("Book store email notification");
        message.setText("Verification code is:" + smsCode.getCode());
        javaMailSender.send(message);
        response.setResult("true");

        return response;
    }

}
