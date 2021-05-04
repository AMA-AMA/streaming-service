package com.epam.audio_streaming.service.models.impl;

import com.epam.audio_streaming.dto.UserRegistrationDTO;
import com.epam.audio_streaming.model.MailSender;
import com.epam.audio_streaming.model.Roles;
import com.epam.audio_streaming.model.User;
import com.epam.audio_streaming.repository.models.UserRepository;
import com.epam.audio_streaming.service.models.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailSender mailSender;

    @Lazy  //or use constructor for delete circle exception
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findById(String id) {
        return userRepository.getOne(id);
    }

    @Override
    @Transactional
    public Boolean registrationUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.findByEmail(registrationDTO.getEmail()) == null) {
            User user = new User(registrationDTO.getFirstName(), registrationDTO.getLastName(), registrationDTO.getEmail());
            user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
            user.setEmailVerified(true);
            user.setRoles(Roles.ROLE_USER);
            user.setActivationCode(UUID.randomUUID().toString());

            userRepository.save(user);
            sendEmail(user);
            return true;
        }
        return false;
    }

    private void sendEmail(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello,%s! \nWelcome to Service! Please, visit next link: http://localhost:8080/registration/activate/%s",
                    user.getFirstName(),
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
            System.out.println(message);
        }
    }

    @Override
    public boolean activateCode(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        userRepository.save(user);
        return true;
    }


}

