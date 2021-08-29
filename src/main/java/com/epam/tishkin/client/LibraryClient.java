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
import java.time.Year;
import java.util.List;

public class LibraryClient {
    private ClientBookService clientBookService;
    private ClientAuthorService clientAuthorService;
    private ClientUserService clientUserService;
    private ClientBookmarkService clientBookmarkService;
    private ClientAdminService clientAdminService;
    private final static Logger logger = LogManager.getLogger(LibraryClient.class);
    private Role role;

    public static void main(String[] args) {
        LibraryClient libraryClient = new LibraryClient();
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(LibraryClientConfig.class);
        libraryClient.clientBookService = applicationContext.getBean(ClientBookService.class);
        libraryClient.clientAuthorService = applicationContext.getBean(ClientAuthorService.class);
        libraryClient.clientBookmarkService = applicationContext.getBean(ClientBookmarkService.class);
        libraryClient.clientUserService = applicationContext.getBean(ClientUserService.class);
        libraryClient.clientAdminService = applicationContext.getBean(ClientAdminService.class);
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
        return clientUserService.authenticate(login, password);
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
                    //addBooksFromCatalog(reader);
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
        Integer publicationYear;
        String ISBNumber;
        Integer pagesNumber;
        System.out.println("Enter the book title");
        String bookTitle = reader.readLine();
        if (bookTitle.isEmpty()) {
            logger.info("Book title is empty");
            return;
        }
        System.out.println("Enter the author of the book");
        String bookAuthor = reader.readLine();
        if (bookAuthor.isEmpty()) {
            logger.info("The author is empty");
            return;
        }
        System.out.println("Enter the book ISBN number");
        ISBNumber = reader.readLine();
        if (!isISBNumberCorrect(ISBNumber)) {
            logger.info("Incorrect ISBNumber: " + ISBNumber);
            return;
        }
        System.out.println("Enter the year of publication");
        String year = reader.readLine();
        publicationYear = checkYearOfPublication(year);
        if (publicationYear == null) {
            logger.info("Incorrect year of publication: " + year);
            return;
        }
        System.out.println("Enter the number of pages");
        String number = reader.readLine();
        pagesNumber = checkNumberOfPages(number);
        if (pagesNumber == null) {
            logger.info("Invalid page value: " + number);
            return;
        }
        logger.info(clientBookService.addNewBook(bookTitle, ISBNumber, publicationYear, pagesNumber, bookAuthor));
    }

    private void deleteBook(BufferedReader reader) throws IOException {
        System.out.println("Enter the book title you want to delete");
        String bookTitle = reader.readLine();
        logger.info(clientBookService.deleteBook(bookTitle));
    }

    private void addAuthor(BufferedReader reader) throws IOException {
        System.out.println("Enter the author's name");
        String bookAuthor = reader.readLine();
        logger.info(clientAuthorService.addAuthor(bookAuthor));
    }

    private void deleteAuthor(BufferedReader reader) throws IOException {
        System.out.println("Enter the author's name");
        String bookAuthor = reader.readLine();
        logger.info(clientAuthorService.deleteAuthor(bookAuthor));
    }

    private void searchBookByTitle(BufferedReader reader) throws IOException {
        System.out.println("Enter part of the book title");
        String bookTitle = reader.readLine();
        List<Book> foundBooks = clientBookService.searchBookByTitle(bookTitle);
        if (foundBooks == null || foundBooks.isEmpty()) {
            logger.info("No books found");
            return;
        }
        foundBooks.forEach(logger::info);
    }

    private void searchBooksByAuthor(BufferedReader reader) throws IOException {
        System.out.println("Enter part of the author's name");
        String bookAuthor = reader.readLine();
        List<Book> foundBooks = clientBookService.searchBooksByAuthor(bookAuthor);
        if (foundBooks == null || foundBooks.isEmpty()) {
            logger.info("No books found");
            return;
        }
        foundBooks.forEach(logger::info);
    }

    private void searchBookByISBNumber(BufferedReader reader) throws IOException {
        System.out.println("Enter book's ISBN number");
        String isbn = reader.readLine();
        Book foundBook = clientBookService.searchBookByISBN(isbn);
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
            List<Book> foundBooks = clientBookService.searchBooksByYearRange(startYear, finishYear);
            if (foundBooks == null || foundBooks.isEmpty()) {
                logger.info("No books found");
                return;
            }
            foundBooks.forEach(logger::info);
        } catch (NumberFormatException e) {
            logger.info("Incorrect year specified: initial year " + firstValue + " final year " + secondValue);
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
            List<Book> foundBooks = clientBookService.searchBookByYearPagesNumberAndTitle(year, pagesNumber, bookTitle);
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
        System.out.println("Enter the book title");
        String bookTitle = reader.readLine();
        System.out.println("Enter the page number");
        String page = reader.readLine();
        Integer pageNumber = checkNumberOfPages(page);
        if (pageNumber == null) {
            logger.info("Invalid page value - " + page);
            return;
        }
        logger.info(clientBookmarkService.addBookmark(bookTitle, pageNumber));
    }

    private void deleteBookmark(BufferedReader reader) throws IOException {
        System.out.println("Enter the book title");
        String bookTitle = reader.readLine();
        logger.info(clientBookmarkService.deleteBookmark(bookTitle));
    }

    private void showBooksWithBookmarks() {
        List<Bookmark> foundBookmarks = clientBookmarkService.showBookmarks();
        if (foundBookmarks == null || foundBookmarks.isEmpty()) {
            logger.info("There are no bookmarks");
            return;
        }
        foundBookmarks.forEach(logger::info);
    }

    private boolean isISBNumberCorrect(String number) {
        int correctIsbnNumberLength = 13;
        if (number.length() != correctIsbnNumberLength) {
            return false;
        }
        try {
            Long.parseLong(number);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private Integer checkYearOfPublication(String year) {
        int publicationYear;
        try {
            publicationYear = Integer.parseInt(year);
            int currentYear = Year.now().getValue();
            int yearOfFirstBookInWorld = 1457;
            if (publicationYear < yearOfFirstBookInWorld || publicationYear > currentYear) {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return publicationYear;
    }

    private Integer checkNumberOfPages(String number) {
        int pagesNumber;
        try {
            pagesNumber = Integer.parseInt(number);
            if (pagesNumber <= 0) {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return pagesNumber;
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
        String response = clientAdminService.registerNewUser(login, password);
        logger.info(response);
    }

    private void blockUser(BufferedReader reader) throws IOException {
        System.out.println("Enter user login");
        String login = reader.readLine();
        logger.info(clientAdminService.blockUser(login));
    }

    private void showHistory() {
        List<String> fullHistory = clientAdminService.showHistory();
        if (fullHistory == null || fullHistory.isEmpty()) {
            logger.info("There are no entries in the history");
            return;
        }
        fullHistory.forEach(logger::info);
    }
}