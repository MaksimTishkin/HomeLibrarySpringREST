package com.epam.tishkin;

import com.epam.tishkin.model.Bookmark;
import com.epam.tishkin.server.SpringBootRestApplication;
import com.epam.tishkin.server.service.BookmarkService;
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

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.Matchers.is;

@SpringBootTest(classes = SpringBootRestApplication.class)
@AutoConfigureMockMvc
public class BookmarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private BookmarkService mockService;

    private static Bookmark bookmark;
    private static List<Bookmark> usersBookmarks;

    @BeforeAll
    static void init() {
        bookmark = new Bookmark("MyFavoriteBook", 77);
        usersBookmarks = Collections.singletonList(bookmark);
    }

    @Test
    @WithMockUser
    public void add_bookmark_thenReturnMessageThatBookmarkAdded() throws Exception {
        String expectedResponse = "Bookmark was added: " + bookmark.getTitle();
        when(mockService.addBookmark(any(Bookmark.class))).thenReturn(expectedResponse);
        mockMvc.perform(post("/bookmarks/add")
                .content(objectMapper.writeValueAsString(bookmark))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedResponse)));
        verify(mockService, timeout(1)).addBookmark(any(Bookmark.class));
    }

    @Test
    @WithMockUser
    public void add_bookmark_thenReturnMessageThatBookmarkExists() throws Exception {
        String expectedResponse = "Bookmark already exists: " + bookmark.getTitle();
        when(mockService.addBookmark(any(Bookmark.class)))
                .thenThrow(new EntityExistsException("Bookmark already exists: " + bookmark.getTitle()));
        mockMvc.perform(post("/bookmarks/add")
                .content(objectMapper.writeValueAsString(bookmark))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedResponse)));
        verify(mockService, timeout(1)).addBookmark(any(Bookmark.class));
    }

    @Test
    @WithMockUser
    public void add_bookmark_thenReturnMessageThatBookmarkNotFound() throws Exception {
        String expectedResponse = "Book not found: " + bookmark.getTitle();
        when(mockService.addBookmark(any(Bookmark.class)))
                .thenThrow(new EntityNotFoundException("Book not found: " + bookmark.getTitle()));
        mockMvc.perform(post("/bookmarks/add")
                .content(objectMapper.writeValueAsString(bookmark))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedResponse)));
        verify(mockService, timeout(1)).addBookmark(any(Bookmark.class));
    }

    @Test
    @WithMockUser
    public void delete_bookmark_thenReturnMessageThatBookmarkDeleted() throws Exception {
        String expectedResponse = "Bookmark was deleted: " + bookmark.getTitle();
        when(mockService.deleteBookmark(bookmark.getTitle())).thenReturn(expectedResponse);
        mockMvc.perform(delete("/bookmarks/delete/{title}", bookmark.getTitle()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedResponse)));
        verify(mockService, timeout(1)).deleteBookmark(bookmark.getTitle());
    }

    @Test
    @WithMockUser
    public void delete_bookmark_thenReturnMessageThatBookmarkNotFound() throws Exception {
        String expectedResponse = "Bookmark not found" + bookmark.getTitle();
        when(mockService.deleteBookmark(bookmark.getTitle()))
                .thenThrow(new EntityNotFoundException("Bookmark not found" + bookmark.getTitle()));
        mockMvc.perform(delete("/bookmarks/delete/{title}", bookmark.getTitle()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedResponse)));
        verify(mockService, timeout(1)).deleteBookmark(bookmark.getTitle());
    }

    @Test
    @WithMockUser
    public void get_UsersBookmarks_thenReturnListWithOneBookmark() throws Exception {
        when(mockService.showBookmarks()).thenReturn(usersBookmarks);
        mockMvc.perform(get("/bookmarks/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(bookmark.getTitle())))
                .andExpect(jsonPath("$[0].page", is(bookmark.getPage())));
        verify(mockService, timeout(1)).showBookmarks();
    }
}
