package com.epam.tishkin;

import com.epam.tishkin.model.Role;
import com.epam.tishkin.server.SpringBootRestApplication;
import com.epam.tishkin.server.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest(classes = SpringBootRestApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    public void get_role_thenReturnRoleVisitor() throws Exception {
        when(userService.getRoleByLogin()).thenReturn(Role.ROLE_VISITOR);
        mockMvc.perform(post("/users/get-role"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",  is(Role.ROLE_VISITOR.toString())));
        verify(userService, times(1)).getRoleByLogin();
    }
}
