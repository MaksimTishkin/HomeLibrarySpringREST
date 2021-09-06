package com.epam.tishkin.client;

import com.epam.tishkin.client.config.LibraryClientConfig;
import com.epam.tishkin.client.service.*;
import com.epam.tishkin.model.Book;
import com.epam.tishkin.model.Bookmark;
import com.epam.tishkin.model.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class LibraryClient {
    private BookService bookService;
    private AuthorService authorService;
    private UserService userService;
    private BookmarkService bookmarkService;
    private AdminService adminService;
    private final static Logger logger = LogManager.getLogger(LibraryClient.class);
    private Role role;

    public static void main(String[] args) {
        LibraryClient libraryClient = new LibraryClient();
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(LibraryClientConfig.class);
        libraryClient.bookService = applicationContext.getBean(BookService.class);
        libraryClient.authorService = applicationContext.getBean(AuthorService.class);
        libraryClient.bookmarkService = applicationContext.getBean(BookmarkService.class);
        libraryClient.userService = applicationContext.getBean(UserService.class);
        libraryClient.adminService = applicationContext.getBean(AdminService.class);
        libraryClient.run();
    }

    private void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while(role == null) {
                role = authenticate(reader);
            }
            startLibraryUse(reader);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private Role authenticate(BufferedReader reader) throws IOException {
        System.out.println("Enter your login");
        String login = reader.readLine();
        System.out.println("Enter your password");
        String password = reader.readLine();
        return userService.authenticate(login, password);
    }

    public void startLibraryUse(BufferedReader reader) throws IOException {
        while (true) {
            System.out.println("1 Add a new book");
            System.out.println("2 Delete book");
            System.out.println("3 Add a new author");
            System.out.println("4 Delete author");
            System.out.println("5 Add books from CSV or JSON catalog");
            System.out.println("6 Find a book by title");
            System.out.println("7 Add a bookmark to a book");
            System.out.println("8 Delete a bookmark from a book");
            System.out.println("9 Find books by author");
            System.out.println("10 Find book by ISBN number");
            System.out.println("11 Find books by year range");
            System.out.println("12 Find a book by year, number of pages, and title");
            System.out.println("13 Find books with my bookmark");
            System.out.println("14 Exit");
            if (role == Role.ROLE_ADMINISTRATOR) {
                System.out.println("15 Settings (for administrators only)");
            }
            String request = reader.readLine();
            switch (request) {
                case "1":
                    addNewBook(reader);
                    break;
                case "2":
                    deleteBook(reader);
                    break;
                case "3":
                    addAuthor(reader);
                    break;
                case "4":
                    deleteAuthor(reader);
                    break;
                case "5":
                    addBooksFromCatalog(reader);
                    break;
                case "6":
                    searchBookByTitle(reader);
                    break;
                case "7":
                    addBookmark(reader);
                    break;
                case "8":
                    deleteBookmark(reader);
                    break;
                case "9":
                    searchBooksByAuthor(reader);
                    break;
                case "10":
                    searchBookByISBNumber(reader);
                    break;
                case "11":
                    searchBooksForYearRange(reader);
                    break;
                case "12":
                    searchBookByYearPagesNumberAndTitle(reader);
                    break;
                case "13":
                    showBooksWithBookmarks();
                    break;
                case "14":
                    return;
                case "15":
                    useAdditionalAdministratorFeatures(reader);
                    break;
            }
        }
    }
    private void addNewBook(BufferedReader reader) throws IOException {
        int publicationYear;
        int pagesNumber;
        System.out.println("Enter the book title");
        String bookTitle = reader.readLine();
        System.out.println("Enter the author of the book");
        String bookAuthor = reader.readLine();
        System.out.println("Enter the book ISBN number");
        String ISBNumber = reader.readLine();
        try {
            System.out.println("Enter the year of publication");
            publicationYear = Integer.parseInt(reader.readLine());
            System.out.println("Enter the number of pages");
            pagesNumber = Integer.parseInt(reader.readLine());
        } catch (NumberFormatException e) {
            logger.error("Incorrect number format");
            return;
        }
        logger.info(bookService.addNewBook(bookTitle, ISBNumber, publicationYear, pagesNumber, bookAuthor));
    }

    private void deleteBook(BufferedReader reader) throws IOException {
        System.out.println("Enter the book title you want to delete");
        String bookTitle = reader.readLine();
        logger.info(bookService.deleteBook(bookTitle));
    }

    private void addAuthor(BufferedReader reader) throws IOException {
        System.out.println("Enter the author's name");
        String bookAuthor = reader.readLine();
        logger.info(authorService.addAuthor(bookAuthor));
    }

    private void deleteAuthor(BufferedReader reader) throws IOException {
        System.out.println("Enter the author's name");
        String bookAuthor = reader.readLine();
        logger.info(authorService.deleteAuthor(bookAuthor));
    }

    private void searchBookByTitle(BufferedReader reader) throws IOException {
        System.out.println("Enter part of the book title");
        String bookTitle = reader.readLine();
        List<Book> foundBooks = bookService.searchBookByTitle(bookTitle);
        if (foundBooks == null || foundBooks.isEmpty()) {
            logger.info("No books found");
            return;
        }
        foundBooks.forEach(logger::info);
    }

    private void searchBooksByAuthor(BufferedReader reader) throws IOException {
        System.out.println("Enter part of the author's name");
        String bookAuthor = reader.readLine();
        List<Book> foundBooks = bookService.searchBooksByAuthor(bookAuthor);
        if (foundBooks == null || foundBooks.isEmpty()) {
            logger.info("No books found");
            return;
        }
        foundBooks.forEach(logger::info);
    }

    private void searchBookByISBNumber(BufferedReader reader) throws IOException {
        System.out.println("Enter book's ISBN number");
        String isbn = reader.readLine();
        Book foundBook = bookService.searchBookByISBN(isbn);
        if (foundBook == null) {
            logger.info("No books found");
            return;
        }
        logger.info(foundBook);
    }

    private void searchBooksForYearRange(BufferedReader reader) throws IOException {
        System.out.println("Enter the initial year value");
        String firstValue = reader.readLine();
        System.out.println("Enter the final year value");
        String secondValue = reader.readLine();
        try {
            int startYear = Integer.parseInt(firstValue);
            int finishYear = Integer.parseInt(secondValue);
            if (startYear > finishYear) {
                logger.info("Incorrect year specified: initial year " + startYear + " final year " + finishYear);
                return;
            }
            List<Book> foundBooks = bookService.searchBooksByYearRange(startYear, finishYear);
            if (foundBooks == null || foundBooks.isEmpty()) {
                logger.info("No books found");
                return;
            }
            foundBooks.forEach(logger::info);
        } catch (NumberFormatException e) {
            logger.info("Incorrect year value");
        }
    }

    private void searchBookByYearPagesNumberAndTitle(BufferedReader reader) throws IOException {
        try {
            System.out.println("Enter the year value");
            int year = Integer.parseInt(reader.readLine());
            System.out.println("Enter the pages number");
            int pagesNumber = Integer.parseInt(reader.readLine());
            System.out.println("Enter part of the book title");
            String bookTitle = reader.readLine();
            List<Book> foundBooks = bookService.searchBookByYearPagesNumberAndTitle(year, pagesNumber, bookTitle);
            if (foundBooks == null || foundBooks.isEmpty()) {
                logger.info("No books found");
                return;
            }
            foundBooks.forEach(logger::info);
        } catch (NumberFormatException e) {
            logger.info("Incorrect input data");
        }
    }

    private void addBookmark(BufferedReader reader) throws IOException {
        int pageNumber;
        System.out.println("Enter the book title");
        String bookTitle = reader.readLine();
        System.out.println("Enter the page number");
        try {
            pageNumber = Integer.parseInt(reader.readLine());
        } catch (NumberFormatException e) {
            logger.error("Incorrect number format");
            return;
        }
        logger.info(bookmarkService.addBookmark(bookTitle, pageNumber));
    }

    private void deleteBookmark(BufferedReader reader) throws IOException {
        System.out.println("Enter the book title");
        String bookTitle = reader.readLine();
        logger.info(bookmarkService.deleteBookmark(bookTitle));
    }

    private void showBooksWithBookmarks() {
        List<Bookmark> foundBookmarks = bookmarkService.showBookmarks();
        if (foundBookmarks == null || foundBookmarks.isEmpty()) {
            logger.info("There are no bookmarks");
            return;
        }
        foundBookmarks.forEach(logger::info);
    }

    private void addBooksFromCatalog(BufferedReader reader) throws IOException {
        System.out.println("Enter the path to the folder");
        String filePath = reader.readLine();
        if (isFileExtensionCorrect(filePath)) {
            logger.info(bookService.addBooksFromCatalog(filePath));
        } else {
            logger.info("Incorrect file's type");
        }
    }

    private boolean isFileExtensionCorrect(String path) {
        int index = path.lastIndexOf('.');
        String extension = path.substring(index + 1);
        return "json".equals(extension) || "csv".equals(extension);
    }

    private void useAdditionalAdministratorFeatures(BufferedReader reader) throws IOException {
        if (role != Role.ROLE_ADMINISTRATOR) {
            return;
        }
        System.out.println("1 Add a new user");
        System.out.println("2 Block user");
        System.out.println("3 Show history");
        System.out.println("4 Go back to the previous menu");
        String request = reader.readLine();
        switch (request) {
            case "1":
                addUser(reader);
                break;
            case "2":
                blockUser(reader);
                break;
            case "3":
                showHistory();
                break;
            case "4":
                break;
        }
    }

    private void addUser(BufferedReader reader) throws IOException {
        System.out.println("Enter user login");
        String login = reader.readLine();
        System.out.println("Enter user password");
        String password = reader.readLine();
        logger.info(adminService.registerNewUser(login, password));
    }

    private void blockUser(BufferedReader reader) throws IOException {
        System.out.println("Enter user login");
        String login = reader.readLine();
        logger.info(adminService.blockUser(login));
    }

    private void showHistory() {
        List<String> fullHistory = adminService.showHistory();
        if (fullHistory == null || fullHistory.isEmpty()) {
            logger.info("There are no entries in the history");
            return;
        }
        fullHistory.forEach(logger::info);
    }
}
