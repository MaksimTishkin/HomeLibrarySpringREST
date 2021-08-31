package com.epam.tishkin.server.service.impl;

import com.epam.tishkin.model.Author;
import com.epam.tishkin.model.Book;
import com.epam.tishkin.model.BooksList;
import com.epam.tishkin.server.repository.AuthorRepository;
import com.epam.tishkin.server.repository.BookRepository;
import com.epam.tishkin.server.service.BookService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {
    @Value("${directoryForTempFiles}")
    private String pathToTempFile;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public boolean addNewBook(Book book) {
        if (bookRepository.findBookByISBNumber(book.getISBNumber()) != null) {
            return false;
        }
        Optional<Author> currentAuthor = authorRepository.findById(book.getAuthor().getName());
        if (currentAuthor.isEmpty()) {
            authorRepository.save(book.getAuthor());
        }
        bookRepository.save(book);
        return true;
    }

    @Override
    public String deleteBook(String title) {
        Book book = bookRepository.findBookByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Book not found: " + title));
        bookRepository.delete(book);
        return "Book was deleted: " + title;
    }

    @Override
    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitleContaining(title);
    }

    @Override
    public List<Book> getBooksByAuthor(String authorName) {
        return bookRepository.findBooksByAuthorNameContaining(authorName);
    }

    @Override
    public Book getBookByISBN(String isbn) {
        return bookRepository.findBookByISBNumber(isbn);
    }

    @Override
    public List<Book> getBooksByYearRange(int startYear, int finishYear) {
        return bookRepository.findBooksByPublicationYearBetween(startYear, finishYear);
    }

    @Override
    public List<Book> getBooksByYearPagesNumberAndTitle(int publicationYear, int pages, String title) {
        return bookRepository.findBooksByPublicationYearAndPagesNumberAndTitle(publicationYear, pages, title);
    }

    @Override
    public Book getBookByFullTitle(String bookTitle) {
        return bookRepository.findBookByTitle(bookTitle)
                .orElseThrow(() -> new EntityNotFoundException("Book not found: " + bookTitle));
    }

    @Override
    public String addBooksFromCatalog(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return "File is empty";
        }
        try {
            File file = multipartToFile(multipartFile);
            int index = file.getName().lastIndexOf('.');
            int numberOfBooksAdded;
            if ("csv".equals(file.getName().substring(index + 1))) {
                numberOfBooksAdded = addBooksFromCSV(file);
            } else {
                numberOfBooksAdded = addBooksFromJSON(file);
            }
            Files.delete(file.toPath());
            return numberOfBooksAdded + " book(s) was added";
        } catch (IOException e) {
            e.printStackTrace();
            return "File input/output exception";
        }
    }

    private File multipartToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(pathToTempFile + multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        return file;
    }

    private int addBooksFromCSV(File file) throws IOException {
        int numberOfBooksAdded = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookParameters = line.split(";");
                String title = bookParameters[0];
                String author = bookParameters[1];
                String ISBNumber = bookParameters[2];
                int year = Integer.parseInt(bookParameters[3]);
                int pagesNumber = Integer.parseInt(bookParameters[4]);
                Book book = new Book(title, ISBNumber, year, pagesNumber, new Author(author));
                if (addNewBook(book)) {
                    numberOfBooksAdded++;
                }
            }
        }
        return numberOfBooksAdded;
    }

    private int addBooksFromJSON(File file) throws IOException {
        int numberOfBooksAdded;
        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            BooksList list = gson.fromJson(reader, BooksList.class);
            numberOfBooksAdded = (int) list.getBooks().stream().filter(this::addNewBook).count();
        }
        return numberOfBooksAdded;
    }
}
