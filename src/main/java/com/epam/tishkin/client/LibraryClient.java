package com.epam.tishkin.client;

import com.epam.tishkin.client.config.LibraryClientConfig;
import com.epam.tishkin.client.service.ClientAuthorService;
import com.epam.tishkin.client.service.ClientBookService;
import com.epam.tishkin.client.service.ClientBookmarkService;
import com.epam.tishkin.client.service.ClientUserService;
import com.epam.tishkin.models.Book;
import com.epam.tishkin.models.Bookmark;
import org.apache.catalina.core.ApplicationContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    final static Logger logger = LogManager.getLogger(LibraryClient.class);
    private String jwt;
    private String role;

    public static void main(String[] args) {
        LibraryClient libraryClient = new LibraryClient();
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(LibraryClientConfig.class);
        libraryClient.clientBookService = applicationContext.getBean(ClientBookService.class);
        libraryClient.clientAuthorService = applicationContext.getBean(ClientAuthorService.class);
        libraryClient.clientBookmarkService = applicationContext.getBean(ClientBookmarkService.class);
        libraryClient.clientUserService = applicationContext.getBean(ClientUserService.class);
        libraryClient.run();
    }

    private void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            /*
            while ((jwt = authorization(reader)) == null) {
                logger.info("Incorrect login/password");
            }
            role = clientUserService.getRole(jwt);
            startLibraryUse(reader);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
         */
            startLibraryUse(reader);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }

    private String authorization(BufferedReader reader) throws IOException {
        System.out.println("Enter your login");
        String login = reader.readLine();
        System.out.println("Enter your password");
        String password = reader.readLine();
        return clientUserService.authorization(login, password);
    }

    public void startLibraryUse(BufferedReader reader) throws IOException {
        while(true) {
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
            if ("ADMINISTRATOR".equals(role)) {
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
            int pagesNumber= Integer.parseInt(reader.readLine());
            System.out.println("Enter part of the book title");
            String bookTitle = reader.readLine();
            List<Book> foundBooks = clientBookService.searchBookByYearPagesNumberAndTitle(year, pagesNumber, bookTitle);
            if (foundBooks == null || foundBooks.isEmpty()) {
                logger.info("No books found");
                return;
            }
            logger.info(foundBooks);
        } catch (NumberFormatException e) {
            logger.info("Incorrect input data");
        }
    }

    private void addBookmark(BufferedReader reader) throws IOException {
        System.out.println("Enter the book title");
        String bookTitle = reader.readLine();
        Book book = clientBookService.findBookByFullTitle(bookTitle);
        if (book == null) {
            logger.info(bookTitle + " book not found");
            return;
        }
        System.out.println("Enter the page number");
        String page = reader.readLine();
        Integer pageNumber = checkNumberOfPages(page);
        if (pageNumber == null || pageNumber >= book.getPagesNumber()) {
            logger.info("Invalid page value - " + pageNumber);
            return;
        }
        if (!clientBookmarkService.addBookmark(bookTitle, pageNumber)) {
            logger.info("The bookmark already exists in this book");
            return;
        }
        logger.info("Bookmark was added to the " + bookTitle + " book on the page " + pageNumber);
    }

    private void deleteBookmark(BufferedReader reader) throws IOException {
        System.out.println("Enter the book title");
        String bookTitle = reader.readLine();
        clientBookmarkService.deleteBookmark(bookTitle);
        logger.info("Bookmark deleted - book title: " + bookTitle);
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
        if (number.length() != 13) {
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
        if (!role.equals("ADMINISTRATOR")) {
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
        if (!role.equals("ADMINISTRATOR")) {
            return;
        }
        System.out.println("Enter user login");
        String login = reader.readLine();
        System.out.println("Enter user password");
        String password = reader.readLine();
        if (!clientUserService.addUser(login, password)) {
            logger.info("This user already exists - " + login);
            return;
        }
        logger.info("New user added - " + login);
    }

    private void blockUser(BufferedReader reader) throws IOException {
        if (!role.equals("ADMINISTRATOR")) {
            return;
        }
        System.out.println("Enter user login");
        String login = reader.readLine();
        clientUserService.blockUser(login);
        logger.info("User deleted - " + login);
    }

    private void showHistory() {
        if (!role.equals("ADMINISTRATOR")) {
            return;
        }
        List<String> fullHistory = clientUserService.showHistory();
        fullHistory.forEach(logger::info);
    }

    /*
    private void addBooksFromCatalog(BufferedReader reader) throws IOException {
        System.out.println("Enter the path to the folder");
        String filePath = reader.readLine();
        try {
            if (isFileExtensionCorrect(filePath)) {
                String booksAdded = clientServiceREST.addBooksFromCatalog(filePath, jwt);
                logger.info("Number of books added from catalog: " + booksAdded);
            } else {
                logger.info("Incorrect file's type");
            }
        } catch (AccessDeniedException e) {
            logger.error(e.getMessage());
        }
    }


    private boolean isFileExtensionCorrect(String path) {
        int index = path.lastIndexOf('.');
        String extension = path.substring(index + 1);
        return "json".equals(extension) || "csv".equals(extension);
    }
    *//*
    private void addBooksFromCatalog(BufferedReader reader) throws IOException {
        System.out.println("Enter the path to the folder");
        String filePath = reader.readLine();
        try {
            if (isFileExtensionCorrect(filePath)) {
                String booksAdded = clientServiceREST.addBooksFromCatalog(filePath, jwt);
                logger.info("Number of books added from catalog: " + booksAdded);
            } else {
                logger.info("Incorrect file's type");
            }
        } catch (AccessDeniedException e) {
            logger.error(e.getMessage());
        }
    }


    private boolean isFileExtensionCorrect(String path) {
        int index = path.lastIndexOf('.');
        String extension = path.substring(index + 1);
        return "json".equals(extension) || "csv".equals(extension);
    }
    */
}