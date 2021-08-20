package com.epam.tishkin.messages;

public class SignInForm {
    private String login;
    private String password;

    public SignInForm(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public SignInForm(){

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
}
