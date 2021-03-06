package com.epam.tishkin.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
@Table(name = "User")
public class User implements Serializable {
    private final static long serialVersionUID = 65896589L;
    @Id
    @Column(name = "Login")
    @NotEmpty(message = "Please provide a login")
    private String login;
    @Column (name = "Password")
    @NotEmpty(message = "Please provide a password")
    private String password;
    @Column (name = "Role")
    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {

    }

    public User(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return login + " " + role;
    }
}
