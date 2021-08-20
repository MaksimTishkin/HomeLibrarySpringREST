package com.epam.tishkin.client.service;

import com.epam.tishkin.messages.SignInForm;
import com.epam.tishkin.models.Role;
import com.epam.tishkin.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ClientUserService {
    private static final String REST_URI = "http://localhost:8088";
    private final RestTemplate restTemplate;

    @Autowired
    public ClientUserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String authorization(String login, String password) {
        SignInForm signInForm = new SignInForm(login, password);
        ResponseEntity<String> response = restTemplate.postForEntity(REST_URI + "/users/auth/signin",
                signInForm, String.class);
        return response.getBody();
    }

    /*
    public String authorization(String login, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("login", login);
        headers.set("password", password);
        ResponseEntity<String> response = restTemplate.exchange(REST_URI + "/users/authenticate", HttpMethod.POST,
                new HttpEntity<String>(headers), String.class);
        if (response.getStatusCodeValue() == HttpStatus.UNAUTHORIZED.value()) {
            return null;
        }
        return response.getBody();
    }
     */

    public String getRole(String login) {
        ResponseEntity<String> response = restTemplate.getForEntity(REST_URI + "/users/get-role/{login}", String.class, login);
        return response.getBody();
    }

    public boolean addUser(String login, String password) {
        User user = restTemplate.postForObject(REST_URI + "/users/add", new User(login, password, Role.VISITOR), User.class);
        return user != null;
    }

    public void blockUser(String login) {
        restTemplate.delete(REST_URI + "users/delete/{login}", login);
    }

    //TODO: implement writing and reading history
    public List<String> showHistory() {
        ResponseEntity<List<String>> history = restTemplate
                .exchange(REST_URI + "users/show-history", HttpMethod.GET,
                        null, new ParameterizedTypeReference<>(){});
        return history.getBody();
    }
}
