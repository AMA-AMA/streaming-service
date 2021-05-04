package com.epam.audio_streaming.repository.models;


import com.epam.audio_streaming.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);

    Boolean existsByEmail(String email);

   User findByActivationCode(String code);
}
