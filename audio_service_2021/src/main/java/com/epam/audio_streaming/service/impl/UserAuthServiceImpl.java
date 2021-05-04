package com.epam.audio_streaming.service.impl;

import com.epam.audio_streaming.exception.NotFoundException;
import com.epam.audio_streaming.model.User;
import com.epam.audio_streaming.service.models.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserAuthServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @SneakyThrows
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userFromDB = userService.findByEmail(username);
        if (userFromDB == null || userFromDB.getActivationCode() != null) {
            throw new NotFoundException("There is no user registered with this email: " + userFromDB.getEmail());
        } else {
            return new org.springframework.security.core.userdetails.User(userFromDB.getEmail(),
                    userFromDB.getPassword(), toAuthorities(userFromDB));
        }
    }

    public Collection<? extends GrantedAuthority> toAuthorities(User user) {
        return Collections.singleton(new SimpleGrantedAuthority(user.getRoles().name()));
    }
}
