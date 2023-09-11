package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.security.jwt.JWTRequestFilter;
import com.example.MyBookShopApp.struct.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.logging.Logger;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final BookstoreUserDetailsService bookStoreUserDetailsService;
    private final JWTRequestFilter filter;


    private final BookstoreUserRepository userRepository;

    @Autowired
    public SecurityConfig(BookstoreUserDetailsService bookStoreUserDetailsService, JWTRequestFilter filter, BookstoreUserRepository userRepository) {
        this.bookStoreUserDetailsService = bookStoreUserDetailsService;
        this.filter = filter;
        this.userRepository = userRepository;
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(bookStoreUserDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/my", "/profile").authenticated()//.hasRole("USER")
                .antMatchers("/**").permitAll()
                .and().formLogin()
                .loginPage("/signin").failureUrl("/signin")
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/signin?logout=true")
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .and()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                        Authentication authentication) throws IOException {
                        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

                        Map<String, Object> attributes = oauthUser.getAttributes();
                        UserEntity userExist = userRepository.findUserByEmail(oauthUser.getAttributes().get("email").toString());
                        if (userExist == null) {
                            UserEntity user = new UserEntity();
                            user.setEmail(attributes.get("email").toString());
                            user.setName(attributes.get("name").toString());
                            user.setPhone("none");
                            user.setPassword("$2a$10$pbclUT0XJPf/8jTebsyrnOBPgcqGCAt1XpHYUVsVB85bnBctCWWdO");
                            user.setHash(String.valueOf(oauthUser.getAttributes().hashCode()));
                            user.setRegTime(LocalDateTime.now());
                            userRepository.save(user);
                        }
                        Cookie cookie = new Cookie("google-email", attributes.get("email").toString());
                        cookie.setPath("/loginSuccess");
                        cookie.setMaxAge(1);
                        response.addCookie(cookie);
                        response.sendRedirect("/loginSuccess");
                    }
                });

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }


}
