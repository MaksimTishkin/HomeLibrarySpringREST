package com.epam.tishkin;

import com.epam.tishkin.model.Author;
import com.epam.tishkin.server.SpringBootRestApplication;
import com.epam.tishkin.server.service.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest(classes = SpringBootRestApplication.class)
@AutoConfigureMockMvc
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private AuthorService mockService;

    private static Author pushkinTestAuthor;

    @BeforeAll
    static void init() {
        pushkinTestAuthor = new Author("Pushkin");
    }

    @Test
    @WithMockUser
    public void add_newAuthor_thenReturnMessageThatAuthorAddedAndStatusOK() throws Exception {
        String expectedResponse = "Author was added: " + pushkinTestAuthor.getName();
        when(mockService.addAuthor(any(Author.class))).thenReturn(expectedResponse);
        mockMvc.perform(post("/authors/add")
                .content(objectMapper.writeValueAsString(pushkinTestAuthor))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedResponse)));
        verify(mockService, times(1)).addAuthor(any(Author.class));
    }

    @Test
    @WithMockUser
    public void add_newAuthor_thenReturnMessageThatAuthorAlreadyExistAndStatusOK() throws Exception {
        String expectedResponse = "Author already exists: " + pushkinTestAuthor.getName();
        when(mockService.addAuthor(any(Author.class)))
                .thenThrow(new EntityExistsException("Author already exists: " + pushkinTestAuthor.getName()));
        mockMvc.perform(post("/authors/add")
                .content(objectMapper.writeValueAsString(pushkinTestAuthor))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedResponse)));
        verify(mockService, times(1)).addAuthor(any(Author.class));
    }

    @Test
    @WithMockUser
    public void delete_author_thenReturnMessageThatAuthorDeletedAndStatusOK() throws Exception {
        String authorName = pushkinTestAuthor.getName();
        String expectedResponse = "Author was deleted: " + authorName;
        when(mockService.deleteAuthor(authorName)).thenReturn(expectedResponse);
        mockMvc.perform(delete("/authors/delete/{authorName}", authorName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedResponse)));
        verify(mockService, times(1)).deleteAuthor(authorName);
    }

    @Test
    @WithMockUser
    public void delete_author_thenReturnMessageThatAuthorNotFoundAndStatusOK() throws Exception {
        String authorName = pushkinTestAuthor.getName();
        String expectedResponse = "Author not found: " + authorName;
        when(mockService.deleteAuthor(authorName))
                .thenThrow(new EntityNotFoundException("Author not found: " + authorName));
        mockMvc.perform(delete("/authors/delete/{authorName}", authorName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedResponse)));
        verify(mockService, times(1)).deleteAuthor(authorName);
    }
}
