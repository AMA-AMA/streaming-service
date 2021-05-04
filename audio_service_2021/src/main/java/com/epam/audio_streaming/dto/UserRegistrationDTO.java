package com.epam.audio_streaming.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@Data
public class UserRegistrationDTO {

	@Length(min = 3, max = 20)
    private String firstName;

	@Length(min = 3, max = 20)
    private String lastName;

	@Email
    private String email;

	@Length(min = 3, max = 20)
    private String password;

    public UserRegistrationDTO(@Length(min = 3, max = 20) String firstName, @Length(min = 3, max = 20) String lastName,
                               @Email String email, @Length(min = 3, max = 20) String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
