package com.epam.audio_streaming.controller;

import com.epam.audio_streaming.dto.UserRegistrationDTO;
import com.epam.audio_streaming.service.models.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping
    public Boolean registration(@RequestBody @Valid UserRegistrationDTO registrationDTO) {
        return userService.registrationUser(registrationDTO);
    }

    @GetMapping("/activate/{code}")
    public Boolean activate( @PathVariable String code) {
        return userService.activateCode(code);
    }

}
