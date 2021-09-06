package com.epam.tishkin;

import com.epam.tishkin.model.Author;
import com.epam.tishkin.model.Book;
import com.epam.tishkin.server.SpringBootRestApplication;
import com.epam.tishkin.server.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.Matchers.is;

@SpringBootTest(classes = SpringBootRestApplication.class)
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private BookService mockService;

    private static Book lukomoreTestBook;
    private static Book ruslanAndLudmilaTestBook;
    private static List<Book> listWithTwoBooks;
    private static List<Book> listWithOneBook;

    @BeforeAll
    static void init() {
        lukomoreTestBook = new Book("Lukomore", "5478569856321",
                1987, 123, new Author("Pushkin"));
        ruslanAndLudmilaTestBook = new Book("Ruslan and Ludmila", "6325632541254",
                1985, 321, new Author("Pushkin"));
        listWithOneBook = Collections.singletonList(lukomoreTestBook);
        listWithTwoBooks = Arrays.asList(lukomoreTestBook, ruslanAndLudmilaTestBook);
    }

    @Test
    @WithMockUser
    public void add_newBook_thenReturnMessageThatBookAddedAndStatusOK() throws Exception {
        String expectedResponse = "Book was added " + ruslanAndLudmilaTestBook.getTitle();
        when(mockService.addNewBook(any(Book.class))).thenReturn(true);
        mockMvc.perform(post("/books/add")
                .content(objectMapper.writeValueAsString(ruslanAndLudmilaTestBook))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedResponse)));
        verify(mockService, times(1)).addNewBook(any(Book.class));
    }

    @Test
    @WithMockUser
    public void add_newBook_thenReturnMessageThatBookAlreadyExistAndStatusOK() throws Exception {
        String expectedResponse = "Book already exists " + lukomoreTestBook.getTitle();
        when(mockService.addNewBook(any(Book.class))).thenReturn(false);
        mockMvc.perform(post("/books/add")
                .content(objectMapper.writeValueAsString(lukomoreTestBook))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedResponse)));
        verify(mockService, times(1)).addNewBook(any(Book.class));
    }

    @Test
    @WithMockUser
    public void delete_Book_thenReturnMessageThatBookDeletedAndStatusOK() throws Exception {
        String title = ruslanAndLudmilaTestBook.getTitle();
        String expectedResponse = "Book was deleted: " + title;
        when(mockService.deleteBook(title)).thenReturn(expectedResponse);
        mockMvc.perform(delete("/books/delete/{title}", title))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedResponse)));
        verify(mockService, times(1)).deleteBook(title);
    }

    @Test
    @WithMockUser
    public void delete_Book_thenReturnMessageThatBookNotFoundAndStatusOK() throws Exception {
        String title = lukomoreTestBook.getTitle();
        String expectedResponse = "Book not found: " + title;
        when(mockService.deleteBook(title)).thenThrow(new EntityNotFoundException(expectedResponse));
        mockMvc.perform(delete("/books/delete/{title}", title))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedResponse)));
        verify(mockService, times(1)).deleteBook(title);
    }

    @Test
    @WithMockUser
    public void find_booksByPartTitle_thenReturnListWithTwoBooksAndStatusOK() throws Exception {
        String partOfTitle = "r";
        when(mockService.getBooksByTitle(partOfTitle)).thenReturn(listWithTwoBooks);
        mockMvc.perform(get("/books/get-by-title/{partOfTitle}", partOfTitle))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is(lukomoreTestBook.getTitle())))
                .andExpect(jsonPath("$[0].isbnumber", is(lukomoreTestBook.getISBNumber())))
                .andExpect(jsonPath("$[0].publicationYear", is(lukomoreTestBook.getPublicationYear())))
                .andExpect(jsonPath("$[0].pagesNumber", is(lukomoreTestBook.getPagesNumber())))
                .andExpect(jsonPath("$[0].author.name", is(lukomoreTestBook.getAuthor().getName())));
        verify(mockService, times(1)).getBooksByTitle(partOfTitle);
    }

    @Test
    @WithMockUser
    public void find_booksByAuthor_thenReturnListWithTwoBooksAndStatusOK() throws Exception {
        String authorName = "Pushkin";
        when(mockService.getBooksByAuthor(authorName)).thenReturn(listWithTwoBooks);
        mockMvc.perform(get("/books/get-by-author/{authorName}", authorName))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].title", is(ruslanAndLudmilaTestBook.getTitle())))
                .andExpect(jsonPath("$[1].isbnumber", is(ruslanAndLudmilaTestBook.getISBNumber())))
                .andExpect(jsonPath("$[1].publicationYear", is(ruslanAndLudmilaTestBook.getPublicationYear())))
                .andExpect(jsonPath("$[1].pagesNumber", is(ruslanAndLudmilaTestBook.getPagesNumber())))
                .andExpect(jsonPath("$[1].author.name", is(ruslanAndLudmilaTestBook.getAuthor().getName())));
        verify(mockService, times(1)).getBooksByAuthor(authorName);
    }

    @Test
    @WithMockUser
    public void find_bookByISBN_thenReturnBookAndStatusOK() throws Exception {
        String isbn = "6325632541254";
        when(mockService.getBookByISBN(isbn)).thenReturn(ruslanAndLudmilaTestBook);
        mockMvc.perform(get("/books/get-by-isbn/{isbn}", isbn))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(ruslanAndLudmilaTestBook.getTitle())))
                .andExpect(jsonPath("$.isbnumber", is(ruslanAndLudmilaTestBook.getISBNumber())))
                .andExpect(jsonPath("$.publicationYear", is(ruslanAndLudmilaTestBook.getPublicationYear())))
                .andExpect(jsonPath("$.pagesNumber", is(ruslanAndLudmilaTestBook.getPagesNumber())))
                .andExpect(jsonPath("$.author.name", is(ruslanAndLudmilaTestBook.getAuthor().getName())));
        verify(mockService, times(1)).getBookByISBN(isbn);
    }

    @Test
    @WithMockUser
    public void find_booksByYearBetween_thenReturnListWithTwoBooksAndStatusOK() throws Exception {
        int initialYear = 1984;
        int finalYear = 1988;
        when(mockService.getBooksByYearRange(initialYear, finalYear)).thenReturn(listWithTwoBooks);
        mockMvc.perform(get("/books/get-by-years/{initialYear}/{finalYear}", initialYear, finalYear))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].title", is(ruslanAndLudmilaTestBook.getTitle())))
                .andExpect(jsonPath("$[1].isbnumber", is(ruslanAndLudmilaTestBook.getISBNumber())))
                .andExpect(jsonPath("$[1].publicationYear", is(ruslanAndLudmilaTestBook.getPublicationYear())))
                .andExpect(jsonPath("$[1].pagesNumber", is(ruslanAndLudmilaTestBook.getPagesNumber())))
                .andExpect(jsonPath("$[1].author.name", is(ruslanAndLudmilaTestBook.getAuthor().getName())));
        verify(mockService, times(1)).getBooksByYearRange(initialYear, finalYear);
    }

    @Test
    @WithMockUser
    public void find_booksByYearPagesTitle_thenReturnListWithOneBookAndStatusOK() throws Exception {
        int year = 1987;
        int pages = 123;
        String title = "Lukomore";
        when(mockService.getBooksByYearPagesNumberAndTitle(year, pages, title)).thenReturn(listWithOneBook);
        mockMvc.perform(get("/books/get-by-year-pages-title/{year}/{pages}/{title}", year, pages, title))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(lukomoreTestBook.getTitle())))
                .andExpect(jsonPath("$[0].isbnumber", is(lukomoreTestBook.getISBNumber())))
                .andExpect(jsonPath("$[0].publicationYear", is(lukomoreTestBook.getPublicationYear())))
                .andExpect(jsonPath("$[0].pagesNumber", is(lukomoreTestBook.getPagesNumber())))
                .andExpect(jsonPath("$[0].author.name", is(lukomoreTestBook.getAuthor().getName())));
        verify(mockService, times(1)).getBooksByYearPagesNumberAndTitle(year, pages, title);
    }

    @Test
    @WithMockUser
    public void find_bookByFullTitle_thenReturnBookAndStatusOK() throws Exception {
        String title = "Lukomore";
        when(mockService.getBookByFullTitle(title)).thenReturn(lukomoreTestBook);
        mockMvc.perform(get("/books/get-by-full-title/{title}", title))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(lukomoreTestBook.getTitle())))
                .andExpect(jsonPath("$.isbnumber", is(lukomoreTestBook.getISBNumber())))
                .andExpect(jsonPath("$.publicationYear", is(lukomoreTestBook.getPublicationYear())))
                .andExpect(jsonPath("$.pagesNumber", is(lukomoreTestBook.getPagesNumber())))
                .andExpect(jsonPath("$.author.name", is(lukomoreTestBook.getAuthor().getName())));
        verify(mockService, times(1)).getBookByFullTitle(title);
    }

    @Test
    @WithMockUser
    public void add_booksFromCatalog_thenReturnMessageWithNumberOfBooksAddedAndStatusOK() throws Exception {
        MockMultipartFile catalogWithBooks = new MockMultipartFile("file", "hello.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        int numberOfBooksAdded = 3;
        String expectedResponse = numberOfBooksAdded + " book(s) was added";
        when(mockService.addBooksFromCatalog(catalogWithBooks)).thenReturn(expectedResponse);
        mockMvc.perform(multipart("/books/add-from-catalog").file(catalogWithBooks))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedResponse)));
        verify(mockService, times(1)).addBooksFromCatalog(catalogWithBooks);
    }
}
