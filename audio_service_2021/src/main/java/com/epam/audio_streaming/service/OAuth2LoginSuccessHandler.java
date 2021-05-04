package com.epam.audio_streaming.service;


import com.epam.audio_streaming.model.CustomOAuth2User;
import com.epam.audio_streaming.model.Roles;
import com.epam.audio_streaming.model.User;
import com.epam.audio_streaming.service.models.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    private UserService userService;

    public OAuth2LoginSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = userService.findByEmail(oauth2User.getName());
        if (user == null) {
            User newUser = new User(oauth2User.firstName(), oauth2User.lastName(), oauth2User.getEmail(),
                    oauth2User.emailVerified());
            newUser.setRoles(Roles.ROLE_USER);
            userService.save(newUser);
        } else {
            user.setFirstName(oauth2User.firstName());
            user.setLastName(oauth2User.lastName());
            user.setEmail(oauth2User.getEmail());
            userService.save(user);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

}