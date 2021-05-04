package com.epam.audio_streaming.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Boolean emailVerified;

    private String activationCode;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Roles roles;

    private String password;

    public User(String firstName, String lastName, String email, Boolean emailVerified) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.emailVerified = emailVerified;
    }

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

    }

    public User(String firstName, String lastName, String email, String activationCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.activationCode = activationCode;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<PlayList> playLists = new ArrayList<>();

    public void setPlayLists(List<PlayList> playLists) {
        if (playLists != null) {
            playLists.forEach(a -> {
                a.setUser(this);
            });
        }
        this.playLists = playLists;
    }

}
