package com.epam.tishkin;

import com.epam.tishkin.model.User;
import com.epam.tishkin.server.SpringBootRestApplication;
import com.epam.tishkin.server.service.AdminService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest(classes = SpringBootRestApplication.class)
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService mockService;

    private static String login;
    private static String password;
    private static String firstHistoryMessage;
    private static String secondHistoryMessage;
    private static List<String> history;

    @BeforeAll
    static void init() {
        login = "login";
        password = "password";
        firstHistoryMessage = "Hello";
        secondHistoryMessage = "World";
        history = Arrays.asList(firstHistoryMessage, secondHistoryMessage);
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    public void add_user_thenReturnMessageThatUserAdded() throws Exception {
        String expectedResponse = "User registered successfully: " + login;
        when(mockService.addUser(any(User.class))).thenReturn(expectedResponse);
        mockMvc.perform(post("/admin/register")
                .header("login", login)
                .header("password", password))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedResponse)));
        verify(mockService, times(1)).addUser(any(User.class));
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    public void add_user_thenReturnMessageThatUserAlreadyExist() throws Exception {
        String expectedResponse = "Username is already taken: " + login;
        when(mockService.addUser(any(User.class)))
                .thenThrow(new EntityExistsException("Username is already taken: " + login));
        mockMvc.perform(post("/admin/register")
                .header("login", login)
                .header("password", password))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedResponse)));
        verify(mockService, times(1)).addUser(any(User.class));
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    public void delete_user_thenReturnMessageThatUserDeleted() throws Exception {
        String expectedResponse = "User deleted: " + login;
        when(mockService.deleteUser(login)).thenReturn(expectedResponse);
        mockMvc.perform(delete("/admin/delete/{login}", login))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedResponse)));
        verify(mockService, times(1)).deleteUser(login);
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    public void delete_user_thenReturnMessageThatUserNotFound() throws Exception {
        String expectedResponse = "User not found: " + login;
        when(mockService.deleteUser(login))
                .thenThrow(new EntityNotFoundException("User not found: " + login));
        mockMvc.perform(delete("/admin/delete/{login}", login))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedResponse)));
        verify(mockService, times(1)).deleteUser(login);
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    public void get_history_thenReturnListWithHistory() throws Exception {
        when(mockService.showHistory()).thenReturn(history);
        mockMvc.perform(get("/admin/get-history"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is(firstHistoryMessage)))
                .andExpect(jsonPath("$[1]", is(secondHistoryMessage)));
        verify(mockService, times(1)).showHistory();
    }
}
