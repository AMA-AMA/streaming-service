package com.epam.audio_streaming.service.models;

import com.epam.audio_streaming.dto.UserRegistrationDTO;
import com.epam.audio_streaming.model.User;

public interface UserService {

    User save(User user);

    User findByEmail(String username);

    User findById(String id);

    Boolean registrationUser(UserRegistrationDTO registrationDTO);

    boolean activateCode(String code);
}


